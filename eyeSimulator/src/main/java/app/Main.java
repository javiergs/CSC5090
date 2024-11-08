package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Main extends JFrame implements ActionListener, PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private MQTTPublisher publisher;
    private JMenuItem startMenuItem;
    private JMenuItem stopMenuItem;
    private JMenuItem configureMenuItem;
    private JMenuItem startTransmitMenuItem;

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
        publisher = new MQTTPublisher();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        startMenuItem = new JMenuItem("Start");
        stopMenuItem = new JMenuItem("Stop");
        configureMenuItem = new JMenuItem("Configure");
        startTransmitMenuItem = new JMenuItem("Start Transmit");

        menu.add(startMenuItem);
        menu.add(stopMenuItem);
        menu.add(configureMenuItem);
        menu.add(startTransmitMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        startMenuItem.addActionListener(this);
        stopMenuItem.addActionListener(this);
        configureMenuItem.addActionListener(this);
        startTransmitMenuItem.addActionListener(this);

        stopMenuItem.setEnabled(false);
        WorkArea workArea = new WorkArea();
        add(workArea);
        logger.debug("Main UI initialized with menu and work area.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startMenuItem) {
            Blackboard.getInstance().startTracking();
            startMenuItem.setEnabled(false);
            stopMenuItem.setEnabled(true);
            logger.info("Tracking started.");
        } else if (e.getSource() == stopMenuItem) {
            Blackboard.getInstance().stopTracking();
            startMenuItem.setEnabled(true);
            stopMenuItem.setEnabled(false);
            logger.info("Tracking stopped.");
        } else if (e.getSource() == configureMenuItem) {
            configureSettings();
        } else if (e.getSource() == startTransmitMenuItem) {
            startTransmit();
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
        } catch (NumberFormatException ex) {
            logger.error("Invalid configuration input", ex);
            JOptionPane.showMessageDialog(this, "Invalid configuration input. Please enter numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startTransmit() {
        logger.info("Data transmission initiated.");
        JOptionPane.showMessageDialog(this, "Data transmission started.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String message = "Clicks: " + Blackboard.getInstance().getClickPositions();
        publisher.publish(message);
    }
}
