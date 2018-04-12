/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;

import eu.selfnet.ae.storm.dto.occurences.Occurrences;


public class Threshold {
    private Level level;
    private Occurrences occurrences;
    private String comparison;
    private String oid;
    private String logical;

    public Threshold() {
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Occurrences getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Occurrences occurrences) {
        this.occurrences = occurrences;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getLogical() {
        return logical;
    }

    public void setLogical(String logical) {
        this.logical = logical;
    }
    
    
}
