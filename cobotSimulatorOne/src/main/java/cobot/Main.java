package cobot;

import cobot.blackboard.Blackboard;

import javax.swing.*;
import java.awt.*;

/**
 * Main class to start the application.
 * This class is responsible for starting a subscriber and setting up a GUI
 * The GUI consists of a work area where the robot arms are drawn, and a menu bar
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
		// menuController
		ComponentController componentController = new ComponentController();
		addComponentListener(componentController);
		// menu bar
		MenuController menuController = new MenuController();
		setJMenuBar(createMenuBar(menuController));
		// status bar
		StatusBarPanel statusBarPanel = new StatusBarPanel("not started");
		add(statusBarPanel, BorderLayout.SOUTH);
		Blackboard.getInstance().addPropertyChangeListener(statusBarPanel);
		// work area
		CobotPanel cobotPanel = new CobotPanel();
		add(cobotPanel);
		Blackboard.getInstance().addPropertyChangeListener(cobotPanel);
	}
	
	private JMenuBar createMenuBar(MenuController menuController) {
		JMenu fileMenu = new JMenu("Options");
		// item - start
		JMenuItem connectItem = new JMenuItem("Start client");
		connectItem.addActionListener(menuController);
		fileMenu.add(connectItem);
		// item - stop
		JMenuItem pauseItem = new JMenuItem("Stop client");
		pauseItem.addActionListener(menuController);
		fileMenu.add(pauseItem);
		// item - exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(menuController);
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