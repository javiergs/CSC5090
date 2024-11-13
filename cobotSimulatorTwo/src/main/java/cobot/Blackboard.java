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

    private static Blackboard blackboard;
    private int[] anglesArray;

    /**
     * Private constructor to initialize the Blackboard with default values for angles.
     *
     * @param o an initial object (unused, here to satisfy superclass requirements)
     */
    Blackboard(Object o) {
        super(new Object());
        anglesArray = new int[]{0, 0, 0, 0, 0, 0};
    }

    /**
     * Retrieves the singleton instance of the Blackboard. If no instance exists, a new one is created.
     *
     * @return the singleton instance of Blackboard
     */
    public static Blackboard getInstance() {
        if (Objects.isNull(blackboard)) {
            blackboard = new Blackboard(new Object());
        }
        return blackboard;
    }

    /**
     * Updates the angles stored in the blackboard and notifies listeners of the change.
     *
     * @param angles an array of integers representing the new angles for the robot
     */
    public void setAngles(int[] angles) {
        anglesArray = angles;
        firePropertyChange("AnglesAdded", null, anglesArray);
    }

    /**
     * Retrieves the current array of angles stored in the blackboard.
     *
     * @return an array of integers representing the angles of the robot
     */
    public int[] getAngles() {
        return anglesArray;
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