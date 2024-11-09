package components;

import components.ThePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;


public class Encoder implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ThePublisher.class);
	private Socket socket;

	public Encoder(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		int mouse[] = new int[2];
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				for (int i = 0; i < mouse.length; i++) {
					mouse[i] = (int) (Math.random() * 400);
				}
				logger.info("Sending mousepos to client: {},{}",
					mouse[0], mouse[1]);
				out.println();
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			logger.error("Error in ServerHandler", ex);
		}
	}

}
