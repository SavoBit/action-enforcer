/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.orchestrator;

/**
 *
 * @author Mohammad Alselek
 */
public class Component {
    
    private String name;
    private SecondComponent component;
    
    public Component(){
    
    }
    public Component(String name, SecondComponent component){
    this.name = name;
    this.component = component;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public SecondComponent getComponent() {
        return component;
    }

    public void setComponent(SecondComponent component) {
        this.component = component;
    }
    
    
    
}
