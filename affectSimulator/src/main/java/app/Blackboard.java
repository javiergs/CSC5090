package app;

import affectSimulator.PublisherInterface;
import affectSimulator.MQTTCommunicatorInterface;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Blackboard extends PropertyChangeSupport implements PublisherInterface, MQTTCommunicatorInterface, PropertyChangeListener {
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

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setSliderValueExternally(String sliderName, int newValue) {
        // Implement as needed or leave empty if Blackboard does not need to use it directly
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if ("someProperty".equals(evt.getPropertyName())) {
            // 处理 Subscriber 的变化通知
        }
    }
}