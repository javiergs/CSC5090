package app.View;

import app.Controller.MainController;
import app.Model.*;
import app.library.TheSubscriber;
import test.EmotionDataServer;
import test.EyeTrackingServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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
public class Main extends JFrame {
	private static final String TESTING_FLAG = "-test";
	private final ArrayList<CustomThread> threads;
	private TheSubscriber eyeSubscriber = null;
	private TheSubscriber emotionSubscriber = null;
	
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
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.EYE_DATA_LABEL, controller);
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.EMOTION_DATA_LABEL, controller);
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.PROPERTY_NAME_VIEW_DATA, drawPanel);
	}
	
	public void connectClients() {
		int eyeTrackingPort = Blackboard.getInstance().getEyeTrackingSocket_Port();
		int emotionPort = Blackboard.getInstance().getEmotionSocket_Port();
		cleanUpThreads();

		CustomThread dataProcessor = new RawDataProcessor();
		ViewDataProcessor dpDelegate = new ViewDataProcessor();

		try {
			eyeSubscriber = new TheSubscriber(Blackboard.getInstance().getEyeTrackingSocket_Host(),
					eyeTrackingPort, Blackboard.EYE_DATA_LABEL, Blackboard.getInstance());
		} catch (IOException e) {
			Blackboard.getInstance().reportEyeThreadError(e.getMessage());
			//do not continue if we don't have access to eye data
			return;
		}
		try {
			emotionSubscriber = new TheSubscriber(Blackboard.getInstance().getEmotionSocket_Host(),
					emotionPort, Blackboard.EMOTION_DATA_LABEL, Blackboard.getInstance());
		} catch (IOException e) {
			Blackboard.getInstance().reportEmotionThreadError(e.getMessage());
        }

		Thread eyeThread = new Thread(eyeSubscriber);
		eyeThread.start();
		if (emotionSubscriber != null){
			Thread emotionThread = new Thread(emotionSubscriber);
			emotionThread.start();
		}


		threads.add(dataProcessor);
		threads.add(dpDelegate);
		for (CustomThread thread : threads) {
			thread.start();
		}
	}
	
	public void cleanUpThreads() {
		if (eyeSubscriber != null ){
			eyeSubscriber.stopSubscriber();
			eyeSubscriber = null;
		}
		if (emotionSubscriber != null ){
			emotionSubscriber.stopSubscriber();
			emotionSubscriber = null;
		}
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

	//Todo: clean up threads, may not need CustomThread class at all

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
