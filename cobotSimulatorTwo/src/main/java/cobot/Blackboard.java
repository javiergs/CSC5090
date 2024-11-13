package cobot;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * The Blackboard class is a singleton class that stores the angles of the robot.
 */
public class Blackboard extends PropertyChangeSupport {

	private static Blackboard blackboard = null;
	private final int[] ANGLES_ARRAY;

	private Blackboard() {
		super(new Object());
		ANGLES_ARRAY = new int[6];
	}

	public static Blackboard getInstance () {
		if (Objects.isNull(blackboard)) {
			blackboard = new Blackboard();
		}
		return blackboard;
	}

	public void setAngles(int[] angles) {
		//Ensure that there are always 6 angles
      	for (int i = 0; i < ANGLES_ARRAY.length; i++) {
			  if (i < angles.length) {
				  ANGLES_ARRAY[i] = angles[i];
			  } else {
				  ANGLES_ARRAY[i] = 0;
			  }
		}

      	firePropertyChange("AnglesAdded", null, ANGLES_ARRAY);
	}

	public int[] getAngles() {
		return ANGLES_ARRAY;
	}

	public void updateProgress(int progress) {
		firePropertyChange("ProgressUpdated", null, progress);
	}

}

