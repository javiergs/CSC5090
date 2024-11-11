package app.library;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * app.library.Main class is responsible for controlling the eye tracking simulation.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Main extends JFrame {

	private Server server;
	private TheSubscriber subscriber;
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private String subscriberType;

	public Main() {
		server = new Server();
		setLayout(new BorderLayout());

		JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		String[] options = {"Server", "Start", "Stop"};
		JComboBox<String> dropdownMenu = new JComboBox<>(options);
		dropdownPanel.add(dropdownMenu);
		add(dropdownPanel, BorderLayout.NORTH);

		Blackboard blackboard = new Blackboard();
		TrackArea area = new TrackArea(server, dropdownMenu, blackboard);
		add(area, BorderLayout.CENTER);

		Controller c = new Controller(area, server, dropdownMenu);
		dropdownMenu.addActionListener(c);

		blackboard.setDrawingState("Updated TrackArea");

		DataRepository destination = DataRepository.getInstance();

		// Default for now
		subscriberType = "tcp";


		if (subscriberType.equals("tcp")) {
			// Initialize TheSubscriber
			try {
				String ipHost = "127.0.0.1"; // Example IP, replace with actual if needed
				int port = 9090;             // Example port, replace with actual if needed
				String dataPrefix = "PointData";
				subscriber = new TheSubscriber(ipHost, port, dataPrefix, DataRepository.getInstance());
				new Thread(subscriber).start();
				logger.info("TheSubscriber initialized and started.");
			} catch (IOException e) {
				logger.error("Failed to initialize TheSubscriber: " + e.getMessage());
			}

		} else if (subscriberType.equals("mqtt")) {
			String broker = "tcp://broker.hivemq.com:1883";
			String clientID = "SubscriberClient";

			// Define topic and prefix pairs
			Map<String, String> topicAndPrefixPairs = new HashMap<>();
			topicAndPrefixPairs.put("device/coords", "XY");
//			topicAndPrefixPairs.put("device/direction", "HUMID");
			try {
				// Instantiate the subscriber
				TheSubscriberMQTT subscriber = new TheSubscriberMQTT(broker, clientID, topicAndPrefixPairs, destination);

				// Run the subscriber in a separate thread
				Thread subscriberThread = new Thread(subscriber);
				subscriberThread.start();
				logger.info("TheSubscriber initialized and started.");

				// Optionally, stop the subscriber after a certain condition
				// subscriber.stopSubscriber();
				// subscriberThread.join();

			} catch (MqttException e) {
				System.out.println("An error occurred while initializing the subscriber: " + e.getMessage());
			}
        }else{
			logger.error("Unknown subscriber type");
		}

	}

	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle("Eye Tracker Simulator");
		main.setSize(800, 600);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		logger.info("Eye Tracker Simulator application started.");
	}

	@Override
	public void dispose() {
		super.dispose();
		if (subscriber != null) {
			subscriber.stopSubscriber();
			logger.info("TheSubscriber stopped.");
		}
	}
}