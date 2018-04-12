======================
AE STORM Topology
======================

This is a storm topology to resolve action dependencies.

This application is using Apache Storm, RabbitMQ and MongoDB

It is composed by two spouts, one with RabbitMQ and other with MongoDB, each using two executors. 
There are three bolts, one to register the incoming messages from RabbitMQ and stores them in MongoDB, another one to simulate a parser, changing the state of the message and other that publishes feedback messages in a RabbitMQ


======================
Requirements
======================

Install and configure :
     
     - Zookeper 3.4.9
     - Apache Sorm 1.0.3
     - RabbitMQ Server
     - MongoDB

	 

======================
Configuration
======================

First : Configure* conf.ini file inside on actionEnforcer/conf-reader/src/main/resources
	-You need to change the configuration related to RabbitMQ and MongoDB
	-Within the conf.ini file it must be configured DB access, name and address, RMQ access, and others. This file must be located in a specific folder before build the jar locate the file and edit the class within conf-reader/src/main/java/eu/selfnet/ae/conf_reader/ConfReader.java line number 35.
	
Second: After doing the configuration you will need to compile the code with:
		- maven command: mvn package
		- compile the code with dependencies from your IDE
		
As a result: JAR File will be created e.g. "storm-1.0-SNAPSHOT-jar-with-dependencies.jar"


======================
Usage
======================

To run the topology you will need the created jar file in actionEnforcer/storm/target:

	bin/storm jar ~/pathToJarFile/storm-1.0-SNAPSHOT-jar-with-dependencies.jar eu.selnet.ae.storm.topology test

You can see on Storm UI the new topology from ctionEnforcer/storm/:
	
	bin/storm ui

