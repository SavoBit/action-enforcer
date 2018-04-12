/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.selfnet.ae.db_model.dao.ActionDAO;
import eu.selfnet.ae.db_model.dao.TacticDAO;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import eu.selfnet.ae.storm.register.ContextResolutionBolt;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class ActionInstanceBolt extends BaseRichBolt {

    private static final Gson GSON = new GsonBuilder().create();
    private TacticDAO tacticDAO;
    private ActionDAO actionDAO;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.tacticDAO = new TacticDAO();
        this.actionDAO = new ActionDAO();
    }

    @Override
    public void execute(Tuple input) {
        Action a = (Action) input.getValueByField("action");

        Logger.getLogger(ContextResolutionBolt.class.getName())
                .log(Level.INFO, "Solve action ID {0}", a.getActionOrder());

        // Query the Service Inventory for the ID
        a.setActionContextID("Amazing ID");
        a.setStatus("Ready To Enforce");
        actionDAO.update(a);
        if (a.getActionOrder() == 1) {
            Tactic t = tacticDAO.findByRequestID(a.getTactic().getRequestId());
            t.setStatus("Ready To Enforce");
            tacticDAO.update(t);
        }
    }

}
