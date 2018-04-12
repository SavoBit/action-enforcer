/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.feedback;

import com.google.gson.Gson;
import eu.selfnet.ae.conf_reader.ConfReader;
import eu.selfnet.ae.db_model.dao.ActionDAO;
import eu.selfnet.ae.db_model.dao.TacticDAO;
import eu.selfnet.ae.db_model.model.feedback.TacticFeedback;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import eu.selfnet.ae.rabbitmq.RabbitMQProducer;
import eu.selfnet.ae.storm.requester.RequesterBolt;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class ReportCompilerBolt extends BaseRichBolt {

    private ActionDAO actionDAO;
    private TacticDAO tacticDAO;
    private OutputCollector _collector;
    private Map<String, String> keyMap;
    private RabbitMQProducer producer;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {

        this._collector = collector;
        this.actionDAO = new ActionDAO();
        this.tacticDAO = new TacticDAO();
        this._collector = collector;
        ConfReader config = new ConfReader();
        this.keyMap = config.getSection("Feedback_Routing_Keys");
        try {
            this.producer = new RabbitMQProducer(
                    "Message_Queue_Parameters",
                    "Feedback_Message_Queue_Exchange",
                    "Feedback_Message_Queue_Config",
                    "Feedback_Routing_Keys");
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(ReportCompilerBolt.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void execute(Tuple input) {
        String requestId = (String) input.getValueByField("feedback-tactic-id");

        List<Action> actions = actionDAO.findByTactic(requestId);
        Tactic t = tacticDAO.findByRequestID(requestId);
        TacticFeedback feedback = new TacticFeedback(requestId, actions);

        try {
            // Emit
            String feedbackMessage = new Gson().toJson(feedback);
            
            String logMessage = "Report Compiler: " + feedbackMessage;
            Logger.getLogger(ReportCompilerBolt.class.getName())
                    .log(Level.INFO, logMessage);

            this.producer.sendMessage(
                    feedbackMessage,
                    this.keyMap.get("feedback_key_a")
            );
            // Delete Tactic and Actions
            tacticDAO.remove(t);
            actionDAO.remove(actions);
        } catch (IOException ex) {
            Logger.getLogger(ReportCompilerBolt.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

}
