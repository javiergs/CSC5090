package affectTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Attempts to connect to a caller defined IP address host and port.
 * The connection is attempted within the constructor so that the connection is sure to exist before it is
 * run as a thread.
 * <p>
 * A caller defined prefix will be added to the data received from the server for parsing
 * when sending the data to its destination.
 *
 * <p>
 * When running within a Thread, TheSubscriber is reading in data from the server, attaching the prefix
 * and sending it to its destination.
 */
public class TheSubscriber implements Runnable{

    private final Logger log = LoggerFactory.getLogger(TheSubscriber.class.getName());
    private final DataInputStream inputStream;
    private final String dataPrefix;
    private final DataDestination dataDestination;
    private static final String PREFIX_DELIMITER = "~";
    private boolean running = true;
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
    public void stopSubscriber() {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("Error closing stream - " + e.getMessage());
        }
        running = false;
    }
}
