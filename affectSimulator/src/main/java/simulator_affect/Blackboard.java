package simulator_affect;

import affectSimulator.MQTTCommunicatorInterface;
import affectSimulator.PublisherInterface;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Blackboard class to manage shared data and coordinate communication between
 * classes in the application. This class is designed as a singleton.
 * The Blackboard class implements interfaces for both Publisher and MQTT
 * communication, allowing it to act as a central hub for handling application
 * state and inter-component data transfer.
 *
 * @author javiergs
 * @author Yayun Tan
 * @version 2024.12.10
 */
public class Blackboard extends PropertyChangeSupport
	implements PublisherInterface, MQTTCommunicatorInterface, PropertyChangeListener {
	
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
	}
	
	@Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		if ("someProperty".equals(evt.getPropertyName())) {
		}
	}

}