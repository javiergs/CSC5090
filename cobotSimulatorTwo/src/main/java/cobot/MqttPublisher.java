package cobot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * The MqttPublisher class establishes a connection to an MQTT broker and continuously publishes messages
 * to a specified topic. The messages contain information about the week counter and are sent at regular intervals.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class MqttPublisher {
    /**
     * The main method that initializes the MQTT client and publishes messages to the broker.
     * Connects to the MQTT broker at a specified address, publishes messages on the topic in a loop,
     * and manages exceptions for connection and message publishing.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String broker = "tcp://broker.emqx.io:1833";
        String clientId = "demo_client";
        String topic = "CSC 509";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Connected to broker: " + broker);

            int counter = 0;
            while (true) {
                String content = "This is week " + counter;
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(2);
                if (client.isConnected()) client.publish(topic, message);
                System.out.println("Message published: " + content);
                Thread.sleep(5000);
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}