package cobotSimulatorOneLibrary;

import org.eclipse.paho.client.mqttv3.*;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTTSubscriber subscribes to an MQTT topic and sends received messages to a specified consumer.
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class MQTTSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriber.class);
    private final String brokerUrl;
    private final String topic;
    private final Consumer<String> messageConsumer;
    private MqttClient client;

    public MQTTSubscriber(String brokerUrl, String topic, Consumer<String> messageConsumer) {
        this.brokerUrl = brokerUrl;
        this.topic = topic;
        this.messageConsumer = messageConsumer;
    }

    public void start() {
        try {
            client = new MqttClient(brokerUrl, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            client.connect(options);
            client.subscribe(topic, this::messageArrived);
            logger.info("MQTTSubscriber connected to broker and subscribed to topic: {}", topic);
        } catch (MqttException e) {
            logger.error("Error starting MQTTSubscriber", e);
        }
    }

    public void stop() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
                logger.info("MQTTSubscriber disconnected from broker");
            }
        } catch (MqttException e) {
            logger.error("Error stopping MQTTSubscriber", e);
        }
    }

    private void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Received message on topic {}: {}", topic, payload);
        messageConsumer.accept(payload);  // Pass message to the specified consumer
    }
}
