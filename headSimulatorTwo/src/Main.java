import javax.swing.*;
import java.awt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(Main.class);


	public Main() {
		server = new Server();
		setLayout(new BorderLayout());

		JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		String[] options = {"Server", "Start", "Stop"};
		JComboBox<String> dropdownMenu = new JComboBox<>(options);
		dropdownPanel.add(dropdownMenu);
		add(dropdownPanel, BorderLayout.NORTH);

		Blackboard blackboard = new Blackboard();  // Create Blackboard instance

		TrackArea area = new TrackArea(server, dropdownMenu, blackboard);
		add(area, BorderLayout.CENTER);

		Controller c = new Controller(area, server, dropdownMenu);
		dropdownMenu.addActionListener(c);

		blackboard.setDrawingState("Updated TrackArea");  // Example update to Blackboard
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Eye Tracker Simulator");
		main.setSize(800, 600);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		logger.info("Eye Tracker Simulator application started.");

	}
}