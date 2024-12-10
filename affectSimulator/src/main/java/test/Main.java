package test;

import simulator_affect.Blackboard;
import affectSimulator.MQTTPublisher;
import affectSimulator.MQTTSubscriber;

/**
 * Main class to test MQTT communication between MQTTPublisher and MQTTSubscriber.
 *
 * Author: Yayun Tan
 * Version: 1.0
 */

public class Main {

    public static void main(String[] args) {
        Blackboard blackboard = Blackboard.getInstance();
        blackboard.setRunning(true);

        MQTTPublisher publisher = MQTTPublisher.getInstance(blackboard);
        MQTTSubscriber subscriber = new MQTTSubscriber(blackboard);

        Thread subscriberThread = new Thread(subscriber);
        subscriberThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Thread publisherThread = new Thread(publisher);
        publisherThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        publisher.publish("Test message from Main class.");
    }
}
