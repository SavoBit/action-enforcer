/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.requester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.selfnet.ae.conf_reader.ConfReader;
import eu.selfnet.ae.db_model.model.orchestrator.OrRequest;
import eu.selfnet.ae.db_model.model.orchestrator.OrderItem;
import eu.selfnet.ae.db_model.model.orchestrator.Service;
import eu.selfnet.ae.db_model.model.orchestrator.ServiceCharacteristic;
import eu.selfnet.ae.db_model.model.orchestrator.ServiceSpecification;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.storm.dto.ActionDTO;
import eu.selfnet.ae.storm.dto.Parameter;
import java.util.ArrayList;
import java.util.List;
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

public class RequestCreatorBolt extends BaseRichBolt {

    private OutputCollector _collector;
    private ConfReader config;
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(
                "Or-Request-Creator", new Fields("action-request", "action-to-os")
        );
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        this._collector = collector;
        this.config = new ConfReader();
    }

    @Override
    public void execute(Tuple input) {

        Action a = (Action) input.getValueByField("action");

        String message = "Create action request "
                + a.getActionOrder()
                + " from tactic "
                + a.getTactic().getRequestId();

        Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO, message);

        try {
            OrRequest r = this.convertActionToRequest(a);
            this._collector.emit("Or-Request-Creator",
                    new Values(r, a));
        } catch (NoSuchFieldError e) {
            Logger.getLogger(RequestCreatorBolt.class.getName())
                    .log(Level.SEVERE, null, e);
        }

    }

    /**
     * Method to convert the action in ready Orchestration request. Maps every
     * action parameter to OR request objects that will be send to the OR API.
     *
     * @param a - The action to convert
     * @return Orchestrator request object
     */
