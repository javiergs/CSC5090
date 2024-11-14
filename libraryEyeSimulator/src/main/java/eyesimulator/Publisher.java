package eyesimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A publisher that connects to a TCP server and sends click position data
 * from the Blackboard.
 *
 * @version 1.2
 * @authors
 * Monish Suresh
 * Christine Widden
 * Luca Ornstil
 */

public class Publisher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
    private final String host;
    private final int port;
    private volatile boolean running = true;
    private DataOutputStream outputStream;

    public Publisher(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void publish(String message) {
        try {
            if (outputStream != null) {
                outputStream.writeUTF(message);
                outputStream.flush();
                logger.debug("Published message: {}", message);
            }
        } catch (IOException e) {
            logger.error("Error sending message", e);
        }
    }

    public void stopPublisher() {
        running = false;
        logger.info("Publisher stopped.");
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {
            outputStream = new DataOutputStream(socket.getOutputStream());
            logger.info("Connected to TCP server at {}:{}", host, port);
            while (running) {
                Thread.sleep(1000);  // Keep the connection alive
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Publisher encountered an error", e);
        }
    }
}
