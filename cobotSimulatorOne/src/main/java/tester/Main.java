package tester;

import cobot.encoder.CsvEncoder;
import cobotSimulatorOneLibrary.MQTTPublisher;
import cobotSimulatorOneLibrary.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts the server to publish data to subscribers
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final int ANGLES_TRANSMIT_DELAY_SECONDS = 2;

	public static void main(String[] args) {
		int port = 12345;
		String brokerUrl = "tcp://test.mosquitto.org:1883";
		String mqttTopic = "cobot/commands";

		Publisher publisher = new Publisher(port);
		CsvEncoder encoder = new CsvEncoder();

		Thread publisherThread = new Thread(publisher);
		publisherThread.start();

		MQTTPublisher mqttPublisher = new MQTTPublisher(brokerUrl, mqttTopic);
		mqttPublisher.start();

		// Main loop to generate and send messages at intervals
		new Thread(() -> {
			int[] angles = new int[6];
			while (true) {
				try {
					for (int i = 0; i < angles.length; i++) {
						angles[i] = (int) (Math.random() * 360);
					}

					String encodedAngles = encoder.encode(angles);
					logger.info("Generated encoded angles: {}", encodedAngles);

					publisher.sendMessageToAll(encodedAngles);

					mqttPublisher.sendMessage(encodedAngles);

					Thread.sleep(ANGLES_TRANSMIT_DELAY_SECONDS * 1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.error("Main angle generation loop interrupted", e);
					break;
				}
			}
		}).start();
	}
}
