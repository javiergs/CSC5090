package cobot;

import CobotSimulatorTwo.Blackboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The RobotPanelHandler class is responsible for managing the robot's simulation process.
 * It listens for angle updates and simulates the movement of the robot arm by gradually
 * adjusting its angles to reach the target positions.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class RobotPanelHandler implements ActionListener, PropertyChangeListener {

    private final Blackboard BLACKBOARD;
    private final Timer SIMULATION_TIMER;
    private int[] targetAngles;
    private int phase = 0;
    private boolean simulate = false;
    private final int[] CURRENT_ANGLES = new int[6];

    /**
     * Constructs a new RobotPanelHandler instance that initializes the blackboard,
     * adds this handler as a property change listener, and sets up the simulation timer.
     */
    public RobotPanelHandler() {
        this.BLACKBOARD = Blackboard.getInstance();
        this.BLACKBOARD.addPropertyChangeListener(this);
        SIMULATION_TIMER = new Timer(20, this);
    }

    /**
     * Starts the simulation to move the robot arm towards the target angles. The simulation begins
     * at phase 0 and updates the angles incrementally.
     */
    public void startSimulation() {
        if (targetAngles != null) {
            simulate = true;
            phase = 0;
            SIMULATION_TIMER.start();
        }
    }

    /**
     * Stops the simulation, halting the movement of the robot arm. The timer is stopped,
     * and the simulation flag is set to false.
     */
    public void stopSimulation() {
        if (SIMULATION_TIMER.isRunning()) {
            SIMULATION_TIMER.stop();
        }
        simulate = false;
    }

    /**
     * Updates the angles of the robot arm towards the target angles based on the current phase.
     * If the target angle for the current phase is reached, the next phase begins. Once all phases
     * are complete, the simulation is stopped and progress is updated to 100%.
     */
    private void updateAngles() {
        boolean reachedTarget = adjustAngleTowardsTarget(phase, targetAngles[phase]);

        if (reachedTarget) {
            phase++;
            if (phase >= CURRENT_ANGLES.length) {
                phase = 0;
                stopSimulation();
                BLACKBOARD.updateProgress(100);
                JOptionPane.showMessageDialog(null, "All angles have been simulated!");
                return;
            }
        }

        int progress = (int) (((phase + getAngleProgress(phase)) / (double) CURRENT_ANGLES.length) * 100);
        BLACKBOARD.updateProgress(progress);

    }

    /**
     * Calculates the progress towards reaching the target angle for a given phase.
     *
     * @param index the phase index of the current joint
     * @return a double value representing the progress (0.0 to 1.0)
     */
    private double getAngleProgress(int index) {
        int targetAngle = targetAngles[index];
        int currentAngle = CURRENT_ANGLES[index];
        int totalDelta = Math.abs(targetAngle - currentAngle);

        return totalDelta == 0 ? 1 : (1 - (double) Math.abs(currentAngle - targetAngle) / totalDelta);
    }

    /**
     * Adjusts the current angle of the specified phase towards the target angle. The angle is
     * adjusted incrementally by a step size of 1 degree.
     *
     * @param index       the phase index of the current joint
     * @param targetAngle the target angle to reach
     * @return true if the target angle is reached, false otherwise
     */
    private boolean adjustAngleTowardsTarget(int index, int targetAngle) {
        int currentAngle = CURRENT_ANGLES[index];
        int step = 1;

        if (Math.abs(currentAngle - targetAngle) <= step) {
            CURRENT_ANGLES[index] = targetAngle;
            return true;
        }
        CURRENT_ANGLES[index] += (currentAngle < targetAngle) ? step : -step;
        return false;
    }

    /**
     * Listens for property changes from the blackboard and starts the simulation if new angles
     * are added to the system.
     *
     * @param evt the PropertyChangeEvent triggered by the blackboard
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("AnglesAdded".equals(evt.getPropertyName()) && evt.getNewValue() instanceof int[]) {
            int[] newAngles = (int[]) evt.getNewValue();
            if (newAngles.length == 6) {
                setTargetAngles(newAngles);
                startSimulation();
            }
        }
    }

    /**
     * Sets the target angles for the robot arm. This method validates that the angles array
     * contains exactly 6 elements.
     *
     * @param angles an array containing 6 target angles for the robot arm's joints
     * @throws IllegalArgumentException if the angles array does not contain exactly 6 elements
     */
    public void setTargetAngles(int[] angles) {
        if (angles.length != 6) {
            throw new IllegalArgumentException("Angles array must have exactly 6 elements.");
        }
        this.targetAngles = angles;
    }

    /**
     * Returns the current angles of the robot arm's joints.
     *
     * @return an array containing the current angles of the robot arm's joints
     */
    public int[] getCurrentAngles() {
        return CURRENT_ANGLES;
    }

    /**
     * Performs the action of updating the robot's angles during the simulation. This method is
     * triggered by the timer and updates the angles incrementally until the target angles are reached.
     *
     * @param e the ActionEvent triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!simulate) {
            SIMULATION_TIMER.stop();
            return;
        }

        updateAngles();
        int progress = (int) ((phase / (double) CURRENT_ANGLES.length) * 100);
        BLACKBOARD.updateProgress(progress);
    }
}