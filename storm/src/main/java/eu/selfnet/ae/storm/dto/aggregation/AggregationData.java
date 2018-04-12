/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation;

import eu.selfnet.ae.storm.dto.Metadata;
import eu.selfnet.ae.storm.dto.Sensor;
import java.util.List;

public class AggregationData {
    private List<Sensor> sensor;
    private Metadata metadata;

    public AggregationData() {
    }

    public List<Sensor> getSensor() {
        return sensor;
    }

    public void setSensor(List<Sensor> sensor) {
        this.sensor = sensor;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    
    
}
