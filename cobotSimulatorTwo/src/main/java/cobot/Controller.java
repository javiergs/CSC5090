package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Controller class is responsible for controlling the simulation of the robot arm.
 */
public class Controller implements ActionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	private Subscriber subscriber;
	
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
		logger.info("Starting subscriber");
		subscriber = new Subscriber("localhost", 12345);
		Thread subscriberThread = new Thread(subscriber);
		subscriberThread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("Error in thread sleep: {}", e.getMessage(), e);
		}
		if (!subscriber.isRunning()) {
			JOptionPane.showMessageDialog(null, "Could not connect to server", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Connected to server", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void pauseClient() {
		if (subscriber.isRunning()) {
			logger.info("Stopping subscriber");
			subscriber.stop();
		}
	}

}