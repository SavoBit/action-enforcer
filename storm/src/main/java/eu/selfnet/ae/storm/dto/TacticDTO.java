/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;

import java.util.List;


public class TacticDTO {
    
    private List<ActionDTO> action;
    private String description;
    private String priority;
    private String id;

    public TacticDTO() {
    }

    public List<ActionDTO> getAction() {
        return action;
    }

    public void setAction(List<ActionDTO> action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
}
