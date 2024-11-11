package cobot;

import cobot.blackboard.Blackboard;

import javax.swing.*;
import java.awt.*;

/**
 * Main class to start the application.
 * This class is responsible for starting a subscriber and setting up a GUI
 * The GUI consists of a work area where the robot arms are drawn, and a menu bar.
 *
 * Author(s): Jack Ortega, Neeraja Beesetti, Saanvi Dua, Javier Gonzalez-Sanchez
 * Version: 2.0
 */
public class Main extends JFrame {
	private Subscriber subscriber;
	private Thread subscriberThread;
	private MQTTSubscriber mqttSubscriber;

	public Main() {
		setLayout(new BorderLayout());

		// Set the window size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int squareSize = (2 * screenSize.height) / 3;
		setSize(squareSize, squareSize);
		setLocationRelativeTo(null);

		// Initialize and add ComponentController
		ComponentController componentController = new ComponentController();
		addComponentListener(componentController);

		// Set up the menu bar with menuController
		MenuController menuController = new MenuController();  // Pass main frame reference
		setJMenuBar(createMenuBar(menuController));

		// Add a status bar panel
		StatusBarPanel statusBarPanel = new StatusBarPanel("Not started");
		add(statusBarPanel, BorderLayout.SOUTH);
		Blackboard.getInstance().addPropertyChangeListener(statusBarPanel);

		// Add the work area panel for the cobot
		CobotPanel cobotPanel = new CobotPanel();
		add(cobotPanel);
		Blackboard.getInstance().addPropertyChangeListener(cobotPanel);
	}

	private JMenuBar createMenuBar(MenuController menuController) {
		JMenu fileMenu = new JMenu("Options");

		// Start client item
		JMenuItem connectItem = new JMenuItem("Start client");
		connectItem.addActionListener(menuController);
		fileMenu.add(connectItem);

		// Start MQTT client item
		JMenuItem mqttConnectItem = new JMenuItem("Start MQTT client");
		mqttConnectItem.addActionListener(menuController);
		fileMenu.add(mqttConnectItem);

		// Stop client item
		JMenuItem pauseItem = new JMenuItem("Stop client");
		pauseItem.addActionListener(menuController);
		fileMenu.add(pauseItem);

		// Exit item
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(menuController);
		fileMenu.add(exitItem);

		// Create and return the menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		return menuBar;
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Cobot Simulator");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
	}
}
