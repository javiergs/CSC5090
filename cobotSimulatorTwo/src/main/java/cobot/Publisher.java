package cobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Publisher implements Runnable{
    private static final Logger logger =
            LoggerFactory.getLogger(Publisher.class);
    private final int port;

    //Wait time can be adjusted as necessary, just to stop from spamming client
    private final static int WAIT_TIME = 5000;

    public Publisher(int port) {
        this.port = port;
    }
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

                logger.info("Wait 5 seconds");
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException ignored) {}
                logger.info(("Wait finished"));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
