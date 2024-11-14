package affectTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
public class TheSubscriber extends PropertyChangeSupport implements Runnable{

    private final Logger log = LoggerFactory.getLogger(TheSubscriber.class.getName());
    private final DataInputStream inputStream;
    private final String dataPrefixWithDelim;
    public static final String CLIENT_PROPERTY_LABEL = "addClientData";
    public static final String REPORT_ERROR_LABEL = "reportSubscriberError";
    private static final String PREFIX_DELIMITER = "~";
    private boolean running = true;
    public TheSubscriber(String ip_host, int ip_port, String dataPrefix, PropertyChangeListener listener) throws IOException {
        super(new Object());
        this.dataPrefixWithDelim = dataPrefix + PREFIX_DELIMITER;
        try {
            Socket socket = new Socket(ip_host, ip_port);
            inputStream = new DataInputStream(socket.getInputStream());
            this.addPropertyChangeListener(CLIENT_PROPERTY_LABEL, listener);
            this.addPropertyChangeListener(REPORT_ERROR_LABEL, listener);
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
                firePropertyChange(CLIENT_PROPERTY_LABEL, null , dataPrefixWithDelim + str );
                long endTime = System.currentTimeMillis();
                log.info("Received data: " + str + " in " + (endTime - startTime) + "ms");
            }
        } catch (IOException ex) {
            if (running) {
                firePropertyChange(REPORT_ERROR_LABEL, null, dataPrefixWithDelim + ex.getMessage());
            }
            log.warn("Error with server connection - " + ex.getMessage());
        }

    }
    public void stopSubscriber() {
        try {
            log.debug("Stopping the Subscriber");
            inputStream.close();
        } catch (IOException e) {
            log.error("Error closing stream - " + e.getMessage());
        }
        running = false;
    }
}
