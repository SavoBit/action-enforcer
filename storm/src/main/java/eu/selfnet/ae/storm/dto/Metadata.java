/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;

import java.util.List;

public class Metadata {

    private List<Parameter> parameter;
    private List<Metadata.Location> location;

    public Metadata() {
    }

    public List<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public List<Metadata.Location> getLocation() {
        return location;
    }

    public void setLocation(List<Metadata.Location> location) {
        this.location = location;
    }

    public static class Location {

        public Location() {
        }

        private Metadata.Location.CloseTo closeTo;
        private Metadata.Location.CloseToSource closeToSource;
        private Metadata.Location.CloseToDestination closeToDestination;
        private Metadata.Location.BothEnds bothEnds;
        private Metadata.Location.RelatingTo relatingTo;
        private Parameter parameter;

        public Metadata.Location.CloseTo getCloseTo() {
            return closeTo;
        }

        public void setCloseTo(Metadata.Location.CloseTo closeTo) {
            this.closeTo = closeTo;
        }

        public Metadata.Location.CloseToSource getCloseToSource() {
            return closeToSource;
        }

        public void setCloseToSource(Metadata.Location.CloseToSource closeToSource) {
            this.closeToSource = closeToSource;
        }

        public Metadata.Location.CloseToDestination getCloseToDestination() {
            return closeToDestination;
        }

        public void setCloseToDestination(Metadata.Location.CloseToDestination closeToDestination) {
            this.closeToDestination = closeToDestination;
        }

        public Metadata.Location.BothEnds getBothEnds() {
            return bothEnds;
        }

        public void setBothEnds(Metadata.Location.BothEnds bothEnds) {
            this.bothEnds = bothEnds;
        }

        public Metadata.Location.RelatingTo getRelatingTo() {
            return relatingTo;
        }

        public void setRelatingTo(Metadata.Location.RelatingTo relatingTo) {
            this.relatingTo = relatingTo;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }

        public static class CloseTo {

            private Parameter parameter;
        }

        public static class CloseToSource {

            private Parameter parameter;
        }

        public static class CloseToDestination {

            private Parameter parameter;
        }

        public static class BothEnds {

            private Parameter parameter;
        }

        public static class RelatingTo {

            private Parameter parameter;
        }
    }

}
