package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Client class to connect to the server and receive commands.
 */
public class Subscriber implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	private String ip;
	private int port;
	private boolean running = false;
	private Consumer<String> messageConsumer;

	public Subscriber(String ip, int port, Consumer<String> messageConsumer) {
		this.ip = ip;
		this.port = port;
		this.messageConsumer = messageConsumer;
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(ip, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.info("Subscriber is running");
			running = true;
			while (running) {
				String command = in.readLine();
				if (command != null) {
					messageConsumer.accept(command);  // Notify Blackboard through callback
				}
			}
			logger.info("Subscriber is stopping");
			socket.close();
		} catch (IOException e) {
			logger.error("I/O error in Subscriber: {}", e.getMessage(), e);
		} finally {
			running = false;
		}
	}

	public void stop() {
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
