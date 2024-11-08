package app.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

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
                dataDestination.addSubscriberData(str);
                long endTime = System.currentTimeMillis();
                log.info("Received data: " + str + " in " + (endTime - startTime) + "ms");
            }
        } catch (IOException ex) {
            dataDestination.alertError(dataPrefix + ex.getMessage());
            log.warn("Error with server connection - " + ex.getMessage());
        }

    }
    public void stopSubscriber() {
        running = false;
    }
}
