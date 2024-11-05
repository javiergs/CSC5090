package app;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Blackboard {
    private static Blackboard instance;
    private final List<Point> clickPositions;
    private int transmissionSpeed;
    private boolean tracking;

    private Blackboard() {
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

    public synchronized void addClick(Point click) {
        if (tracking && clickPositions.size() < transmissionSpeed) {
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
