package cobot.blackboard;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import cobot.encoder.CsvEncoder;
import cobot.encoder.EncoderHelper;

/**
 * Blackboard class to store shared data between classes.
 * The Blackboard class is a singleton, meaning that only one instance of it can exist.
 * This version processes messages received from Subscriber and updates internal state accordingly.
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Blackboard extends PropertyChangeSupport {

	private static Blackboard instance;

	public static final int ARM_COUNT = 6;
	public static final int ARM_LENGTH = 50;

	private final ArmHelper armHelper;
	private final OriginHelper originHelper;
	private final EncoderHelper encoder;

	private Blackboard() {
		super(new Object());
		this.armHelper = ArmHelper.init(ARM_COUNT);
		this.originHelper = OriginHelper.init();
		this.encoder = new CsvEncoder(); // Use a concrete implementation of EncoderHelper
	}

	public static Blackboard getInstance() {
		if (instance == null) instance = new Blackboard();
		return instance;
	}

	/**
	 * Processes a message received from the Subscriber.
	 * Parses the message to update the arm angles.
	 * @param message The message received from the Subscriber.
	 */
	public void processSubscriberMessage(String message) {
		try {
			int[] newArmAngles = encoder.parse(message); // Decode message to obtain new angles
			updateArmAngles(newArmAngles);               // Notify listeners of new target angles
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateStatusLabel(String status) {
		firePropertyChange("status", null, status);
	}

	/**
	 * Updates the arm angles and notifies listeners.
	 *
	 * @param targetAngles The new set of arm angles.
	 */
	private void updateArmAngles(int[] targetAngles) {
		// Fire a property change event to notify listeners (like CobotPanel)
		firePropertyChange("armAngles", null, targetAngles);
	}

	public void updateOrigin(int width, int height) {
		originHelper.setCenter(width, height);
		firePropertyChange("origin", null, originHelper.getCenter());
	}

	public int getArmCount() {
		return ARM_COUNT;
	}

	public int getArmLength() {
		return ARM_LENGTH;
	}

	public List<Double> getArmAngles() {
		return armHelper.getArmAngles();
	}

	public List<Color> getArmColors() {
		return armHelper.getColors();
	}

	public List<Double> getTargetAngles() {
		return armHelper.getTargetAngles();
	}

	public Point getCenter() {
		return originHelper.getCenter();
	}
}
