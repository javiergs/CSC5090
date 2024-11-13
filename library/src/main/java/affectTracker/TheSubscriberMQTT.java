package affectTracker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attempts to connect to a caller defined MQTT broker, with a caller defined ClientID.
 * A {@code Map<String, String} holds pairs of topics to subscribe to and the corresponding prefix
 * that should be used when sending the data to its destination for parsing.
 * The connection is attempted within the constructor so that the connection is sure to exist before it is
 *  * run as a thread.
 * <p>
 * When running within a Thread, TheSubscriberMQTT simply waits for 'mail' from the broker
 * and processes it by adding the prefix to the message and sending it to its destination.
 */
public class TheSubscriberMQTT extends PropertyChangeSupport implements Runnable, MqttCallback {

    private final Logger log = LoggerFactory.getLogger(TheSubscriberMQTT.class.getName());
    private final Map<String, String> topicAndPrefixPairs;

    public static final String CLIENT_PROPERTY_LABEL = "addClientData";
    public static final String REPORT_ERROR_LABEL = "reportSubscriberError";

    private static final String MQTT_PREFIX = "MQTTE";
    private static final String PREFIX_DELIMITER = "~";
    private boolean running = true;

    public TheSubscriberMQTT(String broker, String clientID, Map<String, String> topicAndPrefixPairs, PropertyChangeListener listener) throws MqttException {
        super(new Object());
        this.topicAndPrefixPairs = topicAndPrefixPairs;
        try {
            MqttClient client = new MqttClient(broker, clientID);
            client.setCallback(this);
            client.connect();
            log.info("Connected to broker: " + broker);
            for (String topic : topicAndPrefixPairs.keySet()){
                client.subscribe(topic);
                log.info("Subscribed to topic: " + topic);
            }
            this.addPropertyChangeListener(CLIENT_PROPERTY_LABEL, listener);
            this.addPropertyChangeListener(REPORT_ERROR_LABEL, listener);
        } catch (MqttException e) {
            log.warn("Unable to connect to broker --" + e.getMessage());
            throw e;
       }
    }

    @Override
    public void run() {
        try {
            //keep the thread alive and idle while waiting for new data
            while (running) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            String mqttErrorPrefixWithDelim = MQTT_PREFIX + PREFIX_DELIMITER ;
            firePropertyChange(REPORT_ERROR_LABEL, null, mqttErrorPrefixWithDelim +
                    e.getMessage());
            log.warn("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        firePropertyChange(CLIENT_PROPERTY_LABEL, null, topicAndPrefixPairs.get(s) +
                PREFIX_DELIMITER + mqttMessage);
        log.debug("Message Arrived. Topic: " + s +
                " Message: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void stopSubscriber() {
        running = false;
    }
}
