package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


/**
 * The Publisher class generates and sends random angles to a connected client. It listens for
 * a client connection on a specified port and continuously sends a set of six random angles every
 * 30 seconds until the client disconnects.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class Publisher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
    private final int port;
    private final static int WAIT_TIME = 30000;

    /**
     * Constructs a new Publisher with the specified port.
     *
     * @param port the port number to listen for client connections
     */
    public Publisher(int port) {
        this.port = port;
    }

    /**
     * Runs the Publisher, accepting client connections, generating random angles, and sending them
     * to the connected client. Waits for a new client if the current one disconnects.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Publisher started. Ready for clients to connect");
            Socket client = serverSocket.accept();
            logger.info("Client connected: " + client.toString());
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            Random rand = new Random();

            while (true) {
                logger.info("Randomly generating angles");
                StringBuilder angles = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    angles.append(rand.nextInt(360)).append(",");
                }
                angles.append(rand.nextInt(360));

                logger.info("Sending 6 angles: " + angles);
                out.println(angles);

                if (out.checkError()) {
                    logger.info("Client closed. Waiting for new one");
                    client = serverSocket.accept();
                    logger.info("New client found: " + client.toString());
                    out = new PrintWriter(client.getOutputStream(), true);
                }

                logger.info("Wait 30 seconds");
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException ignored) {
                }
                logger.info(("Wait finished"));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
