/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Class responsible to consume a RabbitMQ queue and handles the delivered
 * messages
 *
 * This class creates a queue with all messages received
 */
public class Consumer extends DefaultConsumer {

    private static final LinkedBlockingQueue<Message> MESSAGE_QUEUE
            = new LinkedBlockingQueue<>();
    private static Channel CHANNEL;

    public Consumer(Channel channel) {
        super(channel);
        CHANNEL = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
            AMQP.BasicProperties properties, byte[] body) throws IOException {
        try {
            MESSAGE_QUEUE.put(
                    new Message(consumerTag, envelope, properties, body)
            );
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        CHANNEL.basicAck(envelope.getDeliveryTag(), false);
    }

    /**
     * Provide the next message in the queue.
     *
     * @return Message object with every RabbitMQ delivery objects.
     */
    public static Message nextMessage() {
        if (MESSAGE_QUEUE.isEmpty()) {
            return null;
        }
        Message m = MESSAGE_QUEUE.poll();
        return m;
    }
}
