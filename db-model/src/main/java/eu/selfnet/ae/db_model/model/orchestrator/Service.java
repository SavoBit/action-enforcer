/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.orchestrator;

import java.util.List;

public class Service {

    private String category;
    private String description;
    private String id;
    private List<ServiceCharacteristic> serviceCharacteristic;
    private List<Resource> resource;

    public Service(){
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    
    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public List<ServiceCharacteristic> getServiceCharacteristic() {
        return serviceCharacteristic;
    }
    
    

    public void setServiceCharacteristic(List<ServiceCharacteristic> serviceCharacteristic) {
        this.serviceCharacteristic = serviceCharacteristic;
    }
    
    public List<Resource> getResource() {
        return resource;
    }
    
    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }


}
