package com.company;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTSubscriber implements MqttCallback, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriber.class);
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String TOPIC = "project/topic";
    private static final String CLIENT_ID = "ProjectSubscriber";
    private MqttClient client;
    private final SliderListener sliderListener;

    public MQTTSubscriber(SliderListener sliderListener) {
        this.sliderListener = sliderListener;
    }

    @Override
    public void run() {
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID);
            client.setCallback(this);
            client.connect();
            client.subscribe(TOPIC);
            logger.info("Connected to broker: {}", BROKER_URL);
            logger.info("Subscribed to topic: {}", TOPIC);
        } catch (MqttException e) {
            logger.error("Failed to connect or subscribe to MQTT Broker", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Connection lost: {}", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String receivedMessage = new String(message.getPayload());
        logger.debug("Message arrived. Topic: {} Message: {}", topic, receivedMessage);

        String[] parts = receivedMessage.split(" updated to: ");
        if (parts.length == 2) {
            String sliderName = parts[0];
            int newValue = Integer.parseInt(parts[1]);
            sliderListener.setSliderValueExternally(sliderName, newValue);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used in subscriber
    }
}
