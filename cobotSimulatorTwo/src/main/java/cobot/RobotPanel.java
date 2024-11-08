package cobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RobotPanel extends JPanel implements ActionListener, PropertyChangeListener {
    //Object for holding angles, generalizes better
        public record RobotAngles(int[] angles) {
            public RobotAngles(int[] angles) {
                this.angles = angles;
                if (this.angles.length != 6) {
                    //TODO: Adjust array so size is 6
                }
            }
        }

    private Timer simulationTimer;
    private boolean simulate = false;
    private RobotAngles angles;
    private int phase = 1;

    public RobotPanel() {
        // Setup RobotPanel (if additional setup is needed)
    }

    public void startSimulation() {
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
                if (adjustAngleTowardsTarget(0, angles.angles()[0])) {
                    phase = 2;
                }
                break;
            case 2:
                if (adjustAngleTowardsTarget(1, angles.angles()[1])) {
                    phase = 3;
                }
                break;
            case 3:
                if (adjustAngleTowardsTarget(2, angles.angles()[2])) {
                    phase = 4;
                }
                break;
            case 4:
                if (adjustAngleTowardsTarget(3, angles.angles()[3])) {
                    phase = 5;
                }
                break;
            case 5:
                if (adjustAngleTowardsTarget(4, angles.angles()[4])) {
                    phase = 6;
                }
                break;
            case 6:
                if (adjustAngleTowardsTarget(5, angles.angles()[5])) {
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
        int[] angles = this.angles.angles();
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

        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArm(g);
    }

    public void drawArm(Graphics g) {
        //TODO: do the drawing
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof RobotAngles) {
            if (!simulationTimer.isRunning()) { //only update once done simulating
                this.angles = (RobotAngles) evt.getNewValue();
            }
        }
    }
}
