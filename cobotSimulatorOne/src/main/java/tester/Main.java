package tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to test the Publisher class.
 *
 * @author Javier Gonzalez-Sanchez
 *
 * @version 1.0
 */
public class Main {

	public static final int PORT = 12345;
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		Publisher server = new Publisher(PORT);
		new Thread(server).start();
		System.out.println("Enter any key to stop the server");

		Thread stopServerThread = new Thread(() -> {
			try {
				System.in.read();
				server.stop();
				System.out.println("Server stopped.");
				System.exit(0);

			} catch (Exception e) {
				logger.error("MainTester: Error in stopping server", e);
			}
		});

		stopServerThread.start();
	}
	
}