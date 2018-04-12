package eu.selfnet.ae.storm;

import eu.selfnet.ae.storm.feedback.FeedbackListnerSpout;
import eu.selfnet.ae.storm.feedback.ReportCompilerBolt;
import eu.selfnet.ae.storm.register.ContextResolutionBolt;
import eu.selfnet.ae.storm.register.RegisterSpout;
import eu.selfnet.ae.storm.handler.ActionContextTypeBolt;
import eu.selfnet.ae.storm.requester.ActionListnerSpout;
import eu.selfnet.ae.storm.requester.RequestCreatorBolt;
import eu.selfnet.ae.storm.requester.RequesterBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

/**
 *
 * Class that creates the Storm Topology
 */
public class Topology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        // Register
        builder.setSpout("Register-Source", new RegisterSpout(), 1);
        builder.setBolt("Register", new ContextResolutionBolt(), 1)
                .shuffleGrouping("Register-Source");

        // Requester
        builder.setBolt("ActionType", new ActionContextTypeBolt(), 1)
                .shuffleGrouping("Register", "Action-Resolver");
        /*
        The Action Instance is currently not used since this
        the other needed APIs are not working
        
        builder.setBolt("ActionInstance", new ActionInstanceBolt(), 1)
                .shuffleGrouping("ActionType", "Action-Instance");
        */        

        // Orchestrator Requester
        builder.setSpout("Action-Listner", new ActionListnerSpout(), 1);

        builder.setBolt("Request-Creater", new RequestCreatorBolt(), 1)
                .shuffleGrouping("Action-Listner");

        builder.setBolt("Client", new RequesterBolt(), 1)
                .shuffleGrouping("Request-Creater", "Or-Request-Creator");

        // Feedback
        builder.setSpout("Feedback-Source", new FeedbackListnerSpout(), 1);
        builder.setBolt("Report-Compiler", new ReportCompilerBolt())
                .shuffleGrouping("Feedback-Source");

        Config config = new Config();
        if (args != null && args.length > 0) {
            config.setNumWorkers(1);  // Sets the number of workers to be used 
            StormSubmitter.submitTopology("ae-topology", config,
                    builder.createTopology());
        } else {
            // Load topology in a local cluster
            config.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();

            cluster.submitTopology("ae-topology", config,
                    builder.createTopology());

        }
    }
}
