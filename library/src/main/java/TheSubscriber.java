import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 * This class is a common implementation of a subscriber
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */


public class TheSubscriber{
	private static final Logger logger = LoggerFactory.getLogger(TheSubscriber.class);
	private String ip;
	private int port;
	private boolean isConnected= false;
	private BufferedReader in;


	public TheSubscriber(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void connect(){
		try {
			Socket socket = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.info("TheSubscriber is running");
			isConnected = true;
		}
		catch (IOException e) {
			logger.error("I/O error in TheSubscriber: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error in TheSubscriber: {}", e.getMessage(), e);
		} finally {
			isConnected = false;
		}
	}

	public String readData(){
		if (!isConnected) {
			throw new RuntimeException("Client not connected");
		}

		try{
			String command = in.readLine();
			if (command != null) {
				return command;
			}
		} catch (IOException e) {
			logger.error("Error in Publisher", e);

		}
		return "";
	}


	public void disconnect() {
		isConnected = false;
	}

	

}