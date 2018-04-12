/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation.items;

import java.util.GregorianCalendar;


public class Period {

    private GregorianCalendar interval;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime ;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private GregorianCalendar duration;

    public Period() {
    }

    public GregorianCalendar getInterval() {
        return interval;
    }

    public void setInterval(GregorianCalendar interval) {
        this.interval = interval;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar startTime) {
        this.startTime = startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar endTime) {
        this.endTime = endTime;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public GregorianCalendar getDuration() {
        return duration;
    }

    public void setDuration(GregorianCalendar duration) {
        this.duration = duration;
    }
    
    
}
