package cobot.blackboard;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Blackboard class to store shared data between classes.
 * The Blackboard class is a singleton, meaning that only one instance of it can exist.
 *
 * @author Jack Ortega,
 * @author Neeraja Beesetti,
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
	
	private Blackboard() {
		super(new Object());
		this.armHelper = ArmHelper.init(ARM_COUNT);
		this.originHelper = OriginHelper.init();
	}
	
	public static Blackboard getInstance() {
		if (instance == null) instance = new Blackboard();
		return instance;
	}
	
	public void updateStatusLabel(String status) {
		firePropertyChange("status", null, status);
	}

	public void updateArmAngles(int[] numbers) {
		armHelper.updateAngles(numbers);
		firePropertyChange("armAngles", null, armHelper.getArmAngles());
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