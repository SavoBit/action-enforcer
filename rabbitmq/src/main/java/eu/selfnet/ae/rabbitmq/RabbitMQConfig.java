/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import eu.selfnet.ae.conf_reader.ConfReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *
 * Class responsible to configure a RabbitMQ connection
 */
public class RabbitMQConfig {

    private final ConfReader config;
    private final String connectionSection;
    private final String exchangeSection;
    private final String queueSection;
    private String routingSection = "";

    private String exchangeName = "";
    private String queueName = "";
    private final List<String> routingKeys;

    public RabbitMQConfig(ConfReader config, String connectionSection,
            String exchangeSection, String queueSection) {
        this.routingKeys = new ArrayList<>();

        this.config = config;
        this.connectionSection = connectionSection;
        this.exchangeSection = exchangeSection;
        this.queueSection = queueSection;

    }

    public RabbitMQConfig(ConfReader config, String connectionSection,
            String exchangeSection, String queueSection,
            String routingSection) {
        this(config, connectionSection, exchangeSection, queueSection);
        this.routingSection = routingSection;
        loadRoutingKeys();
    }

    public Connection createConnection(String uri) throws
            URISyntaxException, NoSuchAlgorithmException,
            KeyManagementException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        return factory.newConnection();
    }

    public Connection createConnection() throws IOException, TimeoutException {
        HashMap<String, String> map
                = this.config.getSection(this.connectionSection);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(map.get("host"));
        factory.setPort(Integer.valueOf(map.get("port")));
        if (!map.get("virtual_host").equals("")) {
            factory.setVirtualHost(map.get("virtual_host"));
        }
        if (!map.get("username").equals("")
                && !map.get("password").equals("")) {
            factory.setUsername(map.get("username"));
            factory.setPassword(map.get("password"));
        }
        return factory.newConnection();
    }

    public void setExchange(Channel channel) throws IOException {
        HashMap<String, String> map
                = this.config.getSection(this.exchangeSection);
        String exchangeType = map.get("exchange_type");
        Boolean durable
                = Boolean.valueOf(map.get("durable").toLowerCase());
        Boolean autoDelete
                = Boolean.valueOf(map.get("auto_delete").toLowerCase());
        Boolean internal
                = Boolean.valueOf(map.get("internal").toLowerCase());

        channel.exchangeDeclare(
                this.getExchangeName(), exchangeType, durable,
                autoDelete, internal, null);

    }

    public void setQueue(Channel channel) throws IOException {
        HashMap<String, String> map = this.config.getSection(this.queueSection);
        Boolean durable
                = Boolean.valueOf(map.get("durable").toLowerCase());
        Boolean autoDelete
                = Boolean.valueOf(map.get("auto_delete").toLowerCase());
        Boolean exclusive
                = Boolean.valueOf(map.get("exclusive").toLowerCase());

        channel.queueDeclare(
                this.getQueueName(), durable,
                exclusive, autoDelete, null);
    }

    public void bind(Channel channel) throws IOException {
        String exchange
                = this.config.getSection(this.exchangeSection).get("exchange");
        String queue
                = this.config.getSection(this.queueSection).get("queue_name");
        if (this.routingSection.equals("")) {
            channel.queueBind(queue, exchange, "");
            return;
        }

        for (String key : this.routingKeys) {
            channel.queueBind(queue, exchange, key);
        }

    }

    private void loadRoutingKeys() {
        HashMap<String, String> map = this.config.
                getSection(this.routingSection);
        map.keySet().stream().forEach((key) -> {
            this.routingKeys.addAll(Arrays.asList(
                    map.get(key).split("\\s*,\\s*")));
        });
    }

    public String getQueueName() {
        if (!this.queueName.equals("")) {
            return this.queueName;
        }

        this.queueName
                = this.config.getSection(this.queueSection).get("queue_name");
        return this.queueName;
    }

    public String getExchangeName() {
        if (!this.exchangeName.equals("")) {
            return this.exchangeName;
        }

        this.exchangeName
                = this.config.getSection(this.exchangeSection).get("exchange");
        return this.exchangeName;
    }

}
