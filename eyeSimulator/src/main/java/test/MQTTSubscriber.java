package test;

import eyesimulator.DataDestination;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscriber that connects to an MQTT broker, receives messages, and forwards
 * them to a DataDestination.
 * 
 * @version 1.2
 * @author Monish Suresh
 * @author Christine Widden
 * @author Luca Ornstil
 */

public class MQTTSubscriber implements Runnable, MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriber.class);
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC = "eye-tracking/topic";
    private final DataDestination dataDestination;
    private volatile boolean running = true;
    private MqttClient client;

    public MQTTSubscriber(DataDestination destination) {
        this.dataDestination = destination;
    }

    public void stopSubscriber() {
        running = false;
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
                logger.info("Disconnected from MQTT broker.");
            } catch (MqttException e) {
                logger.error("Error while disconnecting MQTTSubscriber", e);
            }
        }
    }

    @Override
    public void run() {
        try {
            client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.setCallback(this);
            client.connect(options);
            client.subscribe(TOPIC);
            logger.info("Connected to MQTT broker and subscribed to topic: {}", TOPIC);

            while (running) {
                Thread.sleep(1000);  // Keep the subscriber alive
            }
        } catch (MqttException | InterruptedException e) {
            dataDestination.alertError("Error in MQTTSubscriber: " + e.getMessage());
            logger.error("Error in MQTTSubscriber", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        dataDestination.addSubscriberData(payload);
        logger.debug("Message arrived. Topic: {} Message: {}", topic, payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not applicable for Subscriber
    }
}
