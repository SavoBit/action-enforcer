/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation;

import eu.selfnet.ae.storm.dto.Threshold;
import java.util.List;


public class AggregationItem {
    private AggregationData aggregationData;
    private AggregationRule aggregationRule;
    private List<Threshold> threshold;
    private String oid;

    public AggregationItem() {
    }

    public AggregationData getAggregationData() {
        return aggregationData;
    }

    public void setAggregationData(AggregationData aggregationData) {
        this.aggregationData = aggregationData;
    }

    public AggregationRule getAggregationRule() {
        return aggregationRule;
    }

    public void setAggregationRule(AggregationRule aggregationRule) {
        this.aggregationRule = aggregationRule;
    }

    public List<Threshold> getThreshold() {
        return threshold;
    }

    public void setThreshold(List<Threshold> threshold) {
        this.threshold = threshold;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }


}
