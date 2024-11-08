package components;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTPublisher implements Runnable{

    private String BROKER;
    private String CLIENT_ID;
    private MqttClient client;



    public MQTTPublisher(String broker, String clientId){
        this.BROKER = broker;
        this.CLIENT_ID = clientId;
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void connect(){
        try {
            client.connect();
            System.out.println(CLIENT_ID + " connected to " + BROKER);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return client.isConnected();
    }

    public void disconnect(){
        try {
            client.disconnect();
            System.out.println(CLIENT_ID + " disconnected from " + BROKER);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String content){
        try {
            MqttMessage message= new MqttMessage(content.getBytes());
            message.setQos(2);

            if (client.isConnected()) {
                client.publish(topic, message);
            }

            System.out.println("Message published on " + topic + ": " + message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connect();
    }


}