package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Server class that listens for incoming connections and sends random emotion data to the client.
 * The data is sent every 0.5 seconds.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class EmotionDataServer implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(EmotionDataServer.class);
	
	public static void main(String[] args) {
		logger.info("Emotion Server Main");
		Random random = new Random();
		try (ServerSocket ss = new ServerSocket(6000);
				 Socket connection = ss.accept();
				 DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
			logger.info("Emotion Connection Made");
			while (true) {
				long startTime = System.currentTimeMillis();
				float v1 = random.nextFloat();
				float v2 = random.nextFloat();
				float v3 = random.nextFloat();
				float v4 = random.nextFloat();
				float v5 = random.nextFloat();
				outputStream.writeUTF(v1 + ", " + v2 + ", " + v3 + ", " + v4 + ", " + v5);
				outputStream.flush();
				long endTime = System.currentTimeMillis();
				logger.info("Sent emotion data: {}, {}, {}, {}, {} in {}ms", v1, v2, v3, v4, v5, endTime - startTime);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			logger.error("Emotion Server: {}", e.getMessage());
		}
	}
	
	@Override
	public void run() {
		main(new String[0]);
	}
}
