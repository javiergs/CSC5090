package app;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class is responsible for controlling the eye tracking simulation.
 *
 * @author Luca Ornstil,
 * @author Monish Suresh,
 * @author Christine Widden
 * @version 1.0
 */
public class Main extends JFrame implements ActionListener {
	
	private Publisher server;
	private Thread serverThread;
	private JMenuItem startMenuItem;
	private JMenuItem stopMenuItem;
	private JMenuItem configureMenuItem;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Eye Tracking Simulator");
		main.setSize(800, 600);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
	}
	
	public Main() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		startMenuItem = new JMenuItem("Start");
		stopMenuItem = new JMenuItem("Stop");
		configureMenuItem = new JMenuItem("Configure");
		menu.add(startMenuItem);
		menu.add(stopMenuItem);
		menu.add(configureMenuItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		startMenuItem.addActionListener(this);
		stopMenuItem.addActionListener(this);
		configureMenuItem.addActionListener(this);
		stopMenuItem.setEnabled(false);
		WorkArea workArea = new WorkArea();
		add(workArea);
		
		//Todo: probably start the server with a menu option not automatically in the constructor
		server = new Publisher();
		serverThread = new Thread(server);
		serverThread.start();
	}
 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startMenuItem) {
			startTracking();
		} else if (e.getSource() == stopMenuItem) {
			stopTracking();
		} else if (e.getSource() == configureMenuItem) {
			configureSettings();
		}
	}
	
	private void startTracking() {
		startMenuItem.setEnabled(false);
		stopMenuItem.setEnabled(true);
		configureMenuItem.setEnabled(false);
		Blackboard.getInstance().startTracking();  // Update tracking status on blackboard
		server.startTransmission();  // Start data transmission
	}
	
	private void stopTracking() {
		startMenuItem.setEnabled(true);
		stopMenuItem.setEnabled(false);
		configureMenuItem.setEnabled(true);
		Blackboard.getInstance().stopTracking();  // Stop tracking, but server keeps running
		server.stopTransmission();  // Stop transmission but don't shut down the server
	}
	
	private void configureSettings() {
		String widthStr = JOptionPane.showInputDialog(this, "Enter width:", "Configure", JOptionPane.QUESTION_MESSAGE);
		String heightStr = JOptionPane.showInputDialog(this, "Enter height:", "Configure", JOptionPane.QUESTION_MESSAGE);
		String speedStr = JOptionPane.showInputDialog(this, "Enter data transmission speed (frames per second):", "Configure", JOptionPane.QUESTION_MESSAGE);
		int width = Integer.parseInt(widthStr);
		int height = Integer.parseInt(heightStr);
		int speed = Integer.parseInt(speedStr);
		setSize(width, height);
		Blackboard.getInstance().setTransmissionSpeed(speed);  // Update the transmission speed in the blackboard
	}
 
}
