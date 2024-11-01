package app.Controller;

import app.Model.Blackboard;
import app.View.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code MainController} class serves as an event handler for UI actions, implementing
 * the {@link ActionListener} interface. It handles "Start" and "Stop" actions by interacting
 * with the {@link Blackboard} to start and stop data retrieval.
 * <p>
 * This controller listens for user actions (such as button clicks) and triggers the appropriate
 * data retrieval methods in the {@code Blackboard} instance based on the action command.
 * /**
 * The Blackboard class is a singleton class that stores the angles of the robot.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class MainController implements ActionListener {
	
	private static final Logger controllerLog = LoggerFactory.getLogger(MainController.class.getName());
	private final Main parent;
	
	
	public MainController(Main parent) {
		this.parent = parent;
	}
 
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case ("Start") -> {
				controllerLog.info(String.format("Connection attempted with:\n%s",
					Blackboard.getInstance().getFormattedConnectionSettings()));
				parent.connectClients();
			}
			case ("Stop") -> {
				controllerLog.info("Stop Pressed. Disconnecting.");
				parent.cleanUpThreads();
			}
		}
	}
 
}
