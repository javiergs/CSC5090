package head;

import components.ThePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import components.Encoder;

/**
 * This class is a Server that connects to a port and publishes point data to it	
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */

public class Server implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	private boolean isReady = false;
	private int port;
	private Encoder encoder; 
	
	public Server(int port) {
		this.port = port;
		this.encoder = new Encoder();
		head.Blackboard.getInstance().addPropertyChangeListener(encoder);
	}
	
	@Override
	public void run() {
		try {
			ThePublisher publisher = new ThePublisher(port);
			publisher.connect();
			isReady = true;
			while (isReady) {
				try {
					// get data from encoder which gets it form black board and then "formats" it 
					String point = encoder.getData();
					Thread.sleep(1000 / 30);
					if (point == null)  continue;
					publisher.publish(point);
				} catch (Exception e) {
					logger.error("Error in Server: {}", e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error in Server: {}", e.getMessage(), e);
		} finally {
			isReady = false;
		}
	}
	
	public void stop() {
		isReady = false;
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	
}