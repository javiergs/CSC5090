package affectSimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyChangeSupport;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Subscriber extends PropertyChangeSupport implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private final int port;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public Subscriber(int port) {
        super(new Object());
        this.port = port;
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            logger.error("Error stopping Subscriber", e);
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Subscriber is listening on port {}", port);

            while (running) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
                    logger.info("Connected to Publisher.");
                    while (running) {
                        String message = inputStream.readUTF();
                        logger.debug("Received message: {}", message);
                        firePropertyChange("someProperty", null, message);
                    }
                } catch (IOException e) {
                    if (running) {
                        logger.error("Error in Subscriber", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error initializing Subscriber", e);
        }
    }
}
