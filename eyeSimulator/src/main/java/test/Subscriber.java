package test;

import app.DataDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Subscriber implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private final String host;
    private final int port;
    private final DataDestination dataDestination;
    private volatile boolean running = true;

    public Subscriber(String host, int port, DataDestination destination) {
        this.host = host;
        this.port = port;
        this.dataDestination = destination;
    }

    public void stopSubscriber() {
        running = false;
        logger.info("Subscriber stopped.");
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
            logger.info("Connected to TCP server at {}:{}", host, port);
            while (running) {
                String receivedMessage = inputStream.readUTF();
                dataDestination.addSubscriberData(receivedMessage);
                logger.debug("Received data: {}", receivedMessage);
            }
        } catch (IOException e) {
            dataDestination.alertError("Error in Subscriber: " + e.getMessage());
            logger.error("Error in Subscriber", e);
        }
    }
}
