package tester;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTTPublisher publishes messages to an MQTT topic.
 */
public class MQTTPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MQTTPublisher.class);
    private final String brokerUrl;
    private final String topic;
    private MqttClient client;

    public MQTTPublisher(String brokerUrl, String topic) {
        this.brokerUrl = brokerUrl;
        this.topic = topic;
    }

    public void start() {
        try {
            client = new MqttClient(brokerUrl, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            client.connect(options);
            logger.info("MQTTPublisher connected to broker and ready to publish on topic: {}", topic);
        } catch (MqttException e) {
            logger.error("Error starting MQTTPublisher", e);
        }
    }

    public void stop() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
                logger.info("MQTTPublisher disconnected from broker");
            }
        } catch (MqttException e) {
            logger.error("Error stopping MQTTPublisher", e);
        }
    }

    public void sendMessage(String message) {
        try {
            if (client != null && client.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(1);
                client.publish(topic, mqttMessage);
                logger.info("Published message to topic {}: {}", topic, message);
            } else {
                logger.warn("MQTTPublisher is not connected. Unable to publish message.");
            }
        } catch (MqttException e) {
            logger.error("Error sending message from MQTTPublisher", e);
        }
    }
}
