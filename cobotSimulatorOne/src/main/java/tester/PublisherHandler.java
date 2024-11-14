package tester;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles each connection from the Publisher, sending pre-encoded data.
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class PublisherHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PublisherHandler.class);
	private Socket socket;
	private PrintWriter out;
	private BlockingQueue<String> messageQueue;

	public PublisherHandler(Socket socket) {
		this.socket = socket;
		this.messageQueue = new LinkedBlockingQueue<>();
	}

	public void sendEncodedMessage(String encodedMessage) {
		messageQueue.offer(encodedMessage);
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String message = messageQueue.take(); // Block until a message is available
				out.println(message);
				logger.info("Sent message to client: {}", message);
			}
		} catch (IOException e) {
			logger.error("Error in PublisherHandler output stream", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("PublisherHandler interrupted", e);
		}
	}
}
