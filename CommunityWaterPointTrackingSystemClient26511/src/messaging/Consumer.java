/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

/**
 *
 * @author Jospin Nabonyimana
 */
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.JMSException;


public class Consumer {
    public static void main(String[] args) {
        try {
            // Connect to ActiveMQ broker
            String brokerURL = "tcp://localhost:61616";
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

            // Create connection
            Connection connection = factory.createConnection();
            connection.start();

            // Create session and queue
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("TEST.QUEUE");

            // Create consumer and receive message
            MessageConsumer consumer = session.createConsumer(queue);
            TextMessage message = (TextMessage) consumer.receive();

            System.out.println("Received message: " + message.getText());

            // Close everything
            consumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

