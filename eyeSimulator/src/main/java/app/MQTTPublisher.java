package app;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;

/**
 * A publisher that connects to an MQTT broker and publishes click positions
 * from the Blackboard.
 *
 * @version 1.2
 * @authors
 * Monish Suresh
 * Christine Widden
 * Luca Ornstil
 */

public class MQTTPublisher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MQTTPublisher.class);
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "EyeTrackingPublisher";
    private static final String TOPIC = "eye-tracking/topic";
    private MqttClient client;
    private volatile boolean running = true;

    public MQTTPublisher() {
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);
            logger.info("Connected to MQTT Broker at {}.", BROKER_URL);
        } catch (MqttException e) {
            logger.error("Failed to connect to MQTT Broker", e);
        }
    }

    public void publish(String message) {
        try {
            if (client != null && client.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(2);
                client.publish(TOPIC, mqttMessage);
                Blackboard.getInstance().clearClicks();
                logger.debug("Published message to MQTT topic {}: {}", TOPIC, message);
            }
        } catch (MqttException e) {
            logger.error("Failed to publish message", e);
        }
    }

    public void stopPublisher() {
        running = false;
        logger.info("MQTTPublisher stopped.");
    }

    @Override
    public void run() {
        try {
            while (running) {
                ArrayList<Point> clickPositions = Blackboard.getInstance().getClickPositions();
                if (!clickPositions.isEmpty()) {
                    String message = "Clicks: " + Blackboard.getInstance().getClickPositions();
                    publish(message);
                }
                Thread.sleep(1000);  // Keep the publisher alive
            }
        } catch (InterruptedException e) {
            logger.warn("MQTTPublisher interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
