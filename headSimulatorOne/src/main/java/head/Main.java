package head;

import javax.swing.*;
import java.awt.*;

import components.ThePublisher;

/**
 * This class creates the main window of the application
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 *
 * @version 2.0
 */
public class Main extends JFrame {
	
	public Main() {
		setJMenuBar(createMenuBar());
		setLayout(new GridLayout(1, 1));

		Canvas d = new Canvas();
		add(d);
		this.createPublisher();
	}
	
	private JMenuBar createMenuBar() {
		Controller controller = new Controller();
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem start = new JMenuItem("Start client");
		JMenuItem stop = new JMenuItem("Stop client");
		menu.add(start);
		menu.add(stop);
		menuBar.add(menu);
		start.addActionListener(controller);
		stop.addActionListener(controller);
		return menuBar;
	}

	private void createPublisher() {
		ThePublisher myPublisher = new ThePublisher(5);
		Thread pubThread = new Thread(myPublisher);
		pubThread.start();

	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Head Movement Simulator");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(800, 600);
		main.setResizable(false);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
	
}