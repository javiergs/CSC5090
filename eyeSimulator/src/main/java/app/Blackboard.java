package app;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Blackboard class is a singleton class that stores click data and tracking status.
 * It provides synchronized access to the shared data and state.
 *
 * @author Luca Ornstil,
 * @author Monish Suresh,
 * @author Christine Widden
 * @version 1.0
 */
public class Blackboard {
	
	private static Blackboard instance;
	private final List<Point> clickPositions;  // Shared repository of click data
	private int transmissionSpeed;       // Shared transmission speed
	private boolean tracking;            // Shared state for tracking status
	
	private Blackboard() {
		this.clickPositions = new ArrayList<>();
		this.transmissionSpeed = 60;     // Default speed
		this.tracking = false;           // Initial state
	}
	
	public static synchronized Blackboard getInstance() {
		if (instance == null) {
			instance = new Blackboard();
		}
		return instance;
	}
	
	public synchronized void addClick(Point click) {
		if (tracking && clickPositions.size() < transmissionSpeed) { // added condition to cap number of clicks being sent
			clickPositions.add(click);
		}
	}
	
	public synchronized ArrayList<Point> getClickPositions() {
		return new ArrayList<>(clickPositions);
	}
	
	public synchronized void clearClicks() {
		clickPositions.clear();
	}
	
	public synchronized void setTransmissionSpeed(int speed) {
		this.transmissionSpeed = speed;
	}
	
	public synchronized boolean isTracking() {
		return tracking;
	}
	
	public synchronized void startTracking() {
		tracking = true;
	}
	
	public synchronized void stopTracking() {
		tracking = false;
	}
	
}