/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;


public class ActionOption {
    private Sensor sensor;
    private Actuator actuator;
    private ResourceAction resourceAction;
    private String operation;
    private String description;
    private String typeOfOption;
    private String serviceContext;
    private String id;
    private String applied;

    public ActionOption() {
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public ResourceAction getResourceAction() {
        return resourceAction;
    }

    public void setResourceAction(ResourceAction resourceAction) {
        this.resourceAction = resourceAction;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfOption() {
        return typeOfOption;
    }

    public void setTypeOfOption(String typeOfOption) {
        this.typeOfOption = typeOfOption;
    }

    public String getServiceContext() {
        return serviceContext;
    }

    public void setServiceContext(String serviceContext) {
        this.serviceContext = serviceContext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }

    
    
}
