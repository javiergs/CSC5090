

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is the common implementation of a publisher using a socket. 
 * It has methods for connecting/disconnecting to a port and publishing data to an output stream.
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 3.0
 */
	

	
public class ThePublisher {
	
	private static final Logger logger = LoggerFactory.getLogger(ThePublisher.class);
	private int port;
	private ObjectOutputStream outputStream;
	private boolean isConnected = false;


	public ThePublisher(int port) {
		this.port = port;
	}

	public void disconnect() {
		isConnected = false;
	}

	public void connect() {
		try {
			ServerSocket serverSocket = new ServerSocket(port); 
			logger.info("Server is waiting for connections");
			Socket clientSocket = serverSocket.accept(); 
			logger.info("Client connected");
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream()); //Create stream to specified port
			isConnected = true;
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public void publish(Object obj){
		if (!isConnected) {
			throw new RuntimeException("Client not connected");
		}
		try {
			outputStream.writeObject(obj);
			outputStream.flush();
		} catch (IOException e) {
			logger.error("Error in Publisher", e);
		}

	}
}
