package components;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTPublisher{

    private final String BROKER;
    private final String CLIENT_ID;
    private MqttClient client;
    private static final Logger logger = LoggerFactory.getLogger(MQTTPublisher.class);


    public MQTTPublisher(String broker, String clientId){

        this.BROKER = broker;
        this.CLIENT_ID = clientId;
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
        } catch (MqttException e) {
            logger.error("Error in Publisher", e);
        }
    }


    public void connect(){
        try {
            client.connect();
            System.out.println(CLIENT_ID + " connected to " + BROKER);
        } catch (MqttException e) {
            logger.error("Error in Publisher", e);
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
            logger.error("Error in Publisher", e);
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
            logger.error("Error in Publisher", e);
        }
    }



}