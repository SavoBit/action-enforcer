/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import eu.selfnet.ae.conf_reader.ConfReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * Class that creates a RabbitMQ message producer
 */
public class RabbitMQProducer {

    private final RabbitMQConfig config;
    private final Channel channel;
    private final Connection connection;

    public RabbitMQProducer(String connectionSection,
            String exchangeSection, String queueSection,
            String routingSection) throws IOException, TimeoutException {

        this.config = new RabbitMQConfig(
                new ConfReader(),
                connectionSection,
                exchangeSection,
                queueSection,
                routingSection
        );

        this.connection = config.createConnection();
        this.channel = connection.createChannel();

        config.setExchange(channel);

    }

    public void sendMessage(String message, String key) throws IOException {
        this.channel.basicPublish(
                this.config.getExchangeName(),
                key,
                null,
                message.getBytes()
        );
    }

    public void sendMessage(String message) throws IOException {
        sendMessage(message, "");
    }

    public void close() throws IOException, TimeoutException {
        this.channel.close();
        this.connection.close();
    }

}
