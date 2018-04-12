/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.feedback;

import eu.selfnet.ae.db_model.model.tal.Action;
import java.util.ArrayList;
import java.util.List;

public class TacticFeedback {

    private String id;
    private Boolean success;
    private List<ActionFeedback> actions;

    public TacticFeedback() {
    }

    public TacticFeedback(String requestId, List<Action> actions) {
        this.id = requestId;
        this.actions = new ArrayList<>();
        this.success = true;

        actions.stream().map((a) -> new ActionFeedback(a)).map((af) -> {
            this.actions.add(af);
            return af;
        }).filter((af) -> (!af.getSuccess())).forEach((_item) -> {
            this.success = false;
        });

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ActionFeedback> getActions() {
        return actions;
    }

    public void setActions(List<ActionFeedback> actions) {
        this.actions = actions;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
