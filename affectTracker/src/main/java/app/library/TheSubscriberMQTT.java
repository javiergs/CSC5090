package app.library;

import app.Model.Blackboard;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheSubscriberMQTT implements Runnable, MqttCallback {

    private final Logger log = LoggerFactory.getLogger(TheSubscriberMQTT.class.getName());
    private final String broker;
    private final String clientID;

    private final Map<String, String> topicAndPrefixPairs;

    public TheSubscriberMQTT(String broker, String clientID, Map<String, String> topicAndPrefixPairs) {
        this.broker = broker;
        this.clientID = clientID;
        this.topicAndPrefixPairs = topicAndPrefixPairs;
    }

    @Override
    public void run() {
        try (MqttClient client = new MqttClient(broker, clientID);){
            client.setCallback(this);
            client.connect();
            log.info("Connected to broker: " + broker);
            for (String topic : topicAndPrefixPairs.keySet()){
                client.subscribe(topic);
                log.info("Subscribed to topic: " + topic);
            }
            //keep the thread alive and idle while waiting for new data
            while (true) {
                Thread.sleep(1000);
            }
        } catch (MqttException e) {
            log.warn("Unable to connect to broker --" + e.getMessage());
            Blackboard.getInstance().reportMQTTBrokerError("Unable to connect to broker --\n\t" + e.getMessage());
        } catch (InterruptedException e) {
            log.warn( "Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        Blackboard.getInstance().addClientData(topicAndPrefixPairs.get(s) + "~" + mqttMessage);
        log.debug("Message Arrived. Topic: " + s +
                " Message: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
