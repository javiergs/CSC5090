package head;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class handles the actions of the buttons in the GUI, starting and stopping the client
 * Publishes data to the MQTT server and the server
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */


public class Controller implements ActionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	private Server server;
	private MQTTServer mqttServer;

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
		logger.info("Starting MQTT server");
		mqttServer = new MQTTServer("tcp://test.mosquitto.org:1883", "TestPublisher", "test/topic");
		Thread mqttServerThread = new Thread(mqttServer);
		mqttServerThread.start();
		logger.info("Starting server");
		server = new Server(8888);
		Thread serverThread = new Thread(server);
		serverThread.start();


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("Error in Controller", e);
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
