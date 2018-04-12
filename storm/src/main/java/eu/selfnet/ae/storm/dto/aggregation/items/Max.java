/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation.items;

import eu.selfnet.ae.storm.dto.Output;
import eu.selfnet.ae.storm.dto.Parameter;
import java.util.List;


public class Max {

    private List<Parameter> parameter;
    private List<Period> period;
    private List<Output> output;

    public Max() {
    }

    public List<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public List<Period> getPeriod() {
        return period;
    }

    public void setPeriod(List<Period> period) {
        this.period = period;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }
    
    
}
