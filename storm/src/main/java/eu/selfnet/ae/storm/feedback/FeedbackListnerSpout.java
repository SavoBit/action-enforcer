/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.feedback;

import com.mongodb.client.model.Filters;
import eu.selfnet.ae.db_model.dao.TacticReader;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.bson.Document;
import org.bson.conversions.Bson;

public class FeedbackListnerSpout extends BaseRichSpout {

    private SpoutOutputCollector _collector;
    private TacticReader tacticReader;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("feedback-tactic-id"));
    }

    @Override
    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) {

        this._collector = collector;

        // Sets Mongo Spout Helper 
        // to search for tactics ready for feedback and set them to reporting
        Bson tacticQuery = Filters.regex("status", Tactic.FEEDBACK);
        Bson tacticUpdate = new Document(
                "$set", new Document("status", Tactic.REPORTING)
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

        this._collector.emit(new Values(t.getRequestId()));
    }

}
