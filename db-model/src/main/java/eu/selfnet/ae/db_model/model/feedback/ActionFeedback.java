/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.feedback;

import eu.selfnet.ae.db_model.model.tal.Action;

public final class ActionFeedback {

    private String id;
    private String order;
    private Boolean success;
    private String detail;

    public ActionFeedback() {

    }

    public ActionFeedback(Action a) {
        this.setId(a.getInternalId());
        this.setOrder(a.getActionOrder().toString());
        this.setSuccess(a);
        this.setDetail(a);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Action a) {
        this.success = a.getStatus().equals(Action.ENFORCED);
    }

    public String getDetail() {
        return detail;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setDetail(Action a) {
        if (a.getStatus().equals("") || a.getStatus().equals(Action.READY)) {
            this.detail = Action.NOT_ENFORCED;
        } else if (a.getStatus().equals(Action.ERROR)) {
            this.detail = Action.ERROR;
        }
    }

}
