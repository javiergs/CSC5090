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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
	private Subscriber subscriber = null;
	private final RobotPanelHandler ROBOT_PANEL_HANDLER;

    public Controller(RobotPanelHandler robotPanelHandler) {
        this.ROBOT_PANEL_HANDLER = robotPanelHandler;
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Start client":
				startClient();
				break;
			case "Stop client":
				pauseClient();
				break;
			case "Exit":
				System.exit(0);
				break;
			case "Start simulation":
				startSimulation();
				break;
			case "Stop simulation":
				stopSimulation();
				break;
            default:
				LOGGER.error("Unexpected action event: " + e.getActionCommand());
		}
	}
	
	private void startClient() {
		LOGGER.info("Starting subscriber");
		subscriber = new Subscriber("localhost", Main.PORT);
		Thread subscriberThread = new Thread(subscriber);
		subscriberThread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOGGER.error("Error in thread sleep: {}", e.getMessage(), e);
		}
		if (!subscriber.isRunning()) {
			JOptionPane.showMessageDialog(null, "Could not connect to server", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Connected to server", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void pauseClient() {
		if (subscriber.isRunning()) {
			LOGGER.info("Stopping subscriber");
			subscriber.stop();
		}
	}

	private void startSimulation() {
		LOGGER.info("Starting simulation");
		ROBOT_PANEL_HANDLER.startSimulation();
	}

	private void stopSimulation() {
		LOGGER.info("Stopping simulation");
		ROBOT_PANEL_HANDLER.stopSimulation();
	}

}