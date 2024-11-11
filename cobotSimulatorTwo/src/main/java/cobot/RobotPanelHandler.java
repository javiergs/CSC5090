package cobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RobotPanelHandler implements ActionListener, PropertyChangeListener {

    final Blackboard blackboard;
    private final Timer simulationTimer;
    private int[] targetAngles;
    private int phase = 0;
    private boolean simulate = false;
    private final int[] currentAngles = new int[6];

    public RobotPanelHandler() {
        this.blackboard = Blackboard.getInstance();
        this.blackboard.addPropertyChangeListener(this);
        simulationTimer = new Timer(20, this);
    }

    public void startSimulation() {
        if (targetAngles != null) {
            simulate = true;
            phase = 0;
            simulationTimer.start();
        }
    }

    public void stopSimulation() {
        if (simulationTimer.isRunning()) {
            simulationTimer.stop();
        }
        simulate = false;
    }

    private void updateAngles() {
        boolean reachedTarget = adjustAngleTowardsTarget(phase, targetAngles[phase]);

        if (reachedTarget) {
            phase++;
            if (phase >= currentAngles.length) {
                phase = 0;
                stopSimulation();
                blackboard.updateProgress(100);
                JOptionPane.showMessageDialog(null, "All angles have been simulated!");
                return;
            }
        }

        int progress = (int) (((phase + getAngleProgress(phase)) / (double) currentAngles.length) * 100);
        blackboard.updateProgress(progress);

    }

    private double getAngleProgress(int index) {
        int targetAngle = targetAngles[index];
        int currentAngle = currentAngles[index];
        int totalDelta = Math.abs(targetAngle - currentAngle);

        return totalDelta == 0 ? 1 : (1 - (double) Math.abs(currentAngle - targetAngle) / totalDelta);
    }

    private boolean adjustAngleTowardsTarget(int index, int targetAngle) {
        int currentAngle = currentAngles[index];
        int step = 1;

        if (Math.abs(currentAngle - targetAngle) <= step) {
            currentAngles[index] = targetAngle;
            return true;
        }
        currentAngles[index] += (currentAngle < targetAngle) ? step : -step;
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
        return currentAngles;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!simulate) {
            simulationTimer.stop();
            return;
        }

        updateAngles();
        int progress = (int) ((phase / (double) currentAngles.length) * 100);
        blackboard.updateProgress(progress);
    }
}