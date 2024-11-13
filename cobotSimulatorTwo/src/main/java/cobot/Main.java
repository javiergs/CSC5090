package cobot;

import CobotSimulatorTwo.Publisher;
import CobotSimulatorTwo.Blackboard;
import CobotSimulatorTwo.RobotPanel;
import CobotSimulatorTwo.RobotPanelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI consists of a simulation panel where the robot arms are drawn, and a button panel
 * with buttons to start and stop the simulation.
 */
public class Main extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static final int PORT = 12345;

    /**
     * The Main class is the entry point for the Cobot Simulator application. It creates the main
     * GUI frame consisting of a simulation panel for robot arms, a progress bar, and a menu bar with
     * options to start and stop both the client and simulation. Extends JFrame to display the GUI window.
     *
     * @author Reza Mousakhani
     * @author Damian Dhesi
     * @author Shiv Panchal
     * @author Javier Gonzalez-Sanchez
     * @version 2.0
     */
    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        RobotPanelHandler simulationPanel = new RobotPanelHandler();
        RobotPanel robotPanel = new RobotPanel(simulationPanel);
        Blackboard.getInstance().addPropertyChangeListener(simulationPanel);

        ProgressBar progressBar = new ProgressBar();
        createMenuBar(simulationPanel);

        add(robotPanel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        LOGGER.info("Starting publisher");
        Publisher publisher = new Publisher(PORT);
        Thread publisherThread = new Thread(publisher);
        publisherThread.start();
    }

    /**
     * Creates and sets up the menu bar with items for controlling the client and simulation.
     * Adds "File" and "Simulation" menus with actions for starting, stopping, pausing,
     * and resuming the simulation and client connection.
     */
    private void createMenuBar(RobotPanelHandler handler) {
        Controller controller = new Controller(handler);
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

    /**
     * Main method to launch the Cobot Simulator application. Sets up and displays the main frame.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Main frame = new Main();
        frame.setTitle("Cobot Simulator");
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}