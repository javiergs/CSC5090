package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * CobotPanel class to draw the robot arm and animate it.
 *
 * @author Jack Ortega,
 * @author Neeraja Beesetti,
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class CobotPanel extends JPanel implements PropertyChangeListener {
	
	private static final int armThickness = 8;
	private static final int jointRadius = 10;
	private Timer animationTimer;
	private static final int animationSpeed = 10; // higher = slower
	
	public CobotPanel() {
		animationTimer = new Timer(30, e -> updateArmAngles());
		animationTimer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawXYAxes(g2d);
		drawReachableRadius(g);
		// first joint at center of window
		Point prevPoint = Blackboard.getInstance().getCenter();
		double cumulativeAngle = 0;
		for (int i = 0; i < Blackboard.getInstance().getArmAngles().size(); i++) {
			cumulativeAngle += Blackboard.getInstance().getArmAngles().get(i);
			prevPoint = drawArmAndJoint(g2d, prevPoint, cumulativeAngle, Blackboard.getInstance().getArmColors().get(i));
		}
		drawJoint(g2d, prevPoint); // final joint at end of last arm
	}
	
	private void drawReachableRadius(Graphics g) {
		int totalArmLength = Blackboard.getInstance().ARM_COUNT * Blackboard.getInstance().ARM_LENGTH;
		g.setColor(new Color(200, 200, 200, 100));
		Point center = Blackboard.getInstance().getCenter();
		g.drawOval(center.x - totalArmLength, center.y - totalArmLength, totalArmLength * 2, totalArmLength * 2);
	}
	
	private void drawXYAxes(Graphics2D g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setStroke(new BasicStroke(2));
		// X-axis
		g2d.drawLine(0, Blackboard.getInstance().getCenter().y, getWidth(), Blackboard.getInstance().getCenter().y);
		// Y-axis
		g2d.drawLine(Blackboard.getInstance().getCenter().x, 0, Blackboard.getInstance().getCenter().x, getHeight());
	}
	
	private Point drawArmAndJoint(Graphics2D g2d, Point prevPoint, double angle, Color armColor) {
		// calc new endpoint
		int endX = (int) (prevPoint.x + Blackboard.getInstance().ARM_LENGTH * Math.cos(Math.toRadians(angle)));
		int endY = (int) (prevPoint.y + Blackboard.getInstance().ARM_LENGTH * Math.sin(Math.toRadians(angle)));
		Point endPoint = new Point(endX, endY);
		drawArm(g2d, prevPoint, endPoint, armColor);
		drawJoint(g2d, prevPoint);
		return endPoint;
	}
	
	private void drawArm(Graphics2D g2d, Point prevPoint, Point endPoint, Color armColor) {
		g2d.setColor(armColor);
		g2d.setStroke(new BasicStroke(armThickness));
		g2d.drawLine(prevPoint.x, prevPoint.y, endPoint.x, endPoint.y);
	}
	
	private void drawJoint(Graphics2D g2d, Point point) {
		g2d.setColor(Color.GRAY);
		g2d.fillOval(point.x - jointRadius / 2, point.y - jointRadius / 2, jointRadius, jointRadius);
	}
	
	public void updateArmAngle(int armIndex, int angle) {
		if (armIndex >= 0 && armIndex < Blackboard.getInstance().getArmAngles().size()) {
			Blackboard.getInstance().getTargetAngles().set(armIndex, (double) angle);
		}
	}
	
	private void updateArmAngles() {
		boolean repaintNeeded = false;
		for (int i = 0; i < Blackboard.getInstance().getArmAngles().size(); i++) {
			double currentAngle = Blackboard.getInstance().getArmAngles().get(i);
			double targetAngle = Blackboard.getInstance().getTargetAngles().get(i);
			if (Math.abs(currentAngle - targetAngle) > 0.1) {
				repaintNeeded = true;
				if (currentAngle < targetAngle) {
					Blackboard.getInstance().getArmAngles().set(i, Math.min(currentAngle + animationSpeed * 0.1, targetAngle));
				} else if (currentAngle > targetAngle) {
					Blackboard.getInstance().getArmAngles().set(i, Math.max(currentAngle - animationSpeed * 0.1, targetAngle));
				}
			}
		}
		if (repaintNeeded) repaint();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}
	
}