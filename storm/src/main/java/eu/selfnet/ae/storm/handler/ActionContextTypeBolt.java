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
import eu.selfnet.ae.storm.dto.ActionDTO;
import eu.selfnet.ae.storm.register.ContextResolutionBolt;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Class that solves the Service Context Type of a given action. This class is
 * intended to check if the context type of an action mets the tactic type, if
 * so the action can be enforced. Otherwise it must be resolved the new Service
 * Context ID for this action.
 *
 */
public class ActionContextTypeBolt extends BaseRichBolt {

    private static final Gson GSON = new GsonBuilder().create();
    private OutputCollector _collector;
    private TacticDAO tacticDAO;
    private ActionDAO actionDAO;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(
                "Action-Instance", new Fields("ToActionInstanceBolt")
        );
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        this.tacticDAO = new TacticDAO();
        this.actionDAO = new ActionDAO();
        this._collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Action a = (Action) input.getValueByField("request");

        String message = "Solve action type "
                + a.getActionOrder()
                + " from tactic "
                + a.getTactic().getRequestId();

        Logger.getLogger(ContextResolutionBolt.class.getName())
                .log(Level.INFO, message);

        // TODO: When SCAPI is available
        // Query Service Catalogue
        // Hardcoded resolution based on action operation
        // Based on E2EO Specification for SELFNET v1.0
        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);
//        if (dto.getActionOption().getOperation().contains("Flow") ) { //|| dto.getActionOption().getOperation().contains("modify") || dto.getActionOption().getOperation().contains("eploy")) {
//          a.setActionContextType("CFS.IM");
//        } else {
//            a.setActionContextType("CFS.IM");
//        }
        // Hardcoded context ID
        a.setActionContextID(a.getTactic().getContextId());

        /* Check if services match
        if (!a.getActionContextType().equals(a.getTactic().getContextType())) {
            // Go to ActionInstance and solve the ID
            this.emit(_collector, a);
            return;
        }
         */
        // The action is now Ready to be enforced
        a.setReady();

        actionDAO.update(a);
        if (a.getActionOrder() == 1) {
            // The tactic can be enforced since the first action is available
            Tactic t = tacticDAO.findByRequestID(a.getTactic().getRequestId());
            t.setReadyToEnforce();
            tacticDAO.update(t);
        }
    }

    public void emit(OutputCollector collector, Action a) {
        collector.emit("Action-Instance", new Values(a));
    }

}
