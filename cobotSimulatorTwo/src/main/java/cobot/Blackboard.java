package cobot;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * The Blackboard class is a singleton that stores the angles of the robot and provides a
 * mechanism to observe changes to the data. This class extends PropertyChangeSupport to
 * allow components to listen for updates.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Blackboard extends PropertyChangeSupport {

	private static Blackboard blackboard = null;
	private final int[] ANGLES_ARRAY;

	/**
	 * Private constructor to initialize the Blackboard with default values for angles.
	 */
	private Blackboard() {
		super(new Object());
		ANGLES_ARRAY = new int[6];
	}

	/**
	 * Retrieves the singleton instance of the Blackboard. If no instance exists, a new one is created.
	 *
	 * @return the singleton instance of Blackboard
	 */
	public static Blackboard getInstance () {
		if (Objects.isNull(blackboard)) {
			blackboard = new Blackboard();
		}
		return blackboard;
	}

	/**
	 * Updates the angles stored in the blackboard and notifies listeners of the change.
	 *
	 * @param angles an array of integers representing the new angles for the robot
	 */
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

	/**
	 * Updates the progress of a task or process and notifies listeners of the change.
	 *
	 * @param progress an integer representing the current progress percentage (0-100)
	 */
	public void updateProgress(int progress) {
		firePropertyChange("ProgressUpdated", null, progress);
	}

}

