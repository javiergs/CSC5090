package simulator_affect;

import affectSimulator.MQTTPublisher;
import affectSimulator.MQTTSubscriber;
import affectSimulator.Publisher;
import affectSimulator.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a menu that allows selection of the transmission type and start/stop MQTT transmission
 *
 * @author Yayun Tan
 * @version 1.0
 */
public class CustomMenu extends JMenu {
	
	private Publisher tcpPublisher;
	private Subscriber tcpSubscriber;
	private Thread tcpSubscriberThread;
	private Thread tcpPublisherThread;
	private Thread mqttPublisherThread;
	private Thread mqttSubscriberThread;
	
	private SliderListener sliderListener;
	
	public CustomMenu(String text, SliderListener mainFrame) {
		super(text);
		sliderListener = mainFrame;
		JMenuItem startMQTTMenuItem = new JMenuItem("Start MQTT");
		startMQTTMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
		startMQTTMenuItem.addActionListener(e -> startMQTTTransmission());
		
		JMenuItem startTCPMenuItem = new JMenuItem("Start TCP");
		startTCPMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
		startTCPMenuItem.addActionListener(e -> startTCPTransmission());
		
		JMenuItem stopMenuItem = new JMenuItem("Stop Transmission");
		stopMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
		stopMenuItem.addActionListener(e -> {
			stopTCPTransmission();
			Blackboard.getInstance().setRunning(false);
		});
		
		add(startMQTTMenuItem);
		add(startTCPMenuItem);
		add(stopMenuItem);
	}
	
	public void startMQTTTransmission() {
		stopTCPTransmission();
		sliderListener.setUseMQTT(true);
		Blackboard.getInstance().setRunning(true);
		MQTTPublisher mqttPublisher = MQTTPublisher.getInstance(Blackboard.getInstance());
		MQTTSubscriber mqttSubscriber = new MQTTSubscriber(sliderListener);
		mqttPublisherThread = new Thread(mqttPublisher);
		mqttSubscriberThread = new Thread(mqttSubscriber);
		mqttPublisherThread.start();
		mqttSubscriberThread.start();
	}
	
	public void startTCPTransmission() {
		stopTCPTransmission();
		tcpPublisher = new Publisher("localhost", 5000, Blackboard.getInstance());
		tcpSubscriber = new Subscriber(5000);
		sliderListener.setPublisher(tcpPublisher);
		sliderListener.setUseMQTT(false);
		Blackboard.getInstance().setRunning(true);
		tcpPublisherThread = new Thread(tcpPublisher);
		tcpSubscriberThread = new Thread(tcpSubscriber);
		tcpPublisherThread.start();
		tcpSubscriberThread.start();
	}
	
	public void stopTCPTransmission() {
		Blackboard.getInstance().setRunning(false);
		if (tcpPublisherThread != null && tcpPublisherThread.isAlive()) {
			tcpPublisherThread.interrupt();
			tcpPublisherThread = null;
		}
		if (tcpSubscriberThread != null && tcpSubscriberThread.isAlive()) {
			tcpSubscriber.stop();
			tcpSubscriberThread.interrupt();
			tcpSubscriberThread = null;
		}
		if (tcpPublisher != null) {
			tcpPublisher.close();
			tcpPublisher = null;
		}
		if (mqttPublisherThread != null && mqttPublisherThread.isAlive()) {
			mqttPublisherThread.interrupt();
			mqttPublisherThread = null;
		}
		if (mqttSubscriberThread != null && mqttSubscriberThread.isAlive()) {
			mqttSubscriberThread.interrupt();
			mqttSubscriberThread = null;
		}
	}
}