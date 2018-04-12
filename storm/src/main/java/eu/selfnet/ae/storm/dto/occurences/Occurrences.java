/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.occurences;

public class Occurrences {
    private ConsecutiveOccurrences consecutiveOccurrences;
    private TemporalOccurrence temporalOccurrence;
    private SamplesOccurrence samplesOccurrence;
    private Object uniqueOccurrence;

    public Occurrences() {
    }

    public ConsecutiveOccurrences getConsecutiveOccurrences() {
        return consecutiveOccurrences;
    }

    public void setConsecutiveOccurrences(ConsecutiveOccurrences consecutiveOccurrences) {
        this.consecutiveOccurrences = consecutiveOccurrences;
    }

    public TemporalOccurrence getTemporalOccurrence() {
        return temporalOccurrence;
    }

    public void setTemporalOccurrence(TemporalOccurrence temporalOccurrence) {
        this.temporalOccurrence = temporalOccurrence;
    }

    public SamplesOccurrence getSamplesOccurrence() {
        return samplesOccurrence;
    }

    public void setSamplesOccurrence(SamplesOccurrence samplesOccurrence) {
        this.samplesOccurrence = samplesOccurrence;
    }

    public Object getUniqueOccurrence() {
        return uniqueOccurrence;
    }

    public void setUniqueOccurrence(Object uniqueOccurrence) {
        this.uniqueOccurrence = uniqueOccurrence;
    }
    
    
}
