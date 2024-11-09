package cobot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher {
    public static void main(String[] args) {
        String broker = "tcp://broker.emqx.io:1833";
        String clientId = "demo_client";
        String topic = "CSC 509";

        try {
            MqttClient client = new MqttClient(broker,clientId);
            client.connect();
            System.out.println("Connected to broker: " + broker);

            int counter = 0;
            while (true){
                String content = "This is week " + counter;
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(2);
                if (client.isConnected()) client.publish(topic, message);
                System.out.println("Message published: " + content);
                Thread.sleep(5000);
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}