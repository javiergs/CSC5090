package cobot;

import CobotSimulatorTwo.Subscriber;
import CobotSimulatorTwo.RobotPanelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Controller class is responsible for managing actions related to the robot arm simulation.
 * It listens for user commands to start and stop both the client connection and the simulation.
 * Implements ActionListener to handle action events from the UI.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Controller implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private Subscriber subscriber = null;
    private final RobotPanelHandler ROBOT_PANEL_HANDLER;

    public Controller(RobotPanelHandler robotPanelHandler) {
        this.ROBOT_PANEL_HANDLER = robotPanelHandler;
    }

    /**
     * Handles action events based on the command specified in the ActionEvent.
     *
     * @param e the ActionEvent triggered by user interaction
     */
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

    /**
     * Initializes and starts the client by creating a Subscriber instance and attempting to
     * connect to the server. Displays a message indicating the success or failure of the connection.
     */
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

    /**
     * Pauses or stops the client by stopping the Subscriber instance if it is currently running,
     * disconnecting from the server.
     */
    private void pauseClient() {
        if (subscriber.isRunning()) {
            LOGGER.info("Stopping subscriber");
            subscriber.stop();
        }
    }

    /**
     * Starts the simulation of the robot arm by invoking the startSimulation method on the
     * RobotPanelHandler instance.
     */
    private void startSimulation() {
        LOGGER.info("Starting simulation");
        ROBOT_PANEL_HANDLER.startSimulation();
    }

    /**
     * Stops the simulation of the robot arm by invoking the stopSimulation method on the
     * RobotPanelHandler instance.
     */
    private void stopSimulation() {
        LOGGER.info("Stopping simulation");
        ROBOT_PANEL_HANDLER.stopSimulation();
    }

}