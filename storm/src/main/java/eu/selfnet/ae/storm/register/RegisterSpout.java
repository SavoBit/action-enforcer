/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.register;

import eu.selfnet.ae.rabbitmq.Message;
import eu.selfnet.ae.rabbitmq.RabbitMQClient;
import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

/**
 *
 * Class that uses RabbitMQ as spout
 */
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class RegisterSpout extends BaseRichSpout {

    SpoutOutputCollector _collector;
    private RabbitMQClient client;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("request"));
    }

    @Override
    public void open(Map conf,
            TopologyContext context, SpoutOutputCollector collector) {
        this._collector = collector;
        try {
            _collector = collector;
            this.client = new RabbitMQClient(
                    "Message_Queue_Parameters",
                    "Message_Queue_Exchange",
                    "Message_Queue_Config");
            Logger.getLogger(RegisterSpout.class.getName())
                    .log(Level.INFO, ".:Connected to RabbitMQ:.");
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(RegisterSpout.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nextTuple() {
        // Convert the Message directly to type
        Message message = this.client.nextMessage();
        if (message != null) {
            //String request = "{'id': '2e75a74b-025c-4af7-8df5-7aef06644913', 'action': [{'order': '1', 'actionOption': {'actuator': {'configuration': {'parameter': [{'oid': 'IP', 'name': 'srcIpAddress', 'value': '192.168.12.203'}, {'oid': 'IP', 'name': 'dstIpAddress', 'value': '192.168.12.223'}, {'oid': 'Number', 'name': 'dstPort', 'value': '10850'}, {'oid': 'Text', 'name': 'pattern', 'value': 'botload/tasks.php?uid='}, {'oid': 'Text', 'name': 'alert', 'value': 'A potential zombie/bot is detected'}]}, 'oid': 'SNORT'}, 'id': '9adc990f-e93f-4bf2-aa68-be8d6dec4383', 'operation': 'inspectFlow', 'typeOfOption': 'ADD'}, 'metadata': {'location': [{'closeTo': {'parameter': {'oid': 'IP', 'value': '192.168.12.203'}}}], 'parameter': [{'oid': 'MAPPING', 'name': 'srcIpAddress', 'value': 'zombieIPAddress'}, {'oid': 'MAPPING', 'name': 'dstPort', 'value': 'botnetPort'}, {'oid': 'MAPPING', 'name': 'pattern', 'value': 'botnetUri'}, {'oid': 'MAPPING', 'name': 'dstIpAddress', 'value': 'ccIPAddress'}, {'oid': 'MAPPING', 'name': 'alert', 'value': 'message'}, {'oid': 'MAPPING', 'name': 'inspectFlow', 'value': 'inspectFlows'}]}}, {'order': '2', 'actionOption': {'actuator': {'configuration': {'parameter': [{'oid': 'IP', 'name': 'srcIpAddress', 'value': '192.168.12.203'}, {'oid': 'IP', 'name': 'dstIpAddress', 'value': '192.168.12.223'}, {'oid': 'Number', 'name': 'dstPort', 'value': '10850'}, {'oid': 'Text', 'name': 'pattern', 'value': 'botload/tasks.php?uid='}, {'oid': 'Text', 'name': 'alert', 'value': 'A potential zombie/bot is detected'}]}, 'oid': 'SNORT'}, 'id': '9adc990f-e93f-4bf2-aa68-be8d6dec4383', 'operation': 'inspectFlow', 'typeOfOption': 'ADD'}, 'metadata': {'location': [{'closeTo': {'parameter': {'oid': 'IP', 'value': '192.168.12.203'}}}], 'parameter': [{'oid': 'MAPPING', 'name': 'srcIpAddress', 'value': 'zombieIPAddress'}, {'oid': 'MAPPING', 'name': 'dstPort', 'value': 'botnetPort'}, {'oid': 'MAPPING', 'name': 'pattern', 'value': 'botnetUri'}, {'oid': 'MAPPING', 'name': 'dstIpAddress', 'value': 'ccIPAddress'}, {'oid': 'MAPPING', 'name': 'alert', 'value': 'message'}, {'oid': 'MAPPING', 'name': 'inspectFlow', 'value': 'inspectFlows'}]}}]}";
            Logger.getLogger(RegisterSpout.class.getName())
                    .log(Level.INFO, ".:Received New Message:.{0}", message);
            try { 
                this._collector.emit(new Values(message.getMessage()));
                Logger.getLogger(RegisterSpout.class.getName())
                    .log(Level.INFO, ".:Message SENT:.");

            } catch (Exception ex){
                Logger.getLogger(RegisterSpout.class.getName())
                    .log(Level.INFO, ".:Message NOT SENT:.{0}", ex);
                 
                }
            
        }
    }
    
}
