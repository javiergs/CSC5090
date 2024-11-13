package head;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import library.MQTTPublisher;

/**
 * This class is an MQTT Server that connects to a broker and publishes point data to it
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */

public class MQTTServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MQTTServer.class);
    private final String broker;
    private final String clientId;
    private final String topic;
    private boolean isReady = false;
    private Encoder encoder;


    public MQTTServer(String broker, String clientId, String topic) {
        this.broker = broker;
        this.clientId = clientId;
        this.topic = topic;
        this.encoder = new Encoder();   
        head.Blackboard.getInstance().addPropertyChangeListener(encoder);   
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
                    String point = encoder.getData();
                    Thread.sleep(1000 / 30);
                    if (point == null) continue;
                    mqttPublisher.publish(topic, point);
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
}
