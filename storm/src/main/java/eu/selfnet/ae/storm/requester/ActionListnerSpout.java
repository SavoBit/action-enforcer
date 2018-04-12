/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.requester;

import com.mongodb.client.model.Filters;
import eu.selfnet.ae.db_model.dao.ActionDAO;
import eu.selfnet.ae.db_model.dao.TacticDAO;
import eu.selfnet.ae.db_model.dao.TacticReader;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ActionListnerSpout extends BaseRichSpout {

    private SpoutOutputCollector _collector;
    private ActionDAO actionDAO;
    private TacticDAO tacticDAO;
    private TacticReader tacticReader;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("action"));
    }

    @Override
    public void open(Map conf,
            TopologyContext context, SpoutOutputCollector collector) {
        this._collector = collector;

        this.actionDAO = new ActionDAO();
        this.tacticDAO = new TacticDAO();

        // Sets Mongo Spout Helper 
        // to search for tactics ready and set them to ongoing
        Bson tacticQuery = Filters.regex("status", Tactic.READY_TO_ENFORCE);
        Bson tacticUpdate = new Document(
                "$set", new Document("status", Tactic.ONGOING)
        );

        // Start Mongo Spout Helper
        this.tacticReader = new TacticReader(tacticQuery, tacticUpdate);
        this.tacticReader.startRunning();
        Thread t = new Thread(this.tacticReader);
        t.start();
    }

    @Override
    public void nextTuple() {
        Tactic t = this.tacticReader.next();

        if (t == null) {
            Utils.sleep(100);
            return;
        }

        Action a = actionDAO.findByTactic(t.getRequestId(),
                t.getOngoingAction());
        if (!a.getStatus().equals(Action.READY)) {
            t.setReadyToEnforce();
            // Give time to resolve action dependencies
            Utils.sleep(100);
            this.tacticDAO.update(t);
            return;
        }

        this._collector.emit(new Values(a));
    }

    @Override
    public void close() {
        this.tacticReader.stopRunning();
    }
}
