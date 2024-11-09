package cobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI consists of a simulation panel where the robot arms are drawn, and a button panel
 * with buttons to start and stop the simulation.
 */
public class Main extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static int PORT = 12345;
	
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		RobotPanelHandler simulationPanel = new RobotPanelHandler();
		RobotPanel robotPanel = new RobotPanel(simulationPanel);
		Blackboard.getInstance().addPropertyChangeListener(simulationPanel);

		ProgressBar progressBar = new ProgressBar();
		createMenuBar();

		add(robotPanel, BorderLayout.CENTER);
		add(progressBar, BorderLayout.SOUTH);

		logger.info("Starting publisher");
		Publisher publisher = new Publisher(PORT);
		Thread publisherThread = new Thread(publisher);
		publisherThread.start();
	}
	
	private void createMenuBar() {
		Controller controller = new Controller();
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu simulationMenu = new JMenu("Simulation");
		JMenuItem startMenuItem = new JMenuItem("Start client");
		startMenuItem.addActionListener(controller);
		JMenuItem stopMenuItem = new JMenuItem("Stop client");
		stopMenuItem.addActionListener(controller);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(controller);
		JMenuItem startSimulationMenuItem = new JMenuItem("Start simulation");
		startSimulationMenuItem.addActionListener(controller);
		JMenuItem stopSimulationMenuItem = new JMenuItem("Stop simulation");
		stopSimulationMenuItem.addActionListener(controller);
		JMenuItem pauseMenuItem = new JMenuItem("Pause");
		pauseMenuItem.addActionListener(controller);
		JMenuItem resumeMenuItem = new JMenuItem("Resume");
		resumeMenuItem.addActionListener(controller);
		fileMenu.add(startMenuItem);
		fileMenu.add(stopMenuItem);
		fileMenu.add(exitMenuItem);
		simulationMenu.add(startSimulationMenuItem);
		simulationMenu.add(stopSimulationMenuItem);
		simulationMenu.add(pauseMenuItem);
		simulationMenu.add(resumeMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(simulationMenu);
		setJMenuBar(menuBar);
	}
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("Cobot Simulator");
		frame.setSize(1000, 800);
		frame.setVisible(true);
	}
}
