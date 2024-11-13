package cobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RobotPanelHandler implements ActionListener, PropertyChangeListener {

    private final Blackboard BLACKBOARD;
    private final Timer SIMULATION_TIMER;
    private int[] targetAngles;
    private int phase = 0;
    private boolean simulate = false;
    private final int[] CURRENT_ANGLES = new int[6];

    public RobotPanelHandler() {
        this.BLACKBOARD = Blackboard.getInstance();
        this.BLACKBOARD.addPropertyChangeListener(this);
        SIMULATION_TIMER = new Timer(20, this);
    }

    public void startSimulation() {
        if (targetAngles != null) {
            simulate = true;
            phase = 0;
            SIMULATION_TIMER.start();
        }
    }

    public void stopSimulation() {
        if (SIMULATION_TIMER.isRunning()) {
            SIMULATION_TIMER.stop();
        }
        simulate = false;
    }

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

    private double getAngleProgress(int index) {
        int targetAngle = targetAngles[index];
        int currentAngle = CURRENT_ANGLES[index];
        int totalDelta = Math.abs(targetAngle - currentAngle);

        return totalDelta == 0 ? 1 : (1 - (double) Math.abs(currentAngle - targetAngle) / totalDelta);
    }

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

    public void setTargetAngles(int[] angles) {
        if (angles.length != 6) {
            throw new IllegalArgumentException("Angles array must have exactly 6 elements.");
        }
        this.targetAngles = angles;
    }

    public int[] getCurrentAngles() {
        return CURRENT_ANGLES;
    }

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