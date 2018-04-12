/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;

import eu.selfnet.ae.storm.dto.aggregation.Aggregation;
import java.util.GregorianCalendar;
import java.util.List;

public class PurposeOfAction {

    private List<Aggregation> aggregation;
    private List<AnalysisRule> analysisRule;
    private List<Threshold> threshold;
    private Metadata metadata;
    private String boolOp;
    private String oid;
    private String description;
    private GregorianCalendar feedbackPeriod;

    public PurposeOfAction() {
    }

    public List<Aggregation> getAggregation() {
        return aggregation;
    }

    public void setAggregation(List<Aggregation> aggregation) {
        this.aggregation = aggregation;
    }

    public List<AnalysisRule> getAnalysisRule() {
        return analysisRule;
    }

    public void setAnalysisRule(List<AnalysisRule> analysisRule) {
        this.analysisRule = analysisRule;
    }

    public List<Threshold> getThreshold() {
        return threshold;
    }

    public void setThreshold(List<Threshold> threshold) {
        this.threshold = threshold;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getBoolOp() {
        return boolOp;
    }

    public void setBoolOp(String boolOp) {
        this.boolOp = boolOp;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getFeedbackPeriod() {
        return feedbackPeriod;
    }

    public void setFeedbackPeriod(GregorianCalendar feedbackPeriod) {
        this.feedbackPeriod = feedbackPeriod;
    }

    
    
}
