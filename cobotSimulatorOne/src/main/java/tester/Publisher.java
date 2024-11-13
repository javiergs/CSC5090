package tester;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server class that listens for incoming connections and creates a new PublisherHandler thread for each connection.
 */
public class Publisher implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private int port;
	private boolean running = true;
	private List<PublisherHandler> handlers;

	public Publisher(int port) {
		this.port = port;
		this.handlers = new ArrayList<>();
	}

	public void stop() {
		running = false;
	}

	public void sendMessageToAll(String encodedMessage) {
		for (PublisherHandler handler : handlers) {
			handler.sendEncodedMessage(encodedMessage);
		}
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("Server started on port {}", port);
			while (running) {
				Socket socket = serverSocket.accept();
				PublisherHandler handler = new PublisherHandler(socket);
				handlers.add(handler);
				new Thread(handler).start();
				logger.info("New client connected, handler started");
			}
			logger.info("Server stopped");
		} catch (IOException e) {
			logger.error("Error in Publisher", e);
		}
	}
}
