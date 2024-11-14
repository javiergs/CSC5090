package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import eyesimulator.DataDestination;

/**
 * Singleton class representing a central storage for click positions and tracking status.
 * Manages observers and notifies them of property changes.
 *
 * @version 1.2
 * @authors
 * Monish Suresh
 * Christine Widden
 * Luca Ornstil
 */

public class Blackboard extends PropertyChangeSupport implements DataDestination {
    private static Blackboard instance;
    private final List<Point> clickPositions;
    private int transmissionSpeed;
    private boolean tracking;
    private final PropertyChangeSupport propertyChangeSupport;

    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    private Blackboard() {
        super(new Object());  // Initialize with a dummy object
        this.propertyChangeSupport = new PropertyChangeSupport(this);
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
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeObserver(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public synchronized void addClick(Point click) {
        if (tracking && clickPositions.size() < transmissionSpeed) {
            List<Point> oldValue = new ArrayList<>(clickPositions);
            clickPositions.add(click);
            logger.info("click added");
            propertyChangeSupport.firePropertyChange("clickPositions", oldValue, clickPositions);
        }
    }

    public synchronized ArrayList<Point> getClickPositions() {
        return new ArrayList<>(clickPositions);
    }

    public synchronized void clearClicks() {
        List<Point> oldValue = new ArrayList<>(clickPositions);
        clickPositions.clear();
        propertyChangeSupport.firePropertyChange("clickPositions", oldValue, clickPositions);
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
        propertyChangeSupport.firePropertyChange("clickPositions", oldValue, true);
    }

    public synchronized void stopTracking() {
        boolean oldValue = tracking;
        tracking = false;
        propertyChangeSupport.firePropertyChange("clickPositions", oldValue, false);
    }

    // Implementation of DataDestination methods
    @Override
    public void addSubscriberData(String dataWithPrefix) {
        // Process the incoming data and update the Blackboard as needed
        System.out.println("Received data: " + dataWithPrefix);
    }

    @Override
    public void alertError(String messageWithPrefix) {
        System.err.println("Error received: " + messageWithPrefix);
    }
}
