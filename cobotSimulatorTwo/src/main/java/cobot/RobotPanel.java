package cobot;

import javax.swing.*;
import java.awt.*;

/**
 * The RobotPanel class is a graphical component that displays the current status and simulated arm
 * movement of a robot. It listens to changes from the RobotPanelHandler's blackboard and updates the
 * robot's status accordingly.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class RobotPanel extends JPanel {
    private final JLabel staticStatusLabel;
    private final JLabel dynamicStatusLabel;
    private final RobotPanelHandler controller;

    /**
     * Constructs a new RobotPanel with a specified controller to manage robot status
     * updates and joint angles.
     *
     * @param controller the RobotPanelHandler that provides robot status and angle data
     */
    public RobotPanel(RobotPanelHandler controller) {
        this.controller = controller;

        staticStatusLabel = new JLabel("Status: ");
        staticStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        staticStatusLabel.setForeground(Color.BLACK);

        dynamicStatusLabel = new JLabel("Idle");
        dynamicStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dynamicStatusLabel.setForeground(Color.RED);

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(staticStatusLabel);
        add(dynamicStatusLabel);

        controller.blackboard.addPropertyChangeListener(evt -> {
            if ("ProgressUpdated".equals(evt.getPropertyName())) {
                setRunningStatus();
            } else if ("AnglesAdded".equals(evt.getPropertyName())) {
                setIdleStatus();
            }
            repaint();
        });
    }

    /**
     * Sets the dynamic status label to display "Running" and changes its color to green.
     */
    public void setRunningStatus() {
        dynamicStatusLabel.setText("Running");
        dynamicStatusLabel.setForeground(Color.decode("#008000"));
    }

    /**
     * Sets the dynamic status label to display "Idle" and changes its color to red.
     */
    public void setIdleStatus() {
        dynamicStatusLabel.setText("Idle");
        dynamicStatusLabel.setForeground(Color.RED);
    }

    /**
     * Paints the robot arm on the panel, including its segments and joints based on the current
     * joint angles.
     *
     * @param g the Graphics object used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArm(g);
    }

    /**
     * Draws each segment of the robot arm based on the current angles and the starting position.
     *
     * @param g the Graphics object used for drawing
     */
    private void drawArm(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(6));

        int x = 300, y = 450;
        int length = 50;

        Color[] colors = {Color.decode("#6667ab"), Color.decode("#f18aad"), Color.decode("#ea6759"), Color.decode("#f88f58"), Color.decode("#f3c65f"), Color.decode("#8bc28c")};

        int[] currentAngles = controller.getCurrentAngles();
        Point[] jointPositions = new Point[currentAngles.length + 1];
        jointPositions[0] = new Point(x, y);

        for (int i = 0; i < currentAngles.length; i++) {
            g2d.setColor(colors[i]);

            Point newEndPoint = drawSegment(g2d, x, y, length, currentAngles[i]);
            x = newEndPoint.x;
            y = newEndPoint.y;

            jointPositions[i + 1] = newEndPoint;
        }

        for (Point joint : jointPositions) {
            drawJoint(g2d, joint.x, joint.y);
        }
    }

    /**
     * Draws a single segment of the robot arm based on a starting point, length, and angle.
     *
     * @param g2d    the Graphics2D object used for drawing
     * @param x1     the x-coordinate of the starting point
     * @param y1     the y-coordinate of the starting point
     * @param length the length of the segment
     * @param angle  the angle of the segment in degrees
     * @return the endpoint of the segment as a Point object
     */
    private Point drawSegment(Graphics2D g2d, int x1, int y1, int length, int angle) {
        int x2 = x1 + (int) (length * Math.cos(Math.toRadians(angle)));
        int y2 = y1 - (int) (length * Math.sin(Math.toRadians(angle)));
        g2d.drawLine(x1, y1, x2, y2);
        return new Point(x2, y2);
    }

    /**
     * Draws a joint (represented as a small white circle) at the specified coordinates.
     *
     * @param g2d the Graphics2D object used for drawing
     * @param x   the x-coordinate of the joint
     * @param y   the y-coordinate of the joint
     */
    private void drawJoint(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 5, y - 5, 10, 10);
    }
}