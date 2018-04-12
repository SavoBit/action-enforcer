/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto.aggregation;

import eu.selfnet.ae.storm.dto.Metadata;
import eu.selfnet.ae.storm.dto.Output;
import eu.selfnet.ae.storm.dto.Parameter;
import eu.selfnet.ae.storm.dto.Plugin;
import eu.selfnet.ae.storm.dto.aggregation.items.Accumulate;
import eu.selfnet.ae.storm.dto.aggregation.items.Add;
import eu.selfnet.ae.storm.dto.aggregation.items.Average;
import eu.selfnet.ae.storm.dto.aggregation.items.Ceil;
import eu.selfnet.ae.storm.dto.aggregation.items.Count;
import eu.selfnet.ae.storm.dto.aggregation.items.Div;
import eu.selfnet.ae.storm.dto.aggregation.items.Exp;
import eu.selfnet.ae.storm.dto.aggregation.items.First;
import eu.selfnet.ae.storm.dto.aggregation.items.Floor;
import eu.selfnet.ae.storm.dto.aggregation.items.Last;
import eu.selfnet.ae.storm.dto.aggregation.items.Ln;
import eu.selfnet.ae.storm.dto.aggregation.items.Log;
import eu.selfnet.ae.storm.dto.aggregation.items.Max;
import eu.selfnet.ae.storm.dto.aggregation.items.Min;
import eu.selfnet.ae.storm.dto.aggregation.items.Mul;
import eu.selfnet.ae.storm.dto.aggregation.items.Pow;
import eu.selfnet.ae.storm.dto.aggregation.items.Round;
import eu.selfnet.ae.storm.dto.aggregation.items.Sample;
import eu.selfnet.ae.storm.dto.aggregation.items.Single;
import eu.selfnet.ae.storm.dto.aggregation.items.SlidingWindow;
import eu.selfnet.ae.storm.dto.aggregation.items.Sqrt;
import eu.selfnet.ae.storm.dto.aggregation.items.Stdev;
import eu.selfnet.ae.storm.dto.aggregation.items.Sub;
import eu.selfnet.ae.storm.dto.aggregation.items.Sum;
import eu.selfnet.ae.storm.dto.aggregation.items.Unique;
import eu.selfnet.ae.storm.dto.aggregation.items.Variance;
import java.util.List;

public class AggregationRule {

    private List<AggregationRule.RuleItem> ruleItem;
    private List<AggregationRule.Metric> metric;
    private Metadata metadata;
    private String oid;
    private String description;

    public AggregationRule() {
    }

    public List<AggregationRule.RuleItem> getRuleItem() {
        return ruleItem;
    }

    public void setRuleItem(List<AggregationRule.RuleItem> ruleItem) {
        this.ruleItem = ruleItem;
    }

    public List<AggregationRule.Metric> getMetric() {
        return metric;
    }

