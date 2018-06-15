/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.orchestrator;

import java.util.List;

/**
 *
 * @author Mohammad Alselek
 */
public class SecondResource {
    
    private String id;
    private String type;
    private List<Property> property;
    private List<Component> component;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public List<Property> getProperty() {
        return property;
    }
    
    public void setProperty(List<Property> property) {
        this.property = property;
    }
    
    public List<Component> getComponent() {
        return component;
    }
    
    public void setComponent(List<Component> component) {
        this.component = component;
    }
    
    
    
}
