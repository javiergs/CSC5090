package head;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class that listens for button clicks and calls the appropriate methods in the Client class.
 *
 * @author Javier Gonzalez-Sanchez
 *
 * @version 1.0
 */
public class Controller implements ActionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	private Server server;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start client")) {
			startClient();
		} else if (e.getActionCommand().equals("Stop client")) {
			pauseClient();
		} else if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
	}
	
	private void startClient() {
		logger.info("Starting server");
		server = new Server(8888);
		Thread serverThread = new Thread(server);
		serverThread.start();
		Blackboard.getInstance().addPropertyChangeListener(server);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!server.isReady()) {
			Blackboard.getInstance().updateStatusLabel("disconnected");
		} else {
			Blackboard.getInstance().updateStatusLabel("connected");
		}
	}
	
	private void pauseClient() {
		if (server.isReady()) {
			logger.info("Stopping server");
			server.stop();
		}
	}
	
}
