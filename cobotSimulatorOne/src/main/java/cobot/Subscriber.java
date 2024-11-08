package cobot;

import blackboard.Blackboard;
import encoder.EncoderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Client class to connect to the server and receive commands.
 *
 * @author Jack Ortega,
 * @author Neeraja Beesetti,
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Subscriber implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	private String ip;
	private int port;
	private boolean running = false;
	private EncoderHelper encoderHelper;
	
	public Subscriber(String ip, int port, EncoderHelper encoderHelper) {
		this.ip = ip;
		this.port = port;
		this.encoderHelper = encoderHelper;
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
					Blackboard.getInstance().updateArmAngles(encoderHelper.parse(command));
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

	public boolean isRunning() {
		return running;
	}
	
}