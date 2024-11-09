package cobot;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttSubscriber implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private final String broker = "tcp://broker.emqx.io:1883";
    private final String clientId = "demo_subscriber";
    private final String topic = "CSC 509";

    private MqttClient client;

    public MqttSubscriber() {
        try{
            client = new MqttClient(broker,clientId);
            client.setCallback(this);
            client.connect();
            client.subscribe(topic);
            logger.info("Subscriber is connected and subscribed to topic {}", topic);
        }catch (MqttException e){
            logger.error("Error initializing Subscriber: {}",e.getMessage(), e);
        }
    }
    @Override
    public void connectionLost(Throwable cause) {
        logger.error("Connection lost: {}", cause.getMessage(), cause);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String command = new String(mqttMessage.getPayload());
        logger.info("Received message: {}", command);
        parse(command);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    private void parse(String command) {
        String [] tokens = command.split(",");
        try {
            int[] numbers = new int[6];
            for (int i = 0; i < 6; i++){
                numbers[i] = Integer.parseInt(tokens[i].trim());
            }
            Blackboard.getInstance().setAngles(numbers);
            System.out.println();
        } catch (NumberFormatException e){
            logger.error("Error parsing command: {}",command,e);
        }
    }

    public void disconnect(){
        try {
            if (client != null && client.isConnected()){
                client.disconnect();
                logger.info("Subscriber disconnected");
            }
        } catch (MqttException e){
            logger.error("Error disconnecting Subscriber: {}",e.getMessage(), e);
        }
    }
    public boolean isConnected(){
        return client != null && client.isConnected();
    }
}