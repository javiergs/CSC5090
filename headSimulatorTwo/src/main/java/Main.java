import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class is responsible for controlling the eye tracking simulation.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Main extends JFrame {
	
	private Server server;
	
	public Main() {
		//todo: initialize the server as a menu option not automatically in the constructor
		
		server = new Server(); // Initialize the Server
		setLayout(new BorderLayout());
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		start.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
		stop.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
		start.setPreferredSize(new Dimension(100, 50));
		stop.setPreferredSize(new Dimension(100, 50));
		JPanel buttonPanel = new JPanel(); // To hold the buttons
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(start);
		buttonPanel.add(stop);
		add(buttonPanel, BorderLayout.NORTH); // Place buttons at the top
		TrackArea area = new TrackArea(server); // Pass server to TrackArea
		add(area, BorderLayout.CENTER); // Place TrackArea in the center
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.start();
			}
		});
		
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.stop();
			}
		});
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Eye Tracker Simulator");
		main.setSize(800, 600);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
	}
	
}
