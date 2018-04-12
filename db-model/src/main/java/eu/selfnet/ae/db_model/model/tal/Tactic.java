/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.tal;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "tactic")
public class Tactic implements Serializable {

    public static String READY_TO_ENFORCE = "READY";
    public static String SOLVING = "SOLVING";
    public static String ONGOING = "ONGOING";
    public static String FEEDBACK = "FEEDBACK";
    public static String REPORTING = "REPORTING";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "objectid")
    private String id;

    @Column()
    private String requestId;

    @Column()
    private String contextId;

    @Column()
    private String contextType;

    @Column()
    private Integer actions;

    @Column()
    private Integer ongoingAction = 1;

    @Column()
    private String status;

    public Tactic() {
    }

    public void setReadyToEnforce() {
        this.setStatus(Tactic.READY_TO_ENFORCE);
    }

    public void setSolving() {
        this.setStatus(Tactic.SOLVING);
    }

    public void setOngoing() {
        this.setStatus(Tactic.ONGOING);
    }

    public void setOngoing(Integer actionOrder) {
        String message = Tactic.ONGOING + ": " + actionOrder;
        this.setStatus(message);
    }

    public void setFeedback() {
        this.setStatus(Tactic.FEEDBACK);
    }

    /* Getters and Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public Integer getActions() {
        return actions;
    }

    public void setActions(Integer actions) {
        this.actions = actions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public Integer getOngoingAction() {
        return ongoingAction;
    }

    public void setOngoingAction(Integer ongoingAction) {
        this.ongoingAction = ongoingAction;
    }

}
