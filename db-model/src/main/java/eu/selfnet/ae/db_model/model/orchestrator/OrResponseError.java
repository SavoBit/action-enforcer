/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.model.orchestrator;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class OrResponseError implements Serializable {

    @Transient
    private List<OrResponseDetails> details;

    @Column()
    private String description;

    @Column()
    private String message;

    @Column()
    private String code;

    public OrResponseError() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OrResponseDetails> getDetails() {
        return details;
    }

    public void setDetails(List<OrResponseDetails> details) {
        this.details = details;
    }
}
