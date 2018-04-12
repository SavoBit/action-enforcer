/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import java.io.UnsupportedEncodingException;

/**
 *
 * RabbitMQ Message containing every delivered object
 *
 * The received body is converted to an UTF-8 string
 *
 */
public class Message {

    private final String consumerTag;
    private final Envelope envelope;
    private final AMQP.BasicProperties properties;
    private final String message;

    public Message(String consumerTag, Envelope envelope,
            AMQP.BasicProperties properties, byte[] body)
            throws UnsupportedEncodingException {
        this.consumerTag = consumerTag;
        this.envelope = envelope;
        this.properties = properties;
        this.message = new String(body, "UTF-8");
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public AMQP.BasicProperties getProperties() {
        return properties;
    }

    public String getMessage() {
        return message;
    }

}
