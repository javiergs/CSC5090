package tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to test the Publisher class.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		Publisher server = new Publisher(12345);
		new Thread(server).start();
		System.out.println("Press any key to stop the server");
		try {
			System.in.read();
			server.stop();
		} catch (Exception e) {
			logger.error("Error in MainTester", e);
		}
	}
	
}