    public void setMetric(List<AggregationRule.Metric> metric) {
        this.metric = metric;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
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

    public static class Metric {

        private AggregationRule.Metric.Name name;
        private List<AggregationRule.Metric.Dimensions> dimensions;

        public Metric() {
        }

        public AggregationRule.Metric.Name getName() {
            return name;
        }

        public void setName(AggregationRule.Metric.Name name) {
            this.name = name;
        }

        public List<AggregationRule.Metric.Dimensions> getDimensions() {
            return dimensions;
        }

        public void setDimensions(List<AggregationRule.Metric.Dimensions> dimensions) {
            this.dimensions = dimensions;
        }

        public static class Dimensions {

            public Dimensions() {
            }

            private List<Parameter> parameter;

            public List<Parameter> getParameter() {
                return parameter;
            }

            public void setParameter(List<Parameter> parameter) {
                this.parameter = parameter;
            }
        }

        public static class Name {

            private Parameter parameter;

            public Name() {
            }

            public Parameter getParameter() {
                return parameter;
            }

            public void setParameter(Parameter parameter) {
                this.parameter = parameter;
            }

        }
    }

    public static class RuleItem {

        private List<Single> single;
        private List<Unique> unique;
        private List<Accumulate> accumulate;
        private List<Sample> sample;
        private List<Count> count;
        private List<Sum> sum;
        private List<Average> average;
        private List<First> first;
        private List<Last> last;
        private List<Stdev> stdev;
        private List<Variance> variance;
        private List<Max> max;
        private List<Min> min;
        private List<Mul> mul;
        private List<Div> div;
        private List<Add> add;
        private List<Sub> sub;
        private List<Ceil> ceil;
        private List<Floor> floor;
        private List<Exp> exp;
        private List<Log> log;
        private List<Ln> ln;
        private List<Pow> pow;
        private List<Round> round;
        private List<Sqrt> sqrt;
        private List<SlidingWindow> slidingWindow;
        private List<Plugin> plugin;
        private List<Output> output;

        public RuleItem() {
        }

        public List<Single> getSingle() {
            return single;
        }

        public void setSingle(List<Single> single) {
            this.single = single;
        }

        public List<Unique> getUnique() {
            return unique;
        }

        public void setUnique(List<Unique> unique) {
            this.unique = unique;
        }

        public List<Accumulate> getAccumulate() {
            return accumulate;
        }

        public void setAccumulate(List<Accumulate> accumulate) {
            this.accumulate = accumulate;
        }

        public List<Sample> getSample() {
            return sample;
        }

        public void setSample(List<Sample> sample) {
            this.sample = sample;
        }

        public List<Count> getCount() {
            return count;
        }

        public void setCount(List<Count> count) {
            this.count = count;
        }

        public List<Sum> getSum() {
            return sum;
        }

        public void setSum(List<Sum> sum) {
            this.sum = sum;
        }

        public List<Average> getAverage() {
            return average;
        }

        public void setAverage(List<Average> average) {
            this.average = average;
        }

        public List<First> getFirst() {
            return first;
        }

        public void setFirst(List<First> first) {
            this.first = first;
        }

        public List<Last> getLast() {
            return last;
        }

        public void setLast(List<Last> last) {
            this.last = last;
        }

        public List<Stdev> getStdev() {
            return stdev;
        }

        public void setStdev(List<Stdev> stdev) {
            this.stdev = stdev;
        }

        public List<Variance> getVariance() {
            return variance;
        }

        public void setVariance(List<Variance> variance) {
            this.variance = variance;
        }

        public List<Max> getMax() {
            return max;
        }

        public void setMax(List<Max> max) {
            this.max = max;
        }

        public List<Min> getMin() {
            return min;
        }

        public void setMin(List<Min> min) {
            this.min = min;
        }

        public List<Mul> getMul() {
            return mul;
        }

        public void setMul(List<Mul> mul) {
            this.mul = mul;
        }

        public List<Div> getDiv() {
            return div;
        }

        public void setDiv(List<Div> div) {
            this.div = div;
        }

        public List<Add> getAdd() {
            return add;
        }

        public void setAdd(List<Add> add) {
            this.add = add;
        }

        public List<Sub> getSub() {
            return sub;
        }

        public void setSub(List<Sub> sub) {
            this.sub = sub;
        }

        public List<Ceil> getCeil() {
            return ceil;
        }

        public void setCeil(List<Ceil> ceil) {
            this.ceil = ceil;
        }

        public List<Floor> getFloor() {
            return floor;
        }

        public void setFloor(List<Floor> floor) {
            this.floor = floor;
        }

        public List<Exp> getExp() {
            return exp;
        }

        public void setExp(List<Exp> exp) {
            this.exp = exp;
        }

        public List<Log> getLog() {
            return log;
        }

        public void setLog(List<Log> log) {
            this.log = log;
        }

        public List<Ln> getLn() {
            return ln;
        }

        public void setLn(List<Ln> ln) {
            this.ln = ln;
        }

        public List<Pow> getPow() {
            return pow;
        }

        public void setPow(List<Pow> pow) {
            this.pow = pow;
        }

        public List<Round> getRound() {
            return round;
        }

        public void setRound(List<Round> round) {
            this.round = round;
        }

        public List<Sqrt> getSqrt() {
            return sqrt;
        }

        public void setSqrt(List<Sqrt> sqrt) {
            this.sqrt = sqrt;
        }

        public List<SlidingWindow> getSlidingWindow() {
            return slidingWindow;
        }

        public void setSlidingWindow(List<SlidingWindow> slidingWindow) {
            this.slidingWindow = slidingWindow;
        }

        public List<Plugin> getPlugin() {
            return plugin;
        }

        public void setPlugin(List<Plugin> plugin) {
            this.plugin = plugin;
        }

        public List<Output> getOutput() {
            return output;
        }

        public void setOutput(List<Output> output) {
            this.output = output;
        }

    }
}
