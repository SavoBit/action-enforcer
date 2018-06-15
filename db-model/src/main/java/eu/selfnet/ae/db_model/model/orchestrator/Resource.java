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
public class Resource {
    
     private String name;
     private SecondResource resource;
     
    
     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public SecondResource getResource() {
        return resource;
    }

    public void SetResource(SecondResource resource) {
        this.resource = resource;
    }
    
}
