package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import test.Subscriber;
import test.MQTTSubscriber;

public class Main extends JFrame implements ActionListener, PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private MQTTPublisher mqttPublisher;
    private Publisher tcpPublisher;
    private Subscriber tcpSubscriber;
    private MQTTSubscriber mqttSubscriber;
    private Thread subscriberThread;
    private Thread publisherThread;

    private JMenuItem startTCPMenuItem;
    private JMenuItem startMQTTMenuItem;
    private JMenuItem stopSubscriberMenuItem;
    private JMenuItem configureMenuItem;
    private JMenuItem startTrackingMenuItem;
    private JMenuItem stopTrackingMenuItem;

    public static void main(String[] args) {
        Main main = new Main();
        main.setTitle("Eye Tracking Simulator");
        main.setSize(800, 600);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
        logger.info("Eye Tracking Simulator application started.");
    }

    public Main() {
        Blackboard.getInstance().addObserver(this);
//        mqttPublisher = new MQTTPublisher();
        startMQTTPublisher();
        tcpPublisher = new Publisher("localhost", 5000);  // Replace with actual TCP server address

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        startTCPMenuItem = new JMenuItem("Start TCP Subscriber");
        startMQTTMenuItem = new JMenuItem("Start MQTT Subscriber");
        stopSubscriberMenuItem = new JMenuItem("Stop Subscriber");
        configureMenuItem = new JMenuItem("Configure");
        startTrackingMenuItem = new JMenuItem("Start Tracking");
        stopTrackingMenuItem = new JMenuItem("Stop Tracking");

        menu.add(startTCPMenuItem);
        menu.add(startMQTTMenuItem);
        menu.add(stopSubscriberMenuItem);
        menu.add(configureMenuItem);
        menu.add(startTrackingMenuItem);
        menu.add(stopTrackingMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        startTCPMenuItem.addActionListener(this);
        startMQTTMenuItem.addActionListener(this);
        stopSubscriberMenuItem.addActionListener(this);
        configureMenuItem.addActionListener(this);
        startTrackingMenuItem.addActionListener(this);
        stopTrackingMenuItem.addActionListener(this);

        // Initial state of buttons
        startTCPMenuItem.setEnabled(true);
        startMQTTMenuItem.setEnabled(true);
        stopSubscriberMenuItem.setEnabled(false);
        configureMenuItem.setEnabled(false);
        startTrackingMenuItem.setEnabled(false);
        stopTrackingMenuItem.setEnabled(false);

        WorkArea workArea = new WorkArea();
        add(workArea);
        logger.debug("Main UI initialized with menu and work area.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startTCPMenuItem) {
            startSubscriber("TCP");
        } else if (e.getSource() == startMQTTMenuItem) {
            startSubscriber("MQTT");
        } else if (e.getSource() == stopSubscriberMenuItem) {
            stopSubscriber();
        } else if (e.getSource() == configureMenuItem) {
            configureSettings();
        } else if (e.getSource() == startTrackingMenuItem) {
            startTracking();
        } else if (e.getSource() == stopTrackingMenuItem) {
            stopTracking();
        }
    }

    private void startSubscriber(String type) {
        stopSubscriber();  // Ensure any previous subscriber is stopped

        if (type.equals("TCP")) {
            try {
                tcpSubscriber = new Subscriber("localhost", 5000, Blackboard.getInstance());
                subscriberThread = new Thread(tcpSubscriber);
                subscriberThread.start();
                logger.info("TCP Subscriber started.");
            } catch (Exception e) {
                logger.error("Failed to start TCP Subscriber", e);
            }
        } else if (type.equals("MQTT")) {
            try {
                Map<String, String> topics = new HashMap<>();
                topics.put("eye-tracking/topic", "Data");
                mqttSubscriber = new MQTTSubscriber(Blackboard.getInstance());
                subscriberThread = new Thread(mqttSubscriber);
                subscriberThread.start();
                logger.info("MQTT Subscriber started.");
            } catch (Exception e) {
                logger.error("Failed to start MQTT Subscriber", e);
            }
        }

        // Update button states
        startTCPMenuItem.setEnabled(false);
        startMQTTMenuItem.setEnabled(false);
        configureMenuItem.setEnabled(true);
        stopSubscriberMenuItem.setEnabled(true);
        startTrackingMenuItem.setEnabled(false);
        stopTrackingMenuItem.setEnabled(false);
    }

    private void stopSubscriber() {
        if (tcpSubscriber != null) {
            tcpSubscriber.stopSubscriber();
            tcpSubscriber = null;
            logger.info("TCP Subscriber stopped.");
        }

        if (mqttSubscriber != null) {
            mqttSubscriber.stopSubscriber();
            mqttSubscriber = null;
            logger.info("MQTT Subscriber stopped.");
        }

        if (subscriberThread != null) {
            subscriberThread.interrupt();
            subscriberThread = null;
        }

        // Reset button states
        startTCPMenuItem.setEnabled(true);
        startMQTTMenuItem.setEnabled(true);
        configureMenuItem.setEnabled(false);
        startTrackingMenuItem.setEnabled(false);
        stopTrackingMenuItem.setEnabled(false);
        stopSubscriberMenuItem.setEnabled(false);
    }

    private void startMQTTPublisher() {
        stopMQTTPublisher();  // Ensure any previous subscriber is stopped

        try {
            mqttPublisher = new MQTTPublisher();
            publisherThread = new Thread(mqttPublisher);
            publisherThread.start();
            logger.info("MQTT Publisher started.");
        } catch (Exception e) {
            logger.error("Failed to start MQTT Publisher", e);
        }
    }

    private void stopMQTTPublisher() {
        if (mqttPublisher != null) {
            mqttPublisher.stopPublisher();
            mqttPublisher = null;
            logger.info("MQTT Publisher stopped.");
        }

        if (publisherThread != null) {
            publisherThread.interrupt();
            publisherThread = null;
        }
    }

    private void configureSettings() {
        try {
            String widthStr = JOptionPane.showInputDialog(this, "Enter width:", "Configure", JOptionPane.QUESTION_MESSAGE);
            String heightStr = JOptionPane.showInputDialog(this, "Enter height:", "Configure", JOptionPane.QUESTION_MESSAGE);
            String speedStr = JOptionPane.showInputDialog(this, "Enter data transmission speed (frames per second):", "Configure", JOptionPane.QUESTION_MESSAGE);

            int width = Integer.parseInt(widthStr);
            int height = Integer.parseInt(heightStr);
            int speed = Integer.parseInt(speedStr);

            setSize(width, height);
            Blackboard.getInstance().setTransmissionSpeed(speed);
            logger.info("Configuration updated: width={}, height={}, transmission speed={}", width, height, speed);

            startTrackingMenuItem.setEnabled(true);
            configureMenuItem.setEnabled(false);

        } catch (NumberFormatException ex) {
            logger.error("Invalid configuration input", ex);
            JOptionPane.showMessageDialog(this, "Invalid configuration input. Please enter numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startTracking() {
        Blackboard.getInstance().startTracking();
        startTrackingMenuItem.setEnabled(false);
        stopTrackingMenuItem.setEnabled(true);
        logger.info("Tracking started.");
    }

    private void stopTracking() {
        Blackboard.getInstance().stopTracking();
        startTrackingMenuItem.setEnabled(false);
        stopTrackingMenuItem.setEnabled(false);
        configureMenuItem.setEnabled(false);
        stopSubscriberMenuItem.setEnabled(true);
        startTCPMenuItem.setEnabled(false);
        startMQTTMenuItem.setEnabled(false);
        logger.info("Tracking stopped.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Main detected property change on property {}", evt.getPropertyName());
//        String message = "Clicks: " + Blackboard.getInstance().getClickPositions();
//        mqttPublisher.publish(message);  // Publishes using MQTT
//        tcpPublisher.publish(message);   // Publishes using TCP
    }
}
