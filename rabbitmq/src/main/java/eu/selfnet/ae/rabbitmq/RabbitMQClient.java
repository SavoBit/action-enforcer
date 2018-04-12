/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.rabbitmq;

import java.io.Serializable;
import com.rabbitmq.client.*;
import eu.selfnet.ae.conf_reader.ConfReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * Class that will subscribe a RabbitMQ channel
 */
public class RabbitMQClient implements Serializable {

    private final Consumer consumer;

    public RabbitMQClient(String connectionSection, String exchangeSection,
            String queueSection) throws IOException, TimeoutException {

        RabbitMQConfig config = new RabbitMQConfig(
                new ConfReader(),
                connectionSection,
                exchangeSection,
                queueSection,
                "Message_Queue_Routing_Keys"
        );
        Connection connection = config.createConnection();
        Channel channel = connection.createChannel();

        config.setExchange(channel);
        config.setQueue(channel);
        config.bind(channel);

        this.consumer = new Consumer(channel);
        channel.basicConsume(config.getQueueName(), false, consumer);
    }

    public Message nextMessage() {
        return Consumer.nextMessage();
    }

}
