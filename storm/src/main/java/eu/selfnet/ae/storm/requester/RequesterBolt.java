/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.requester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.utils.Base64Coder;
import eu.selfnet.ae.conf_reader.ConfReader;
import eu.selfnet.ae.db_model.dao.ActionDAO;
import eu.selfnet.ae.db_model.dao.TacticDAO;
import eu.selfnet.ae.db_model.model.orchestrator.OrRequest;
import eu.selfnet.ae.db_model.model.orchestrator.OrResponse;
import eu.selfnet.ae.db_model.model.orchestrator.OrResponseError;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class RequesterBolt extends BaseRichBolt {

    private ConfReader config;
    private String host;
    private TacticDAO tacticDAO;
    private ActionDAO actionDAO;
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        this.config = new ConfReader();
        HashMap<String, String> map
                = this.config.getSection("OR_REST");
        this.host = map.get("host");
        this.tacticDAO = new TacticDAO();
        this.actionDAO = new ActionDAO();

    }

    @Override
    public void execute(Tuple input) {
        Logger.getLogger(RequesterBolt.class.getName())
                .log(Level.INFO, "Received request");

        OrRequest orRequest = (OrRequest) input.getValue(0);
        Action action = (Action) input.getValue(1);

        Tactic tactic = tacticDAO.findByRequestID(
                action.getTactic().getRequestId());

        String message = "Perform OR request: "
                + orRequest.toJson();
        Logger.getLogger(RequesterBolt.class.getName())
                .log(Level.INFO, message);

        try {

            action.setEnforcing();
            tactic.setOngoing(action.getActionOrder());

            tacticDAO.update(tactic);
            actionDAO.update(action);

            HttpResponse<JsonNode> response = Unirest.post(this.host)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + Base64Coder.encodeString("user_TESTING" + ":" + "user_TESTING"))
                    .body(orRequest.toJson())
                    .asJson();

            String responseMessage = response.getBody().toString();
            String logResponse = "OR Response: " + responseMessage;
            Logger.getLogger(RequesterBolt.class.getName())
                    .log(Level.INFO, logResponse);

            this.processResponse(responseMessage, action, tactic);
        } catch (UnirestException ex) {
            // Send Feedback Error
            action.setErrorStatus();
            tactic.setFeedback();

            tacticDAO.update(tactic);
            actionDAO.update(action);
            Logger.getLogger(RequesterBolt.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void processResponse(String json, Action action, Tactic tactic) {

        if (json.contains("orderDate")) {
            // Successfull Append to action if needed
            OrResponse response = GSON.fromJson(json, OrResponse.class);
        } else {
            // Error
            OrResponseError error = GSON.fromJson(json, OrResponseError.class);
            action.setError(error);
            action.setErrorStatus();
            // Set tactic to feedback
            tactic.setFeedback();
            tacticDAO.update(tactic);
            actionDAO.update(action);
        }
    }

}
