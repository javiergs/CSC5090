package app.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The `TheSubscriber` class establishes a TCP connection to a server and continuously
 * receives data, which it then forwards to a `DataDestination` for processing. It
 * prepends a predefined prefix to the received data to identify its source.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class TheSubscriber implements Runnable{

    private final Logger log = LoggerFactory.getLogger(TheSubscriber.class.getName());
    private final DataInputStream inputStream;
    private final String dataPrefix;
    private final DataDestination dataDestination;
    private static final String PREFIX_DELIMITER = "~";
    private boolean running = true;

    /**
     * Constructs a `TheSubscriber`.
     *
     * @param ip_host The IP address of the server.
     * @param ip_port The port number of the server.
     * @param dataPrefix The prefix to be added to the received data.
     * @param destination The `DataDestination` where data will be sent.
     * @throws IOException If an error occurs during socket creation or connection.
     */
    public TheSubscriber(String ip_host, int ip_port, String dataPrefix, DataDestination destination) throws IOException {
        this.dataPrefix = dataPrefix + PREFIX_DELIMITER;
        this.dataDestination = destination;
        try {
            Socket socket = new Socket(ip_host, ip_port);
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.warn("Unable to connect to sever --" + e.getMessage());
            throw e;
        }
    }

    /**
     * Continuously receives data from the server and forwards it to the `DataDestination`.
     */
    @Override
    public void run() {
        try {
            while (running) {
                long startTime = System.currentTimeMillis();
                String str = inputStream.readUTF();
                dataDestination.addSubscriberData( dataPrefix + str);
                long endTime = System.currentTimeMillis();
                log.info("Received data: " + str + " in " + (endTime - startTime) + "ms");
            }
        } catch (IOException ex) {
            dataDestination.alertError(dataPrefix + ex.getMessage());
            log.warn("Error with server connection - " + ex.getMessage());
        }

    }

    /**
     * Stops the subscriber and closes the input stream.
     */
    public void stopSubscriber() {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("Error closing stream - " + e.getMessage());
        }
        running = false;
    }
}