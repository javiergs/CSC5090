import org.eclipse.paho.client.mqttv3.*;
/**
 * This class is the common implementaion of the MQTT Subscriber 
 * 
 * It has implemnetation for establishing connections and reciving data all using an MQTT Client 
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */




public class TheSubscriberMQTT implements MqttCallback{
    String broker;
    String clientId;

    public TheSubscriberMQTT(String broker, String clientId) {
        this.broker = broker;
        this.clientId = clientId;
    }

    public void subscribe(String topic) {
        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.setCallback(new TheSubscriberMQTT(this.broker, this.clientId));
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