package cobot;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * The Blackboard class is a singleton class that stores the angles of the robot.
 */
public class Blackboard extends PropertyChangeSupport {
	
	private static Blackboard blackboard;
	private int[] anglesArray;
 
	Blackboard(Object o) {
		super(new Object());
		anglesArray = new int[]{0, 0, 0, 0, 0, 0};
	}
	
	public static Blackboard getInstance () {
		if (Objects.isNull(blackboard)) {
			blackboard = new Blackboard(new Object());
		}
		return blackboard;
	}
	
	public void setAngles(int[] angles) {
      anglesArray = angles;
      firePropertyChange("AnglesAdded", null, anglesArray);
	}
	
	public int[] getAngles() {
		return anglesArray;
	}

}