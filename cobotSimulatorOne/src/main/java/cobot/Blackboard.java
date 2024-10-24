package cobot;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public static final int ARM_LENGTH = 80;
	
	private List<Double> armAngles;
	private List<Double> targetAngles; // for smooth animation
	private List<Color> armColors;
	
	private Point center;
	
	private Blackboard() {
		super(new Object());
		armAngles = new ArrayList<>();
		targetAngles = new ArrayList<>();
		armColors = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < ARM_COUNT; i++) {
			armAngles.add(0.0);
			targetAngles.add(0.0);
			armColors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		}
	}
	
	public static Blackboard getInstance() {
		if (instance == null)
			instance = new Blackboard();
		return instance;
	}
	
	public void updateStatusLabel(String status) {
		firePropertyChange("status", null, status);
	}
	
	public void updateOrigin(int width, int height) {
		this.center = new Point(width / 2, height / 2);
		this.center.y -= 36; //remove menu size from calculation
		firePropertyChange("origin", null, center);
	}
	
	public void updateArmAngles(int[] numbers) {
		armAngles.clear();
		for (int i = 0; i < numbers.length; i++) {
			armAngles.add((double) numbers[i]);
		}
		firePropertyChange("armAngles", null, armAngles);
	}
	
	public Point getCenter() {
		return center;
	}
	
	public List<Double> getArmAngles() {
		return armAngles;
	}
	
	public List<Color> getArmColors() {
		return armColors;
	}
	
	public List<Double> getTargetAngles() {
		return targetAngles;
	}
	
}