package app;

import java.beans.PropertyChangeSupport;

public class Blackboard extends PropertyChangeSupport {
    private static Blackboard instance;
    private boolean isRunning;
    private int sliderValue;
    private String sliderName;

    private Blackboard() {
        super(new Object());
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public String getSliderName() {
        return sliderName;
    }

    public void setRunning(boolean isRunning) {
        boolean oldRunning = this.isRunning;
        this.isRunning = isRunning;
        firePropertyChange("isRunning", oldRunning, isRunning);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
