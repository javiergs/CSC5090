package head;


import components.MQTTPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MQTTServer implements Runnable, PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(MQTTServer.class);
    private final String broker;
    private final String clientId;
    private final String topic;
    private boolean isReady = false;
    private Point point;


    public MQTTServer(String broker, String clientId, String topic) {
        this.broker = broker;
        this.clientId = clientId;
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            MQTTPublisher mqttPublisher = new MQTTPublisher(broker, clientId);
            mqttPublisher.connect();


            if (mqttPublisher.isConnected()) {
                isReady = true;
            }

            while (isReady) {
                try {
                    Thread.sleep(1000 / 30);
                    mqttPublisher.publish(topic, point.toString());

                } catch (Exception e) {
                    logger.error("Error in Server: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error in Server: {}", e.getMessage(), e);
        } finally {
            isReady = false;
        }
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("point".equals(evt.getPropertyName())) {
            point = (Point) evt.getNewValue();
        }
    }
}
