package app.View;

import app.Controller.MainController;
import app.Model.*;
import test.EmotionDataServer;
import test.EyeTrackingServer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The {@code Main} class serves as the entry point for the Eye Tracking & Emotion Hub application.
 * It sets up the main window, initializes the user interface components, and starts the necessary threads
 * for data retrieval, processing, and visualization.
 * <p>
 * The application displays a user interface with a panel for adjusting preferences, a draw panel for
 * visualizing circles representing emotion and eye-tracking data, and a key panel explaining the color-coded emotions.
 * <p>
 * Main also acts as the default factory for necessary components.
 * <p>
 * If run with the "-test" flag, this class will also start the test servers for emotion and eye-tracking data.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 */
public class Main extends JFrame implements PropertyChangeListener {
	
	private static final String TESTING_FLAG = "-test";
	private final ArrayList<CustomThread> threads;
	
	public Main() {
		threads = new ArrayList<>();
		setLayout(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		JMenuItem start = new JMenuItem("Start");
		JMenuItem stop = new JMenuItem("Stop");
		menuBar.add(actionsMenu);
		actionsMenu.add(start);
		actionsMenu.add(stop);
		setJMenuBar(menuBar);
		MainController controller = new MainController(this);
		start.addActionListener(controller);
		stop.addActionListener(controller);
		DrawPanel drawPanel = new DrawPanel();
		drawPanel.setPreferredSize(new Dimension(1000, 1000));
		add(drawPanel, BorderLayout.CENTER);
		PreferencePanel preferencePanel = new PreferencePanel();
		add(preferencePanel, BorderLayout.NORTH);
		ColorKeyPanel colorKeyPanel = new ColorKeyPanel();
		colorKeyPanel.setPreferredSize(new Dimension(200, 1000));
		add(colorKeyPanel, BorderLayout.EAST);
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.PROPERTY_NAME_EYETHREAD_ERROR, this);
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR, this);
	}
	
	public void connectClients() {
		int eyeTrackingPort = Blackboard.getInstance().getEyeTrackingSocket_Port();
		int emotionPort = Blackboard.getInstance().getEmotionSocket_Port();
		cleanUpThreads();
		CustomThread eyeTrackingThread = new EyeTrackingClient(
			Blackboard.getInstance().getEyeTrackingSocket_Host(),
			eyeTrackingPort);
		CustomThread emotionThread = new EmotionDataClient(
			Blackboard.getInstance().getEmotionSocket_Host(),
			emotionPort);
		CustomThread dataProcessor = new RawDataProcessor();
		ViewDataProcessor dpDelegate = new ViewDataProcessor();
		threads.add(eyeTrackingThread);
		threads.add(emotionThread);
		threads.add(dataProcessor);
		threads.add(dpDelegate);
		for (CustomThread thread : threads) {
			thread.start();
		}
	}
	
	public void cleanUpThreads() {
		for (CustomThread thread : threads) {
			if (thread != null) {
				thread.stopThread();
			}
		}
		threads.clear();
	}
	
	private void startServerThreads() {
		System.out.println("Starting test servers.");
		Thread emotionServerThread = new Thread(new EmotionDataServer());
		Thread eyeTrackingThread = new Thread(new EyeTrackingServer());
		emotionServerThread.start();
		eyeTrackingThread.start();
	}
	
	public void createConnectionErrorPopUp(String main_message, String error_message) {
		JOptionPane.showMessageDialog(this,
			String.format("%s\n\n%s\nError: %s", main_message,
				Blackboard.getInstance().getFormattedConnectionSettings(),
				error_message));
	}
	
	@Override
	//Todo: Move this to the controller. Do not create threads until confirmed connection.
	//Todo: Main is not recommended to be observer. Move this to the controller.
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case Blackboard.PROPERTY_NAME_EYETHREAD_ERROR -> {
				cleanUpThreads();
				createConnectionErrorPopUp("Unable to connect to Eye Tracking server. \n" +
					"Please check that the server is running and the IP address is correct.", (String) evt.getNewValue());
			}
			case Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR ->
				createConnectionErrorPopUp("Unable to connect to Emotion server. \n" +
					"Application will run without emotion data.", (String) evt.getNewValue());
		}
	}
	
	public static void main(String[] args) {
		Main window = new Main();
		window.setTitle ("Eye Tracking & Emotion Hub");
		window.setSize (1024, 768);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (args.length > 0 && args[0].equals(TESTING_FLAG)) {
			System.out.println(args[0]);
			window.startServerThreads();
		}
	}

}
