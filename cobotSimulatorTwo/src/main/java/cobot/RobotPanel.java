package cobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RobotPanel extends JPanel implements ActionListener {

    private Timer simulationTimer;
    private boolean simulate = false;
    private int[] targetAngles = new int[6];
    private int phase = 1;

    public RobotPanel() {
        // Setup RobotPanel (if additional setup is needed)
    }

    public void startSimulation() {
        if (AngleBlackboard.getInstance().getAngles() == null) {
            JOptionPane.showMessageDialog(this, "No angles to simulate.");
            return;
        }
        simulate = true;
        phase = 1;
        runSimulation();
    }

    public void stopSimulation() {
        if (simulationTimer != null && simulationTimer.isRunning()) {
            simulationTimer.stop();
        }
        simulate = false;
    }

    private void runSimulation() {
        int[] angles = AngleBlackboard.getInstance().getAngles();
        System.arraycopy(angles, 0, targetAngles, 0, angles.length);

        final int delay = 50;  // Delay for smooth animation
        simulationTimer = new Timer(delay, this);
        simulationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!simulate) {
            simulationTimer.stop();
            return;
        }

        updateAngles();
        repaint();  // Repaint to reflect changes
    }

    private void updateAngles() {
        boolean finished = false;

        switch (phase) {
            case 1:
                if (adjustAngleTowardsTarget(0, targetAngles[0])) {
                    phase = 2;
                }
                break;
            case 2:
                if (adjustAngleTowardsTarget(1, targetAngles[1])) {
                    phase = 3;
                }
                break;
            case 3:
                if (adjustAngleTowardsTarget(2, targetAngles[2])) {
                    phase = 4;
                }
                break;
            case 4:
                if (adjustAngleTowardsTarget(3, targetAngles[3])) {
                    phase = 5;
                }
                break;
            case 5:
                if (adjustAngleTowardsTarget(4, targetAngles[4])) {
                    phase = 6;
                }
                break;
            case 6:
                if (adjustAngleTowardsTarget(5, targetAngles[5])) {
                    finished = true;
                }
                break;
        }

        if (finished) {
            simulationTimer.stop();
            JOptionPane.showMessageDialog(this, "All angle sets have been simulated!");
        }
    }

    private boolean adjustAngleTowardsTarget(int index, int targetAngle) {
        int[] angles = AngleBlackboard.getInstance().getAngles();
        int currentAngle = angles[index];
        int step = 1;  // Step size for smoother animation

        if (Math.abs(currentAngle - targetAngle) <= step) {
            angles[index] = targetAngle;
            return true;
        }

        if (currentAngle < targetAngle) {
            angles[index] += step;
        } else if (currentAngle > targetAngle) {
            angles[index] -= step;
        }

        AngleBlackboard.getInstance().setAngles(angles);  // Update angles on the Blackboard
        return false;
    }
}
