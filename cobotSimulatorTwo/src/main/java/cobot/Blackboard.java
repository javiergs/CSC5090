package cobot;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * The Blackboard class is a singleton class that stores the angles of the robot.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Blackboard extends PropertyChangeSupport {
	
	private static Blackboard blackboard;
	private int[] anglesArray;
 
	private Blackboard() {
		super(new Object());
		anglesArray = new int[]{0, 0, 0, 0, 0, 0};
	}
	
	public static Blackboard getInstance () {
		if (Objects.isNull(blackboard)) {
			blackboard = new Blackboard();
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

