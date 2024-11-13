package affectTracker;

import java.util.Map;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheSubscriberMQTT implements Runnable, MqttCallback {

    private final Logger log = LoggerFactory.getLogger(TheSubscriberMQTT.class.getName());
    private final Map<String, String> topicAndPrefixPairs;
    private final DataDestination dataDestination;

    private static final String MQTT_PREFIX = "MQTTE";
    private static final String PREFIX_DELIMITER = "~";
    private boolean running = true;

    public TheSubscriberMQTT(String broker, String clientID, Map<String, String> topicAndPrefixPairs, DataDestination destination) throws MqttException {
        this.topicAndPrefixPairs = topicAndPrefixPairs;
        this.dataDestination = destination;
        try {
            MqttClient client = new MqttClient(broker, clientID);
            client.setCallback(this);
            client.connect();
            log.info("Connected to broker: " + broker);
            for (String topic : topicAndPrefixPairs.keySet()){
                client.subscribe(topic);
                log.info("Subscribed to topic: " + topic);
            }
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
            dataDestination.alertError(mqttErrorPrefixWithDelim +
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
        dataDestination.addSubscriberData(topicAndPrefixPairs.get(s) +
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