//    private OrRequest convertActionToRequest(Action a) throws NoSuchFieldError {
//
//        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);
//
//        OrRequest request = new OrRequest();
//
//        // Set request category (how?)
//        if (a.getActionContextType().equals("CFS.IM")) {
//            request.setCategory("monitoring");
//        } else {
//            request.setCategory("mobile broadband");
//        }
//
//        // Set callback
//        String host = this.config.getSection("REST_CLIENT").get("host");
//        if (!host.endsWith("/")) {
//            host += "/";
//        }
//        String client = host
//                + a.getTactic().getRequestId()
//                + "/" + a.getActionOrder();
//        request.setRequesterCallback(client);
//
//        // Create the order item
//        OrderItem orderItem = new OrderItem();
//        orderItem.setId(a.getActionOrder().toString());
//
//        // Currently mapping to operation Kostas used typeofoption
//        orderItem.setAction(dto.getActionOption().getOperation());
//
//        ServiceSpecification ss = new ServiceSpecification();
//        // Currently OSS_E2EO uses the CFS.MI or CFS.IM as service specification
//        // id which we map as action context type
//        ss.setId(a.getActionContextType());
//        orderItem.setServiceSpecification(ss);
//
//        Service s = new Service();
//        s.setCategory("CFS"); // Category hardcoded to CFS
//        List<ServiceCharacteristic> parameters = new ArrayList<>();
//
//        // Get the APP type
//        String app;
//        List<Parameter> configuration;
//        if (dto.getActionOption().getActuator() != null) {
//            app = dto.getActionOption().getActuator().getOid();
//            configuration = dto.getActionOption().getActuator()
//                    .getConfiguration().getParameter();
//        } else if (dto.getActionOption().getSensor() != null) {
//            app = dto.getActionOption().getSensor().getOid();
//            configuration = dto.getActionOption().getSensor()
//                    .getConfiguration().getParameter();
//        } else {
//            String Message
//                    = "Invalid parameter, only Sensor and Actuator available, "
//                    + "both are null";
//            throw new java.lang.NoSuchFieldError();
//        }
//        parameters.add(new ServiceCharacteristic("appType", app));
//
//        /*
//        // Get Request Information mapping names on the Metadata
//        configuration.stream().forEach((p) -> {
//        dto.getMetadata().getParameter().stream()
//        .filter((meta) -> (meta.getName().equals(p.getName())
//        && meta.getOid().equals("MAPPING")))
//        .forEach((meta) -> {
//        parameters.add(
//        new ServiceCharacteristic(
//        meta.getValue(), p.getValue())
//        );
//        });
//        });
//         */
//        // Fill the parameters
//        configuration.stream().forEach((p) -> {
//
//            if (!p.getName().equals("alert") && !p.getName().equals("pattern")) {
//
//                parameters.add(
//                        new ServiceCharacteristic(p.getName(), p.getValue())
//                );
//            }
//        });
//
//        s.setServiceCharacteristic(parameters);
//        orderItem.setService(s);
//        List<OrderItem> orderItems = new ArrayList<>();
//        orderItems.add(orderItem);
//        request.setOrderItem(orderItems);
//
//        return request;
//    }
    
    private OrRequest convertActionToRequest(Action a) throws NoSuchFieldError {

        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);

        OrRequest request = new OrRequest();
        
        // 
        // Set request category ()
        
        /*
        if (a.getActionContextType().equals("CFS.IM")) {
            request.setCategory("monitoring");
        } else {
            request.setCategory("mobile broadband");
        }
        */
        
        if (dto.getActionOption().getOperation().contains("Flow")) {
            request.setCategory("monitoring");
            request.setDescription("Infrastructure Monitoring Service Order");
        } else {
            request.setCategory("mobile broadband");
            request.setDescription("Mobile Internet Service Order");
        }
        
        // Set callback
        String host = this.config.getSection("REST_CLIENT").get("host");
        if (!host.endsWith("/")) {
            host += "/";
        }
        String client = host
                + a.getTactic().getRequestId()
                + "/" + a.getActionOrder();
        request.setRequesterCallback(client);

        // Create the order item
        OrderItem orderItem = new OrderItem();
        orderItem.setId(a.getActionOrder().toString());

        // Currently mapping to operation Kostas used typeofoption
        String actionName;
        actionName = dto.getActionOption().getOperation();
        if (actionName.contains("Flow"))
            orderItem.setAction("inspectFlows");
        else orderItem.setAction("deployBotnet");

        ServiceSpecification ss = new ServiceSpecification();
        // Currently OSS_E2EO uses the CFS.MI or CFS.IM as service specification
        // id which we map as action context type
        ss.setId(a.getActionContextType());
        orderItem.setServiceSpecification(ss);

        Service s = new Service();
        s.setCategory("CFS"); // Category hardcoded to CFS
        List<ServiceCharacteristic> parameters = new ArrayList<>();
        // set the location #hardcoded#
        parameters.add(new ServiceCharacteristic("location", "Close to Destination"));
        
        // Get the APP type
        String app;
        List<Parameter> configuration;
        if (dto.getActionOption().getActuator() != null) {
            app = dto.getActionOption().getActuator().getOid();
            configuration = dto.getActionOption().getActuator()
                    .getConfiguration().getParameter();
        } else if (dto.getActionOption().getSensor() != null) {
            app = dto.getActionOption().getSensor().getOid();
            configuration = dto.getActionOption().getSensor()
                    .getConfiguration().getParameter();
        } else {
            String Message
                    = "Invalid parameter, only Sensor and Actuator available, "
                    + "both are null";
            throw new java.lang.NoSuchFieldError();
        }
        if ("HNET".equals(app)){
            parameters.add(new ServiceCharacteristic("appType", "BOTNET"));
            parameters.add(new ServiceCharacteristic("zombieId", dto.getActionOption().getId()));
            // Fill the parameters
            configuration.stream().forEach((p) -> {

                switch (p.getName()) {
                    case "zombieIP":
                        parameters.add(
                                new ServiceCharacteristic("srcIpAddress", p.getValue())
                        );  break;
                    case "ccServer":
                        parameters.add(
                                new ServiceCharacteristic("dstIpAddress", p.getValue())
                        );  break;
                    case "typeOfBotnet":
                        parameters.add(
                                new ServiceCharacteristic("botnetType", p.getValue())
                        );  break;
                    case "botnetReal":
                        parameters.add(
                                new ServiceCharacteristic("botnetReal", p.getValue())
                        );  break;
                    case "frequency":
                        parameters.add(
                                new ServiceCharacteristic("frequency", p.getValue())
                        );  break;
                    default:
                        break;
                }
            });
        
        }    
        else {
            parameters.add(new ServiceCharacteristic("appType", "DPI"));
            // Fill the parameters
            configuration.stream().forEach((p) -> {

                switch (p.getName()) {
                    case "srcIpAddress":
                        parameters.add(
                                new ServiceCharacteristic("srcIpAddress", p.getValue())
                        );  break;
                    case "dstIpAddress":
                        parameters.add(
                                new ServiceCharacteristic("dstIpAddress", p.getValue())
                        );  break;
                    case "dstPort":
                        parameters.add(
                                new ServiceCharacteristic("dstPort", p.getValue())
                        );  break;
                    case "pattern":
                        parameters.add(
                                new ServiceCharacteristic("dstUri", p.getValue())
                        );  break;
                    case "alert":
                        parameters.add(
                                new ServiceCharacteristic("alert", p.getValue())
                        );  break;
                    default:
                        break;
                }
            });
        }
        
        
        // Get Request Information mapping names on the Metadata
//        configuration.stream().forEach((p) -> {
//        dto.getMetadata().getParameter().stream()
//        .filter((meta) -> (meta.getName().equals(p.getName()) && !(p.getName().equals("alert"))
//        && meta.getOid().equals("MAPPING")))
//        .forEach((meta) -> {
//        parameters.add(
//        new ServiceCharacteristic(
//        meta.getValue(), p.getValue())
//        );
//        });
//        });
        
        
        
        
         
        
        s.setServiceCharacteristic(parameters);
        orderItem.setService(s);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        request.setOrderItem(orderItems);

        return request;
    }
}
