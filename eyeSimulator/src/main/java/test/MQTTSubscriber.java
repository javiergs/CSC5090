package test;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTSubscriber implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriber.class);
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC = "eye-tracking/topic";
    private static final String CLIENT_ID = "EyeTrackingSubscriber";

    public MQTTSubscriber() {
        try {
            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID);
            client.setCallback(this);
            client.connect();
            logger.info("Connected to broker: {}", BROKER_URL);
            client.subscribe(TOPIC);
            logger.info("Subscribed to topic: {}", TOPIC);
        } catch (MqttException e) {
            logger.error("Failed to connect or subscribe to MQTT Broker", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Connection lost: {}", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        logger.debug("Message arrived. Topic: {} Message: {}", topic, new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used in subscriber
    }

    public static void main(String[] args) {
        new MQTTSubscriber();
    }
}
