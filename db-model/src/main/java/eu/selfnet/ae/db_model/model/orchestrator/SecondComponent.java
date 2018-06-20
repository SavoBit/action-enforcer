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
public class SecondComponent {
    
    private String id;
    private String type;
    private List<PropertyForComponent> property;
    
    public SecondComponent(){
    
    }
    public SecondComponent(String id, String type, List<PropertyForComponent> property){
    this.id = id;
    this.type = type;
    this.property = property;
    }
    
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
    
    public List<PropertyForComponent> getProperty() {
        return property;
    }
    
    public void setProperty(List<PropertyForComponent> propertyForComponent) {
        this.property = propertyForComponent;
    }
    
    
}
