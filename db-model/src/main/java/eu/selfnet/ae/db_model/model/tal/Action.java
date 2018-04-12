/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.tal;

import eu.selfnet.ae.db_model.model.orchestrator.OrResponseError;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "action")
public class Action implements Serializable {

    public static String READY = "READY";
    public static String RESOLVING = "RESOLVING";
    public static String ENFORCING = "ENFORCING";
    public static String ERROR = "ERROR";
    public static String ENFORCED = "ENFORCED";
    public static String NOT_ENFORCED = "NOT_ENFORCED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "objectid")
    private String id;
    
    @Column()
    private String internalId;

    @Column()
    private String actionContextType;
    @Column()
    private String actionContextID;
    @Column(nullable = false)
    private Integer actionOrder;

    @Column(nullable = false)
    private String actionDto;

    @Column(nullable = false)
    private String status;

    @Embedded
    @Column(nullable = false)
    private ActionTactic tactic;

    @Embedded
    @Column()
    private OrResponseError error;

    public Action() {
    }

    public void setReady() {
        this.setStatus(Action.READY);
    }

    public void setResolving() {
        this.setStatus(Action.RESOLVING);
    }

    public void setEnforcing() {
        this.setStatus(Action.ENFORCING);
    }
    
    public void setErrorStatus(){
        this.setStatus(Action.ERROR);
    }

    /* Getters and setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionContextType() {
        return actionContextType;
    }

    public void setActionContextType(String actionContextType) {
        this.actionContextType = actionContextType;
    }

    public String getActionContextID() {
        return actionContextID;
    }

    public void setActionContextID(String actionContextID) {
        this.actionContextID = actionContextID;
    }

    public ActionTactic getTactic() {
        return tactic;
    }

    public void setTactic(ActionTactic tactic) {
        this.tactic = tactic;
    }

    public Integer getActionOrder() {
        return actionOrder;
    }

    public void setActionOrder(Integer actionOrder) {
        this.actionOrder = actionOrder;
    }

    public String getActionDto() {
        return actionDto;
    }

    public void setActionDto(String actionDto) {
        this.actionDto = actionDto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrResponseError getError() {
        return error;
    }

    public void setError(OrResponseError error) {
        this.error = error;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

}
