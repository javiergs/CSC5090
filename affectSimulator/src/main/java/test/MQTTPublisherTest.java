package test;

import simulator_affect.Blackboard;
import affectSimulator.MQTTPublisher;

/**
 * MQTTPublisherTest class to verify MQTT message publishing.
 * Initializes Blackboard and MQTTPublisher, connects to the MQTT broker,
 * and publishes a test message to validate functionality.
 *
 * Author: Yayun Tan
 * Version: 1.0
 */

public class MQTTPublisherTest {

    public static void main(String[] args) {
        Blackboard blackboard = Blackboard.getInstance();

        blackboard.setRunning(true);

        MQTTPublisher publisher = MQTTPublisher.getInstance(blackboard);

        System.out.println("Connecting to MQTT Broker...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String testMessage = "Hello from MQTTPublisherTest!";
        publisher.publish(testMessage);

        System.out.println("Message published: " + testMessage);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
