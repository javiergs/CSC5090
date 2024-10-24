package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Server class that listens for incoming connections and sends random eye tracking data to the client.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class EyeTrackingServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EyeTrackingServer.class);
    
    public static void main(String[] args) {
		logger.info ("Eye Tracking Server Main");
		Random random = new Random();
		try (ServerSocket ss = new ServerSocket(6001);
				 Socket connection = ss.accept();
				 DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());) {
			logger.info("Eye Tracking Connection Made");
			while (true) {
				long startTime = System.currentTimeMillis();
				int x_pos = random.nextInt(1000);
				int y_pos = random.nextInt(950 - 200); // accommodate for top bar and pad accordingly
				outputStream.writeUTF(x_pos + ", " + y_pos);
				outputStream.flush();  // Ensure each packet is sent immediately
				long endTime = System.currentTimeMillis();
				logger.info("Sent eye tracking data: {}, {} in {}ms", x_pos, y_pos, endTime - startTime);
				Thread.sleep(500);  // Send data every 0.5 seconds
			}
			
		} catch (Exception e) {
			logger.error("Eye Tracking Sever: {}", e.getMessage());
		}
	}
 
	@Override
	public void run() {
		main(new String[0]);
	}

}
