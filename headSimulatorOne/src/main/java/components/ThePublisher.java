package components;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThePublisher{
	
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
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
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
