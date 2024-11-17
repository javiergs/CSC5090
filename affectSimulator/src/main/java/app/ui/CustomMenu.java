package app.ui;

import app.Blackboard;
import app.Main;

import javax.swing.*;
import java.awt.*;

/**
 * CustomMenu class that creates a menu with options to start and stop MQTT or TCP transmission.
 * The menu allows the user to control the transmission type and stop transmission when needed.
 *
 * @author Yayun Tan
 * @version 1.0
 */

public class CustomMenu extends JMenu {
    public CustomMenu(String text, Main mainFrame) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 18));
        setPreferredSize(new Dimension(120, 40));

        JMenuItem startMQTTMenuItem = new JMenuItem("Start MQTT");
        startMQTTMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
        startMQTTMenuItem.addActionListener(e -> mainFrame.startMQTTTransmission());

        JMenuItem startTCPMenuItem = new JMenuItem("Start TCP");
        startTCPMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
        startTCPMenuItem.addActionListener(e -> mainFrame.startTCPTransmission());

        JMenuItem stopMenuItem = new JMenuItem("Stop Transmission");
        stopMenuItem.setFont(new Font("Arial", Font.PLAIN, 18));
        stopMenuItem.addActionListener(e -> {
            mainFrame.stopTCPTransmission();
            Blackboard.getInstance().setRunning(false);
        });

        add(startMQTTMenuItem);
        add(startTCPMenuItem);
        add(stopMenuItem);
    }
}
