/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.register;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import eu.selfnet.ae.db_model.dao.ActionDAO;
import eu.selfnet.ae.db_model.dao.TacticDAO;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.db_model.model.tal.ActionTactic;
import eu.selfnet.ae.db_model.model.tal.Tactic;
import eu.selfnet.ae.storm.dto.ActionDTO;
import eu.selfnet.ae.storm.dto.TacticDTO;
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
 *
 * Class that is capable to determine the TAL request type. It's expected that
 * every TAL request has the Service Context ID, this bolt is capable of solving
 * the corresponding Service Context Type per action.
 */
public class ContextResolutionBolt extends BaseRichBolt {

    private static final Gson GSON = new GsonBuilder().create();
    private TacticDAO tacticDAO;
    private ActionDAO actionDAO;
    private OutputCollector _collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(
                "Action-Resolver", new Fields("request")
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
        String message = input.getStringByField("request");
        
        if (message != null) {
            //String request = "{'id': '2e75a74b-025c-4af7-8df5-7aef06644913', 'action': [{'order': '1', 'actionOption': {'actuator': {'configuration': {'parameter': [{'oid': 'IP', 'name': 'srcIpAddress', 'value': '192.168.12.203'}, {'oid': 'IP', 'name': 'dstIpAddress', 'value': '192.168.12.223'}, {'oid': 'Number', 'name': 'dstPort', 'value': '10850'}, {'oid': 'Text', 'name': 'pattern', 'value': 'botload/tasks.php?uid='}, {'oid': 'Text', 'name': 'alert', 'value': 'A potential zombie/bot is detected'}]}, 'oid': 'SNORT'}, 'id': '9adc990f-e93f-4bf2-aa68-be8d6dec4383', 'operation': 'inspectFlow', 'typeOfOption': 'ADD'}, 'metadata': {'location': [{'closeTo': {'parameter': {'oid': 'IP', 'value': '192.168.12.203'}}}], 'parameter': [{'oid': 'MAPPING', 'name': 'srcIpAddress', 'value': 'zombieIPAddress'}, {'oid': 'MAPPING', 'name': 'dstPort', 'value': 'botnetPort'}, {'oid': 'MAPPING', 'name': 'pattern', 'value': 'botnetUri'}, {'oid': 'MAPPING', 'name': 'dstIpAddress', 'value': 'ccIPAddress'}, {'oid': 'MAPPING', 'name': 'alert', 'value': 'message'}, {'oid': 'MAPPING', 'name': 'inspectFlow', 'value': 'inspectFlows'}]}}, {'order': '2', 'actionOption': {'actuator': {'configuration': {'parameter': [{'oid': 'IP', 'name': 'srcIpAddress', 'value': '192.168.12.203'}, {'oid': 'IP', 'name': 'dstIpAddress', 'value': '192.168.12.223'}, {'oid': 'Number', 'name': 'dstPort', 'value': '10850'}, {'oid': 'Text', 'name': 'pattern', 'value': 'botload/tasks.php?uid='}, {'oid': 'Text', 'name': 'alert', 'value': 'A potential zombie/bot is detected'}]}, 'oid': 'SNORT'}, 'id': '9adc990f-e93f-4bf2-aa68-be8d6dec4383', 'operation': 'inspectFlow', 'typeOfOption': 'ADD'}, 'metadata': {'location': [{'closeTo': {'parameter': {'oid': 'IP', 'value': '192.168.12.203'}}}], 'parameter': [{'oid': 'MAPPING', 'name': 'srcIpAddress', 'value': 'zombieIPAddress'}, {'oid': 'MAPPING', 'name': 'dstPort', 'value': 'botnetPort'}, {'oid': 'MAPPING', 'name': 'pattern', 'value': 'botnetUri'}, {'oid': 'MAPPING', 'name': 'dstIpAddress', 'value': 'ccIPAddress'}, {'oid': 'MAPPING', 'name': 'alert', 'value': 'message'}, {'oid': 'MAPPING', 'name': 'inspectFlow', 'value': 'inspectFlows'}]}}]}";
            Logger.getLogger(ContextResolutionBolt.class.getName())
                    .log(Level.INFO, ".:New Message in BOLT:.{0}", message);
            try { 
//                String builder = "Incoming action resolution:" + message;
//                Logger.getLogger(ContextResolutionBolt.class.getName()).log(Level.INFO, builder);

                // Create the tactic DTO object
                TacticDTO tDto = GSON.fromJson(message, TacticDTO.class);

                // TODO: When SI API Available:
                // Query Service Instance to get Service Context Type
                // Persist tactic
                Tactic t = this.convertTacticDTO(tDto);
                this.tacticDAO.persist(t);

                // Persist Actions and emmit them to the next bolt
                tDto.getAction().stream().map((dto) -> convertActionDTO(t, dto))
                        .map((a) -> {
                            actionDAO.persist(a);
                            return a;
                        }).forEach((a) -> {
                    this.emit(this._collector, a);
                });
        
                Logger.getLogger(ContextResolutionBolt.class.getName())
                    .log(Level.INFO, ".:Message SENT:.");

            } catch (JsonSyntaxException ex){
                Logger.getLogger(ContextResolutionBolt.class.getName())
                    .log(Level.INFO, ".:Message NOT SENT:.{0}", ex);
                 
                }
            
        }
//        Logger.getLogger(ContextResolutionBolt.class.getName())
//                    .log(Level.INFO, ".:Message NOT SENT:.{0}", message);
    }

    public void emit(OutputCollector collector, Action a) {
        collector.emit("Action-Resolver", new Values(a));
    }

    /**
     * Converts the request tactic in a AE's tactic This is a simple object
     * where only the essential information is kept.
     *
     * @param dto - The object sent in the request
     * @return A Tactic object
     */
    private Tactic convertTacticDTO(TacticDTO dto) {
        Tactic t = new Tactic();
        t.setActions(dto.getAction().size());
        t.setContextType("Incoming Request");  // To be solved
        t.setRequestId(dto.getId());
        t.setSolving();
        return t;
    }

    /**
     * Converts the request action in a AE's action This is a complex object
     * where the essential information is organized.
     *
     * @param t - The original tactic to be appended on the action
     * @param dto - The object sent in the request
     * @return An Action object
     */
    private Action convertActionDTO(Tactic t, ActionDTO dto) {
        Action a = new Action();
        a.setInternalId(dto.getActionOption().getId());

        a.setActionContextID(dto.getActionOption().getServiceContext());
        a.setActionContextID("");
        a.setActionOrder(Integer.valueOf(dto.getOrder()));

        // Append tactic information
        ActionTactic at = new ActionTactic();
        at.setActions(t.getActions());
        at.setContextId(t.getContextId());
        at.setContextType(t.getContextType());
        at.setRequestId(t.getRequestId());
        a.setTactic(at);

        // Keep the original request data
        a.setActionDto(GSON.toJson(dto, ActionDTO.class));

        // Initial tactic status where not all SCID and SCTypes are resolved
        a.setResolving();
        return a;
    }

}
