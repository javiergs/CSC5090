package tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class that listens for incoming connections and creates a new ServerHandler thread for each connection.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Publisher implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private int port;
	private boolean running = true;
	
	public Publisher(int port) {
		this.port = port;
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			logger.info("Server started");
			while (running) {
				Socket socket = serverSocket.accept();
				PublisherHandler publisherHandler = new PublisherHandler(socket);
				new Thread(publisherHandler).start();
			}
			logger.info("Server stopped");
		} catch (IOException e) {
			logger.error("Error in Publisher", e);
		}
	}
	
}