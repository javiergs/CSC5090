package cobot;

import cobot.blackboard.Blackboard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CobotPanel class to draw the robot arm and animate it.
 *
 * Author(s): Jack Ortega, Neeraja Beesetti, Saanvi Dua, Javier Gonzalez-Sanchez
 * Version: 2.0
 */
public class CobotPanel extends JPanel implements PropertyChangeListener {

	private static final int ARM_THICKNESS = 8;
	private static final int JOINT_RADIUS = 10;
	private static final int ANIMATION_STEP_DELAY = 30;
	private static final double TRANSITION_SPEED = 0.05; // 5% of the way per step

	private final Queue<int[]> animationQueue = new LinkedList<>(); // Queue for incoming commands
	private Timer animationTimer; // Timer for smooth animations

	public CobotPanel() {
		// Initialize and start the animation timer
		animationTimer = new Timer();
		animationTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateArmAngles();
			}
		}, 0, ANIMATION_STEP_DELAY);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawXYAxes(g2d);
		drawReachableRadius(g);

		Point prevPoint = Blackboard.getInstance().getCenter();
		double cumulativeAngle = 0;
		for (int i = 0; i < Blackboard.getInstance().getArmAngles().size(); i++) {
			cumulativeAngle += Blackboard.getInstance().getArmAngles().get(i);
			prevPoint = drawArmAndJoint(g2d, prevPoint, cumulativeAngle, Blackboard.getInstance().getArmColors().get(i));
		}
		drawJoint(g2d, prevPoint); // Final joint at end of last arm
	}

	private void drawReachableRadius(Graphics g) {
		int totalArmLength = Blackboard.getInstance().getArmCount() * Blackboard.getInstance().getArmLength();
		g.setColor(new Color(200, 200, 200, 100));
		Point center = Blackboard.getInstance().getCenter();
		g.drawOval(center.x - totalArmLength, center.y - totalArmLength, totalArmLength * 2, totalArmLength * 2);
	}

	private void drawXYAxes(Graphics2D g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(0, Blackboard.getInstance().getCenter().y, getWidth(), Blackboard.getInstance().getCenter().y); // X-axis
		g2d.drawLine(Blackboard.getInstance().getCenter().x, 0, Blackboard.getInstance().getCenter().x, getHeight()); // Y-axis
	}

	private Point drawArmAndJoint(Graphics2D g2d, Point prevPoint, double angle, Color armColor) {
		int endX = (int) (prevPoint.x + Blackboard.getInstance().getArmLength() * Math.cos(Math.toRadians(angle)));
		int endY = (int) (prevPoint.y + Blackboard.getInstance().getArmLength() * Math.sin(Math.toRadians(angle)));
		Point endPoint = new Point(endX, endY);
		drawArm(g2d, prevPoint, endPoint, armColor);
		drawJoint(g2d, prevPoint);
		return endPoint;
	}

	private void drawArm(Graphics2D g2d, Point prevPoint, Point endPoint, Color armColor) {
		g2d.setColor(armColor);
		g2d.setStroke(new BasicStroke(ARM_THICKNESS));
		g2d.drawLine(prevPoint.x, prevPoint.y, endPoint.x, endPoint.y);
	}

	private void drawJoint(Graphics2D g2d, Point point) {
		g2d.setColor(Color.GRAY);
		g2d.fillOval(point.x - JOINT_RADIUS / 2, point.y - JOINT_RADIUS / 2, JOINT_RADIUS, JOINT_RADIUS);
	}

	/**
	 * Queues a new target angle set for animation.
	 *
	 * @param targetAngles The target angles to be queued.
	 */
	private void queueArmAngles(int[] targetAngles) {
		animationQueue.offer(targetAngles); // Add command to the queue
	}

	/**
	 * Updates the arm angles gradually toward the next target in the queue.
	 */
	private void updateArmAngles() {
		if (animationQueue.isEmpty()) return;

		int[] targetAngles = animationQueue.peek();
		boolean reachedTarget = true;

		for (int i = 0; i < Blackboard.getInstance().getArmAngles().size(); i++) {
			double currentAngle = Blackboard.getInstance().getArmAngles().get(i);
			double targetAngle = targetAngles[i];
			double delta = (targetAngle - currentAngle) * TRANSITION_SPEED;

			if (Math.abs(delta) > 0.1) {
				reachedTarget = false;
				currentAngle += delta;
				Blackboard.getInstance().getArmAngles().set(i, currentAngle);
			} else {
				Blackboard.getInstance().getArmAngles().set(i, targetAngle);
			}
		}

		if (reachedTarget) {
			animationQueue.poll(); // Remove the command if target is reached
		}

		repaint();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("armAngles")) {
			queueArmAngles((int[]) evt.getNewValue()); // Queue new command for animation
		} else if (evt.getPropertyName().equals("origin")) {
			repaint(); // Repaint on origin change
		}
	}
}
