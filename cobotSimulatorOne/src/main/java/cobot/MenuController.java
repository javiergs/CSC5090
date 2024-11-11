package cobot;

import cobot.blackboard.Blackboard;
import cobot.encoder.CsvEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MenuController class that listens for button clicks and calls the appropriate methods in the Client class.
 *
 * This version supports starting both traditional TCP and MQTT clients.
 *
 * Author(s): Jack Ortega, Neeraja Beesetti, Saanvi Dua, Javier Gonzalez-Sanchez
 * Version: 2.0
 */
public class MenuController implements ActionListener {

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	public static final String LOCALHOST = "localhost";
	public static final int PORT = 12345;
	private Subscriber subscriber;
	private MQTTSubscriber mqttSubscriber;

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Start client":
				startClient();
				break;
			case "Start MQTT client":
				startMQTTClient();
				break;
			case "Stop client":
				stopClient();
				break;
			case "Exit":
				System.exit(0);
				break;
		}
	}

	private void startClient() {
		if (subscriber == null || !subscriber.isRunning()) {
			logger.info("Starting TCP subscriber");
			subscriber = new Subscriber(LOCALHOST, PORT, Blackboard.getInstance()::processSubscriberMessage);
			Thread subscriberThread = new Thread(subscriber);
			subscriberThread.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!subscriber.isRunning()) {
				logger.info("Client could not connect to server");
				JOptionPane.showMessageDialog(null, "Could not connect to server", "Error", JOptionPane.ERROR_MESSAGE);
				Blackboard.getInstance().updateStatusLabel("disconnected");
			} else {
				logger.info("Client connected to server");
				JOptionPane.showMessageDialog(null, "Connected to server", "Success", JOptionPane.INFORMATION_MESSAGE);
				Blackboard.getInstance().updateStatusLabel("connected");
			}
		} else {
			logger.info("TCP subscriber is already running");
		}
	}

	private void startMQTTClient() {
		if (mqttSubscriber == null) {
			logger.info("Starting MQTT subscriber");
			String brokerUrl = "tcp://test.mosquitto.org:1883";  // Mosquitto public broker URL
			String topic = "cobot/commands";  // Replace with actual MQTT topic
			mqttSubscriber = new MQTTSubscriber(brokerUrl, topic, Blackboard.getInstance()::processSubscriberMessage);
			mqttSubscriber.start();
			JOptionPane.showMessageDialog(null, "Connected to MQTT server", "Success", JOptionPane.INFORMATION_MESSAGE);
			Blackboard.getInstance().updateStatusLabel("MQTT connected");
		} else {
			logger.info("MQTT subscriber is already running");
		}
	}

	private void stopClient() {
		if (subscriber != null && subscriber.isRunning()) {
			logger.info("Stopping TCP subscriber");
			subscriber.stop();
			JOptionPane.showMessageDialog(null, "TCP Client stopped", "Success", JOptionPane.INFORMATION_MESSAGE);
			Blackboard.getInstance().updateStatusLabel("disconnected");
		}
		if (mqttSubscriber != null) {
			logger.info("Stopping MQTT subscriber");
			mqttSubscriber.stop();
			mqttSubscriber = null;
			JOptionPane.showMessageDialog(null, "MQTT Client stopped", "Success", JOptionPane.INFORMATION_MESSAGE);
			Blackboard.getInstance().updateStatusLabel("disconnected");
		}
	}
}
