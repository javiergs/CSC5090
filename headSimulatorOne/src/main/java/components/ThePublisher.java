package components;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThePublisher implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(ThePublisher.class);
	private int port;
	private boolean running = true;
	
	public ThePublisher(int port) {
		this.port = port;
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);

			logger.info("Server is Running");
			while (running) {
				Socket socket = serverSocket.accept();
				Encoder publisherEncoder = new Encoder(socket);
				new Thread(publisherEncoder).start();
			}
			logger.info("Server Stopped Running");
		} catch (IOException e) {
			logger.error("Error  Publisher", e);
		}
	}
	
}
