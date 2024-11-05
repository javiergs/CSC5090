package app;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MQTTPublisher.class);
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "EyeTrackingPublisher";
    private static final String TOPIC = "eye-tracking/topic";
    private MqttClient client;

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
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish(TOPIC, mqttMessage);
            logger.debug("Message published to topic {}: {}", TOPIC, message);
        } catch (MqttException e) {
            logger.error("Failed to publish message", e);
        }
    }
}
