package cobot;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MqttSubscriber class connects to an MQTT broker, subscribes to a specified topic, and processes incoming messages.
 * It listens for commands that represent angles and updates a shared blackboard with these values.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class MqttSubscriber implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private final String broker = "tcp://broker.emqx.io:1883";
    private final String clientId = "demo_subscriber";
    private final String topic = "CSC 509";
    private MqttClient client;

    /**
     * Constructor for MqttSubscriber. Initializes the MQTT client, connects to the broker,
     * sets the callback, and subscribes to the topic.
     */
    public MqttSubscriber() {
        try {
            client = new MqttClient(broker, clientId);
            client.setCallback(this);
            client.connect();
            client.subscribe(topic);
            logger.info("Subscriber is connected and subscribed to topic {}", topic);
        } catch (MqttException e) {
            logger.error("Error initializing Subscriber: {}", e.getMessage(), e);
        }
    }

    /**
     * Called when the MQTT connection is lost. Logs the cause of the disconnection.
     *
     * @param cause the reason for the connection loss
     */
    @Override
    public void connectionLost(Throwable cause) {
        logger.error("Connection lost: {}", cause.getMessage(), cause);
    }

    /**
     * Called when a message arrives from the subscribed topic. The message is parsed and processed.
     *
     * @param s           the topic the message was received from
     * @param mqttMessage the received MQTT message containing the command
     * @throws Exception if any error occurs while handling the message
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String command = new String(mqttMessage.getPayload());
        logger.info("Received message: {}", command);
        parse(command);
    }

    /**
     * Called when the delivery of a message is complete. (Not used in this subscriber).
     *
     * @param iMqttDeliveryToken the token associated with the delivered message
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    /**
     * Parses the received command to extract angle values and updates the Blackboard instance.
     *
     * @param command the command string containing comma-separated angle values
     */
    private void parse(String command) {
        String[] tokens = command.split(",");
        try {
            int[] numbers = new int[6];
            for (int i = 0; i < 6; i++) {
                numbers[i] = Integer.parseInt(tokens[i].trim());
            }
            Blackboard.getInstance().setAngles(numbers);
            System.out.println();
        } catch (NumberFormatException e) {
            logger.error("Error parsing command: {}", command, e);
        }
    }

    /**
     * Disconnects the subscriber from the MQTT broker, if connected.
     */
    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                logger.info("Subscriber disconnected");
            }
        } catch (MqttException e) {
            logger.error("Error disconnecting Subscriber: {}", e.getMessage(), e);
        }
    }

    /**
     * Checks if the subscriber is currently connected to the MQTT broker.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}