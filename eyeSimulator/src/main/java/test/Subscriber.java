package test;

import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * test.Client class that connects to a server and reads a list of click positions from the server.
 * The client retries connecting to the server a few times before failing.
 *
 * @author Luca Ornstil,
 * @author Monish Suresh,
 * @author Christine Widden
 * @version 1.0
 */
public class Subscriber {
 
	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream objectInputStream = null;
		int maxRetries = 5;  // Maximum number of retry attempts
		int retryInterval = 2000;  // Wait time between retries in milliseconds (2 seconds)
		int attempts = 0;
		while (attempts < maxRetries) {
			try {
				// Try to connect to the server
				socket = new Socket("localhost", 12345);  // Connect to server on port 12345
				System.out.println("Connected to server.");
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				break;  // If connected, exit the loop
			} catch (ConnectException e) {
				attempts++;
				System.err.println("Failed to connect to the server. Attempt " + attempts + " of " + maxRetries);
				if (attempts < maxRetries) {
					System.err.println("Retrying in " + (retryInterval / 1000) + " seconds...");
					try {
						Thread.sleep(retryInterval);  // Wait before retrying
					} catch (InterruptedException ie) {
						System.err.println("Retry interrupted.");
					}
				} else {
					System.err.println("Could not connect to the server after " + maxRetries + " attempts. Exiting.");
					return;  // Exit the program after max retries
				}
			} catch (IOException e) {
				System.err.println("An I/O error occurred: " + e.getMessage());
				e.printStackTrace();
				return;  // Exit if there's an I/O error
			}
		}
		
		try {
			// Process incoming data from the server
			while (true) {
				try {
					// Read the list of click positions from the server
					List<Point> clicks = (List<Point>) objectInputStream.readObject();
					System.out.println("Received clicks from server: " + clicks);
				} catch (EOFException eof) {
					// app.Server has closed the connection
					System.out.println("app.Server closed connection. Exiting.");
					break;
				} catch (SocketException se) {
					// Handle the case when the connection is reset (e.g., server closes the socket)
					System.err.println("Connection with the server was reset. Exiting.");
					break;
				} catch (ClassNotFoundException e) {
					System.err.println("Class not found while reading data from server.");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.err.println("Error while communicating with the server: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Clean up resources
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				System.err.println("Error while closing resources: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
 
}
