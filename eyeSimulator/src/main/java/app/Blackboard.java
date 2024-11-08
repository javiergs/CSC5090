package app;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Blackboard extends PropertyChangeSupport {
    private static Blackboard instance;
    private final List<Point> clickPositions;
    private int transmissionSpeed;
    private boolean tracking;

    private Blackboard() {
        super(instance);
        this.clickPositions = new ArrayList<>();
        this.transmissionSpeed = 60;
        this.tracking = false;
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void addObserver(PropertyChangeListener listener) {
        this.addPropertyChangeListener(listener);
    }

    public void removeObserver(PropertyChangeListener listener) {
        this.removePropertyChangeListener(listener);
    }

    public synchronized void addClick(Point click) {
        if (tracking && clickPositions.size() < transmissionSpeed) {
            List<Point> oldValue = new ArrayList<>(clickPositions); // copy current list state
            clickPositions.add(click);
            firePropertyChange("clickPositions", oldValue, clickPositions);
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
        boolean oldValue = tracking;
        tracking = true;
        firePropertyChange("clickPositions", oldValue, true);
    }

    public synchronized void stopTracking() {
        boolean oldValue = tracking;
        tracking = false;
        firePropertyChange("clickPositions", oldValue, false);
    }
}
