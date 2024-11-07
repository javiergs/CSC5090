package app.library;

import app.Model.Blackboard;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheSubscriberMQTT implements Runnable, MqttCallback {

    private final Logger log = LoggerFactory.getLogger(TheSubscriberMQTT.class.getName());
    private final String broker;
    private final String topic;
    private final String clientID;
    private final String dataPrefix;
    public TheSubscriberMQTT(String broker, String topic, String clientID, String dataPrefix) {
        this.broker = broker;
        this.topic = topic;
        this.clientID = clientID;
        this.dataPrefix = dataPrefix;
    }

    @Override
    public void run() {
        try (MqttClient client = new MqttClient(broker, clientID);){
            client.setCallback(this);
            client.connect();
            log.info("Connected to broker: " + broker);
            client.subscribe(topic);
            log.info("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            log.warn("Unable to connect to broker --" + e.getMessage());
            Blackboard.getInstance().reportMQTTBrokerError("Unable to connect to broker --\n\t" + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        Blackboard.getInstance().addClientData(dataPrefix + "~" + mqttMessage);
        log.debug("Message Arrived. Topic: " + s +
                " Message: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
