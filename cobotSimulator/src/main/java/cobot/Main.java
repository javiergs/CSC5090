package cobot;

import javax.swing.*;
import java.awt.*;

/**
 * Main class to start the application.
 * This class is responsible for starting the server, setting up the GUI, and handling user input.
 * The GUI consists of a work area where the robot arms are drawn, and a menu bar
 * with options to start the client, toggle modes, and exit the application.
 *
 * @author Jack Ortega,
 * @author Neeraja Beesetti,
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Main extends JFrame {
	
	public Main() {
		setLayout(new BorderLayout());
		// size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int squareSize = (2 * screenSize.height) / 3;
		setSize(squareSize, squareSize);
		setLocationRelativeTo(null);
		// controller
		Controller controller = new Controller();
		addComponentListener(controller);
		// menu bar
		setJMenuBar(createMenuBar(controller));
		// status bar
		StatusBarPanel statusBarPanel = new StatusBarPanel("not started");
		add(statusBarPanel, BorderLayout.SOUTH);
		Blackboard.getInstance().addPropertyChangeListener(statusBarPanel);
		// work area
		CobotPanel cobotPanel = new CobotPanel();
		add(cobotPanel);
		Blackboard.getInstance().addPropertyChangeListener(cobotPanel);
	}
	
	private JMenuBar createMenuBar(Controller controller) {
		JMenu fileMenu = new JMenu("Options");
		// item - start
		JMenuItem connectItem = new JMenuItem("Start client");
		connectItem.addActionListener(controller);
		fileMenu.add(connectItem);
		// item - stop
		JMenuItem pauseItem = new JMenuItem("Stop client");
		pauseItem.addActionListener(controller);
		fileMenu.add(pauseItem);
		// item - exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(controller);
		fileMenu.add(exitItem);
		// menu bar
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