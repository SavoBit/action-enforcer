/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.orchestrator;

import com.google.gson.Gson;
import java.util.List;

public class OrRequest {

    private String externalId;
    private String description;
    private String category;
    private String requesterCallback;
    private String id;
    private List<OrderItem> orderItem;

    public OrRequest() {
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getExternalID() {
        return externalId;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }
    
    public String getID() {
        return id;
    }

    public void setExternalID(String externalId) {
        this.externalId = externalId;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setID(String id) {
        this.id = id;
    }

    public String getRequesterCallback() {
        return requesterCallback;
    }

    public void setRequesterCallback(String requesterCallback) {
        this.requesterCallback = requesterCallback;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

}
