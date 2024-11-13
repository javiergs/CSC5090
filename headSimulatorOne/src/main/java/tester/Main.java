package tester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Client class that connects to the server and listens for incoming data.
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 8888;
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("Connected to the server");
			while (true) {
				String p = (String) inputStream.readObject();
				System.out.println(p);
			}
		} catch (IOException e) {
			logger.error("I/O error: {}", e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error("Class not found: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage(), e);
		}
	}
	
}