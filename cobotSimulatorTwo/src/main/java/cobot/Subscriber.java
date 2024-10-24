package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The Server class is responsible for receiving the angles of the robot from the client.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Subscriber implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	private String ip;
	private int port;
	private boolean running = false;
	
	public Subscriber(String ip, int port) {
		this.ip = ip;
		this.port = port;
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
					parse(command);
				}
			}
			logger.info("Subscriber is stopping");
			socket.close();
		} catch (IOException e) {
			logger.error("I/O error in Subscriber: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error in Subscriber: {}", e.getMessage(), e);
		} finally {
			running = false;
		}
	}
	
	public void stop() {
		running = false;
	}
	
	private void parse(String command) {
		String[] tokens = command.split(",");
		try {
			int[] numbers = new int[6];
			for (int i = 0; i < 6; i++) {
				numbers[i] = Integer.parseInt(tokens[i]);
			}
			Blackboard.getInstance().setAngles(numbers);
		} catch (NumberFormatException e) {
			logger.error("Error parsing command", e);
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
}