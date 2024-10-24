package app.View;

import app.Model.Blackboard;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code PreferencePanel} class represents a configuration panel that allows users to modify
 * server connection settings, display parameters, and system behavior. This panel provides fields
 * for setting IP addresses, ports, the maximum number of circles displayed, and the threshold radius
 * for circle consolidation.
 * <p>
 * When the "Apply" button is pressed, the updated values are applied to the {@link Blackboard},
 * and the display is refreshed. If the system is actively running, it will restart data retrieval
 * based on the new settings.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class PreferencePanel extends JPanel {
	
	private final JTextField emotionIpField;
	private final JTextField emotionPortField;
	private final JTextField eyeTrackingIpField;
	private final JTextField eyeTrackingPortField;
	private final JTextField maxCirclesField;
	private final JTextField thresholdRadiusField;
	
	public PreferencePanel() {
		setPreferredSize(new Dimension(1000, 150));  // Set the size of the panel
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2);  // Set smaller padding between components
		// Initialize fields with existing values from Blackboard
		Blackboard blackboard = Blackboard.getInstance();
		// Emotion Server IP
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		add(new JLabel("Emotion Server IP:"), gbc);
		emotionIpField = new JTextField(blackboard.getEmotionSocket_Host(), 10); // Autofill with current value
		gbc.gridx = 1;
		add(emotionIpField, gbc);
		// Emotion Server Port
		gbc.gridx = 2;
		add(new JLabel("Emotion Server Port:"), gbc);
		emotionPortField = new JTextField(String.valueOf(blackboard.getEmotionSocket_Port()), 10); // Autofill with current value
		gbc.gridx = 3;
		add(emotionPortField, gbc);
		// Eye Tracking Server IP
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("Eye Tracking Server IP:"), gbc);
		eyeTrackingIpField = new JTextField(blackboard.getEyeTrackingSocket_Host(), 10); // Autofill with current value
		gbc.gridx = 1;
		add(eyeTrackingIpField, gbc);
		// Eye Tracking Server Port
		gbc.gridx = 2;
		add(new JLabel("Eye Tracking Server Port:"), gbc);
		eyeTrackingPortField = new JTextField(String.valueOf(blackboard.getEyeTrackingSocket_Port()), 10); // Autofill with current value
		gbc.gridx = 3;
		add(eyeTrackingPortField, gbc);
		// Max Circles
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("Max Circles:"), gbc);
		maxCirclesField = new JTextField(String.valueOf(blackboard.getMaxCircles()), 10);  // Autofill with current value
		gbc.gridx = 1;
		add(maxCirclesField, gbc);
		// Threshold Radius
		gbc.gridx = 2;
		add(new JLabel("Threshold Radius:"), gbc);
		thresholdRadiusField = new JTextField(String.valueOf(blackboard.getThresholdRadius()), 10);  // Autofill with current value
		gbc.gridx = 3;
		add(thresholdRadiusField, gbc);
		// Apply Button
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(e -> applyChanges());
		add(applyButton, gbc);
	}
	
	private void applyChanges() {
		try {
			Blackboard blackboard = Blackboard.getInstance();
			blackboard.setEmotionSocket_Host(emotionIpField.getText());
			blackboard.setEmotionSocket_Port(Integer.parseInt(emotionPortField.getText()));
			blackboard.setEyeTrackingSocket_Host(eyeTrackingIpField.getText());
			blackboard.setEyeTrackingSocket_Port(Integer.parseInt(eyeTrackingPortField.getText()));
			int maxCircles = Integer.parseInt(maxCirclesField.getText());
			int thresholdRadius = Integer.parseInt(thresholdRadiusField.getText());
			blackboard.setMaxCircles(maxCircles);
			blackboard.setThresholdRadius(thresholdRadius);
			// logger.info ("Settings applied.");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid ints for ports, Max Circles, and Threshold Radius.");
		}
	}
	
}