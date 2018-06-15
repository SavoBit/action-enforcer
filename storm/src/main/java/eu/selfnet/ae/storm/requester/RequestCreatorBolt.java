/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.requester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.selfnet.ae.conf_reader.ConfReader;
import eu.selfnet.ae.db_model.model.orchestrator.Component;
import eu.selfnet.ae.db_model.model.orchestrator.OrRequest;
import eu.selfnet.ae.db_model.model.orchestrator.OrderItem;
import eu.selfnet.ae.db_model.model.orchestrator.Property;
import eu.selfnet.ae.db_model.model.orchestrator.PropertyForComponent;
import eu.selfnet.ae.db_model.model.orchestrator.Resource;
import eu.selfnet.ae.db_model.model.orchestrator.SecondComponent;
import eu.selfnet.ae.db_model.model.orchestrator.SecondResource;
import eu.selfnet.ae.db_model.model.orchestrator.Service;
import eu.selfnet.ae.db_model.model.orchestrator.ServiceCharacteristic;
import eu.selfnet.ae.db_model.model.orchestrator.ServiceSpecification;
import eu.selfnet.ae.db_model.model.tal.Action;
import eu.selfnet.ae.storm.dto.ActionDTO;
//import eu.selfnet.ae.storm.dto.Metadata.Location;
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
        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);
        
        String message = "Create action request "
                + a.getActionOrder()
                + " from tactic "
                + a.getTactic().getRequestId();

        Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO, message);
        
        String app;
        if (dto.getActionOption().getActuator() != null) {
            app = dto.getActionOption().getActuator().getOid();
        } else if (dto.getActionOption().getSensor() != null) {
            app = dto.getActionOption().getSensor().getOid();
        } else {
            String Message
                    = "Invalid parameter, only Sensor and Actuator available, "
                    + "both are null";
            throw new java.lang.NoSuchFieldError(Message);
        }
        
        switch (app){
            case ("FCA"):
                try {
            OrRequest r = this.convertActionToSORequest(a);
            Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO,".:The SO Request is ready:.{0}" , r.toJson());
            this._collector.emit("Or-Request-Creator",
                    new Values(r, a));
        } catch (NoSuchFieldError e) {
            Logger.getLogger(RequestCreatorBolt.class.getName())
                    .log(Level.SEVERE, null, e);
        }
            break;
            case ("DPI"):
            try {
            OrRequest r = this.convertActionToRequest(a);
            Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO,".:The SP Request is ready:.{0}" , r.toJson());
            this._collector.emit("Or-Request-Creator",
                    new Values(r, a));   
        } catch (NoSuchFieldError e) {
            Logger.getLogger(RequestCreatorBolt.class.getName())
                    .log(Level.SEVERE, null, e);
        }
            break;
            case ("BOTNET"):
            try {
            OrRequest r = this.convertActionToRequest(a);
            Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO,".:The SP Request is ready:.{0}" , r.toJson());
            this._collector.emit("Or-Request-Creator",
                    new Values(r, a));   
        } catch (NoSuchFieldError e) {
            Logger.getLogger(RequestCreatorBolt.class.getName())
                    .log(Level.SEVERE, null, e);
        }
            break;
            case ("FW"):
            try {
            OrRequest r = this.convertActionToSHRequest(a);
            Logger.getLogger(RequestCreatorBolt.class.getName())
                .log(Level.INFO,".:The SH Request is ready:.{0}" , r.toJson());
            this._collector.emit("Or-Request-Creator",
                    new Values(r, a));   
        } catch (NoSuchFieldError e) {
            Logger.getLogger(RequestCreatorBolt.class.getName())
                    .log(Level.SEVERE, null, e);
        }
            break;
            
            
        }
        

        

    }

    /**
     * Method to convert the action in ready Orchestration request. Maps every
     * action parameter to OR request objects that will be send to the OR API.
     *
     * @param a - The action to convert
     * @return Orchestrator request object
     */    
    private OrRequest convertActionToRequest(Action a) throws NoSuchFieldError {

        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);

        OrRequest request = new OrRequest();
        
        
        // Set externalID
        String externalID;
        externalID = a.getTactic().getRequestId();
        request.setExternalID(externalID);
        // 
        // Set request category ()
        
        /*
        if (a.getActionContextType().equals("CFS.IM")) {
            request.setCategory("monitoring");
        } else {
            request.setCategory("mobile broadband");
        }
        */
        OrderItem orderItem = new OrderItem();
        orderItem.setId(a.getActionOrder().toString());
        
        String actionName;
        actionName = dto.getActionOption().getOperation();
        List<Parameter> mapping;
        mapping = dto.getMetadata().getParameter();
        mapping.stream().filter((meta) -> (meta.getName().equals(actionName) && meta.getOid().equals("MAPPING"))).forEach((meta) -> {
            
            orderItem.setAction(meta.getValue());
            });
                    
        
        
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


        ServiceSpecification ss = new ServiceSpecification();
        // Currently OSS_E2EO uses the CFS.MI or CFS.IM as service specification
        // id which we map as action context type
        //ss.setId(a.getActionContextType());
        ss.setId("CFS.IM");
        orderItem.setServiceSpecification(ss);

        Service s = new Service();
        s.setCategory("CFS"); // Category hardcoded to CFS
        List<ServiceCharacteristic> parameters = new ArrayList<>();
        // set the location #hardcoded#
        //Location location = new Location();
        if (dto.getMetadata().getLocation().get(0).getCloseToDestination() != null){
         parameters.add(new ServiceCharacteristic("location", "Close to Destination"));
        }
        else if (dto.getMetadata().getLocation().get(0).getCloseToSource() != null) {
         parameters.add(new ServiceCharacteristic("location", "Close to Source"));  
        }
        
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
            throw new java.lang.NoSuchFieldError(Message);
        }
        if ("BOTNET".equals(app)){
            parameters.add(new ServiceCharacteristic("appType", "BOTNET"));
            //parameters.add(new ServiceCharacteristic("zombieId", dto.getActionOption().getId()));
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
                    case "zombieID":
                        parameters.add(
                                new ServiceCharacteristic("zombieId", p.getValue())
                        );  break;
                    case "botnetReal":
                        parameters.add(
                                new ServiceCharacteristic("botnetReal", p.getValue())
                        );  break;
                    case "ServiceID":
                        s.setId( p.getValue());
                            break; 
                    case "dstPort":
                        parameters.add(
                                new ServiceCharacteristic("dstPort", p.getValue())
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
        else if (externalID.contains("SP_LOOP_III")){
            
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
                    case "ServiceID":
                        s.setId( p.getValue());
                            break; 
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
                    case "ServiceID":
                        s.setId( p.getValue());
                            break; 
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
    
    private OrRequest convertActionToSHRequest(Action a) throws NoSuchFieldError {

        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);

        OrRequest request = new OrRequest();
        
        
        // Set externalID
        String externalID;
        externalID = a.getTactic().getRequestId();
        request.setExternalID(externalID);
        request.setCategory("firewall");
        request.setDescription("Firewall Service Order");
        // 
        // Set request category ()
        
        /*
        if (a.getActionContextType().equals("CFS.IM")) {
            request.setCategory("monitoring");
        } else {
            request.setCategory("mobile broadband");
        }
        */
        OrderItem orderItem = new OrderItem();
        orderItem.setId(a.getActionOrder().toString());
        
        String actionName;
        actionName = dto.getActionOption().getOperation();
        orderItem.setAction(actionName);
        
        
        // Set callback
        String host = this.config.getSection("REST_CLIENT").get("host");
        if (!host.endsWith("/")) {
            host += "/";
        }
        String client = host
                + a.getTactic().getRequestId()
                + "/" + a.getActionOrder();
        request.setRequesterCallback(client);


        ServiceSpecification ss = new ServiceSpecification();
        // Currently OSS_E2EO uses the CFS.MI or CFS.IM as service specification
        // id which we map as action context type
        //ss.setId(a.getActionContextType());
        ss.setId("CFS.FW");
        orderItem.setServiceSpecification(ss);

        Service s = new Service();
        s.setCategory("CFS"); // Category hardcoded to CFS
        s.setDescription("Firewall Service");
        List<ServiceCharacteristic> parameters = new ArrayList<>();
        
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
            throw new java.lang.NoSuchFieldError(Message);
        }
        if ("FW".equals(app)){
            // Fill the parameters
            configuration.stream().forEach((Parameter p) -> {

                switch (p.getName()) {
                    case "ServiceID":
                        s.setId( p.getValue());
                            break; 
                    default:
                        break;
                }
            });
        
        } 
        
        s.setServiceCharacteristic(parameters);
        orderItem.setService(s);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        request.setOrderItem(orderItems);

    return request;
    }
    
    
private OrRequest convertActionToSORequest(Action a) throws NoSuchFieldError {

        ActionDTO dto = GSON.fromJson(a.getActionDto(), ActionDTO.class);

        OrRequest request = new OrRequest();
        
        Resource resource = new Resource();
        SecondResource sResource = new SecondResource();
        List<Property> property = new ArrayList<>();
        List<PropertyForComponent> propertyForComponent = new ArrayList<>();
        Component component = new Component();
        List<Component> components = new ArrayList<>();
        SecondComponent sComponent = new SecondComponent();
        
        // Set externalID
        String externalID;
        externalID = a.getTactic().getRequestId();
        request.setExternalID(externalID);
 
        request.setCategory("monitoring");
        request.setDescription("Infrastructure Monitoring Service Order");
        request.setID("TBD");
        
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
        List<Parameter> mapping;
        mapping = dto.getMetadata().getParameter();
        
        mapping.stream().filter((meta) -> (meta.getName().equals(actionName) && meta.getOid().equals("MAPPING"))).forEach((meta) -> {
            
            orderItem.setAction(meta.getValue());
            });
 

        ServiceSpecification ss = new ServiceSpecification();
        // Currently OSS_E2EO uses the CFS.MI or CFS.IM as service specification
        // id which we map as action context type
        //ss.setId(a.getActionContextType());
        ss.setId("CFS.IM");
        orderItem.setServiceSpecification(ss);

        Service s = new Service();
        s.setCategory("CFS"); // Category hardcoded to CFS
        List<ServiceCharacteristic> parameters = new ArrayList<>();
        // set the location #hardcoded#
        //Location location = new Location();
        
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
            throw new java.lang.NoSuchFieldError(Message);
        }
            
        s.setDescription("Infrastrucure Monitoring Service");
        
        parameters.add(new ServiceCharacteristic("appType", "FCA"));
        parameters.add(new ServiceCharacteristic("ipAddress", "10.4.0.9"));
        s.setServiceCharacteristic(parameters);
        
        if ("FCA".equals(app)){
        
//        property.add(new Property("encapsulationLayer", "0"));
//        property.add(new Property("layer3Protocol", "TBD"));
//        property.add(new Property("srcIpAddress", "1.1.1.1"));
//        property.add(new Property("dstIpAddress", "1.1.1.1"));
//        property.add(new Property("layer4Protocol", "00"));
//        property.add(new Property("srcPort", "1111"));
//        property.add(new Property("dstPort", "1111"));
//        property.add(new Property("layer7Protocol", "TBD"));
//        property.add(new Property("completePacketStructure", "TBD"));
        
        
        configuration.stream().forEach((p) -> {

        switch (p.getName()) {
            case "encapsulationLayer":
                property.add(new Property("encapsulationLayer", p.getValue()));
                if (p.getValue() != "0"){
                    component.setName("TBD");
                    sComponent.setId("TBD");
                    sComponent.setType("TBD");
                    propertyForComponent.add(new PropertyForComponent("id","TBD"));
                    propertyForComponent.add(new PropertyForComponent("type","TBD"));
                    sComponent.setProperty(propertyForComponent); 
                    component.setComponent(sComponent);
                } 
                break;
            case "l3Proto":
                property.add(new Property("layer3Protocol", p.getValue()));
                break;
            case "srcIP":
                property.add(new Property("srcIpAddress", p.getValue()));
                break;
            case "dstIP":
                property.add(new Property("dstIpAddress", p.getValue()));
                break;
            case "l4Proto":
                property.add(new Property("layer4Protocol", p.getValue()));
                break;
            case "srcPort":
                property.add(new Property("srcPort", p.getValue()));
                break;
            case "dstPort":
                property.add(new Property("dstPort", p.getValue()));
                break;
            case "l7Proto":
                property.add(new Property("layer7Protocol", p.getValue()));
                break;
            case "completePacketStructure":
                property.add(new Property("completePacketStructure", p.getValue()));
                break;
            case "packetStructure":
                property.add(new Property("packetStructure", p.getValue()));
                break;
            case "srcMacAddress":
                property.add(new Property("srcMacAddress", p.getValue()));
                break;
            case "dstMacAddress":
                property.add(new Property("dstMacAddress", p.getValue()));
                break;
            case "parentResourceId":
                property.add(new Property("parentResourceId", p.getValue()));
                break;
            case "reporterId":
                property.add(new Property("reporterId", p.getValue()));
                break;
            case "resourceAbstractionLayer":
                property.add(new Property("resourceAbstractionLayer", p.getValue()));
                break;
            case "state":
                property.add(new Property("state", p.getValue()));
                break;
            case "ServiceID":
                s.setId(p.getValue());
                break;
            default:
                break;
        }
      
        }); 
            
        }
       
        property.add(new Property("layer3Protocol", "19"));
        property.add(new Property("packetStructure", "/ip4:20/udp:8"));
        property.add(new Property("srcMacAddress", "00:23:22:24:2c:90"));
        property.add(new Property("dstMacAddress", "00:23:24:14:57:cc"));
        property.add(new Property("parentResourceId", "TBD"));
        property.add(new Property("reporterId", "TBD"));
        property.add(new Property("resourceAbstractionLayer", "7"));
        property.add(new Property("state", "TBD"));
       
    configuration.stream().forEach((p) -> {

        

        if (p.getName().contains("l7K")){
            component.setName("layer7Key" + p.getName().substring(p.getName().length() - 1));
            sComponent.setId(p.getName().substring(p.getName().length() - 1));
            sComponent.setType("layer7Key");
            propertyForComponent.add(new PropertyForComponent("id",p.getValue()));
            sComponent.setProperty(propertyForComponent);
            component.setComponent(sComponent);
        }
    });
    
    
        propertyForComponent.add(new PropertyForComponent("id","TBD"));
        propertyForComponent.add(new PropertyForComponent("type","TBD"));

        sComponent.setId("TBD");
        sComponent.setType("TBD");
        sComponent.setProperty(propertyForComponent);

        component.setName("TBD");
        component.setComponent(sComponent);
        components.add(component);


        sResource.setId("TBD");
        sResource.setType("LR.FLOW");
        sResource.setProperty(property);
        sResource.setComponent(components);

        List<Resource> resources;
        resources = new ArrayList<>();
        resource.setName("LR.FLOW");
        resource.SetResource(sResource);
        resources.add(resource);
        s.setResource(resources);
        orderItem.setService(s);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        request.setOrderItem(orderItems);

    return request;
    }
    
}
