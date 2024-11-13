package cobot;

/**
 * Singleton class that stores and manages angle data for the cobot.
 * This class utilizes the blackboard pattern to allow different components to share
 * and observe changes to angle data.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class AngleBlackboard extends Blackboard {
    private static AngleBlackboard instance;

    /**
     * Private constructor to prevent instantiation. Initializes the blackboard with a default value.
     */
    private AngleBlackboard() {
        super(new Object());
    }

    /**
     * Retrieves the singleton instance of the AngleBlackboard. If no instance exists, it creates one.
     *
     * @return the singleton instance of AngleBlackboard
     */
    public static AngleBlackboard getInstance() {
        if (instance == null) {
            instance = new AngleBlackboard();
        }
        return instance;
    }

    /**
     * Sets new angle values in the blackboard and notifies listeners of the update.
     *
     * @param angles an array of integers representing the angles for the cobot
     */
    public void setAngles(int[] angles) {
        super.setAngles(angles);
        firePropertyChange("AnglesAdded", null, super.getAngles());
    }
}