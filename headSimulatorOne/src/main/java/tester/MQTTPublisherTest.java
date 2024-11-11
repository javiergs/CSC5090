package tester;

import components.MQTTPublisher;

public class MQTTPublisherTest {

    public static void main(String[] args) {
        // Use a public MQTT broker for testing
        String broker = "tcp://test.mosquitto.org:1883"; // Replace with your broker URL if needed
        String clientId = "TestPublisher";

        MQTTPublisher publisher = new MQTTPublisher(broker, clientId);
        publisher.connect();

        String topic = "test/topic"; // Ensure this topic is not causing conflicts
        String messageContent = "Hello, MQTT!";

        publisher.publish(topic, messageContent);

        publisher.disconnect();
    }
}