/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation;

import java.util.List;


public class Aggregation {
    private List<AggregationItem> aggregationItem;

    public Aggregation() {
    }

    public List<AggregationItem> getAggregationItem() {
        return aggregationItem;
    }

    public void setAggregationItem(List<AggregationItem> aggregationItem) {
        this.aggregationItem = aggregationItem;
    }
    
}
