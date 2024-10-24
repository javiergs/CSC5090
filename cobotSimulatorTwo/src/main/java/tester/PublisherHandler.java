package tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * ServerHandler class that sends random angles to a connected client every second.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class PublisherHandler implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private Socket socket;
	
	public PublisherHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		int angles[] = new int[6];
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				for (int i = 0; i < angles.length; i++) {
					angles[i] = (int) (Math.random() * 360);
				}
				logger.info("Sending angles to client: {},{},{},{},{},{}",
					angles[0], angles[1], angles[2], angles[3], angles[4], angles[5]);
				out.println(
					angles[0] + "," +
						angles[1] + "," +
						angles[2] + "," +
						angles[3] + "," +
						angles[4] + "," +
						angles[5]);
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			logger.error("Error in ServerHandler", ex);
		}
	}
	
}