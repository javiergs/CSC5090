package components;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTSubscriber implements MqttCallback{
    String broker;
    String clientId;

    public MQTTSubscriber(String broker, String clientId) {
        this.broker = broker;
        this.clientId = clientId;
    }

    public void subscribe(String topic) {
        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.setCallback(new MQTTSubscriber(this.broker, this.clientId));
            client.connect();
            System.out.println("Connected to broker: " + broker);
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("Message arrived on: " + s + "Message: " + new String(mqttMessage.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken IMqttDeliveryToken) {}
}