/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;


public class ActionDTO {

    private ActionOption actionOption;
    private Metadata metadata;
    private PurposeOfAction purposeOfAction;
    private String order;
    private String id;

    public ActionDTO() {
    }

    public ActionOption getActionOption() {
        return actionOption;
    }

    public void setActionOption(ActionOption actionOption) {
        this.actionOption = actionOption;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public PurposeOfAction getPurposeOfAction() {
        return purposeOfAction;
    }

    public void setPurposeOfAction(PurposeOfAction purposeOfAction) {
        this.purposeOfAction = purposeOfAction;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}
