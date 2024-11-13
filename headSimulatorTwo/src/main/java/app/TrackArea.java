package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The `TrackArea` class is a visual component that displays a face with eyes that
 * track the mouse cursor. It receives eye tracking data and visualizes it by
 * moving the pupils within the eyes. It also interacts with a `Blackboard`
 * object to display status information.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class TrackArea extends JPanel implements Blackboard.Observer {

	private int leftX = 315;
	private int leftY = 215;
	private int rightX = 465;
	private int rightY = 215;
	private int latestX, latestY;
	private String drawingState;

	/**
	 * Constructs a `TrackArea` object.
	 *
	 * @param server The server managing the WebSocket connection.
	 * @param menu The dropdown menu for controlling the simulation.
	 * @param blackboard The `Blackboard` object for displaying status information.
	 */
	public TrackArea(Publisher server, JComboBox<String> menu, Blackboard blackboard) {
		setSize(800, 500);
		setVisible(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		Controller c = new Controller(this, server, menu);
		addMouseMotionListener(c);
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 5);
		setBorder(blackLine);
		blackboard.addObserver(this);
		this.drawingState = blackboard.getDrawingState();

		DataRepository.getInstance().addPropertyChangeListener(new DataPointListener(this));
	}

	/**
	 * Constrains a point (x, y) to be within a circle defined by its center and radius.
	 *
	 * @param x The x-coordinate of the point.
	 * @param y The y-coordinate of the point.
	 * @param centerX The x-coordinate of the circle's center.
	 * @param centerY The y-coordinate of the circle's center.
	 * @param radius The radius of the circle.
	 * @return An array containing the constrained x and y coordinates.
	 */
	private int[] constrainToCircle(int x, int y, int centerX, int centerY, int radius) {
		int dx = x - centerX;
		int dy = y - centerY;
		double distance = Math.sqrt(dx * dx + dy * dy);
		if (distance <= radius) {
			return new int[]{x, y};
		}
		double ratio = radius / distance;
		int constrainedX = centerX + (int) (dx * ratio);
		int constrainedY = centerY + (int) (dy * ratio);
		return new int[]{constrainedX, constrainedY};
	}

	/**
	 * Moves the pupils of the eyes based on the mouse position.
	 *
	 * @param x The x-coordinate of the mouse.
	 * @param y The y-coordinate of the mouse.
	 */
	public void moveEyes(int x, int y) {
		int leftEyeCenterX = 275 + 50;
		int leftEyeCenterY = 175 + 50;
		int rightEyeCenterX = 425 + 50;
		int rightEyeCenterY = 175 + 50;
		int eyeRadius = 50;

		int[] constrainedLeftPupil = constrainToCircle(x, y, leftEyeCenterX, leftEyeCenterY, eyeRadius - 10);
		leftX = constrainedLeftPupil[0] - 10;
		leftY = constrainedLeftPupil[1] - 10;

		int[] constrainedRightPupil = constrainToCircle(x, y, rightEyeCenterX, rightEyeCenterY, eyeRadius - 10);
		rightX = constrainedRightPupil[0] - 10;
		rightY = constrainedRightPupil[1] - 10;
		repaint();
	}

	/**
	 * Draws the face, eyes, and pupils on the component.
	 *
	 * @param g The `Graphics` object used for drawing.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(255, 233, 0)); // Yellow face color
		g.fillOval(200, 100, 400, 400); // Face

		g.setColor(Color.WHITE);
		g.fillOval(275, 175, 100, 100); // Left eye
		g.fillOval(425, 175, 100, 100); // Right eye

		g.setColor(Color.WHITE);
		g.fillOval(275, 350, 250, 50); // Mouth

		g.setColor(Color.BLACK);
		g.fillOval(leftX, leftY, 20, 20); // Left pupil
		g.fillOval(rightX, rightY, 20, 20); // Right pupil

		// Display latest point and drawing state
		g.setColor(Color.BLACK);
		g.drawString("Latest Point: (" + latestX + ", " + latestY + ")", 50, 50);
		g.drawString("Drawing State: " + drawingState, 50, 70);
	}


	/**
	 * Updates the drawing state from the `Blackboard`.
	 *
	 * @param drawingState The new drawing state.
	 */
	@Override
	public void update(String drawingState) {
		this.drawingState = drawingState;
		repaint();
	}

	/**
	 * Updates the latest point coordinates when a new point is received.
	 *
	 * @param x The x-coordinate of the new point.
	 * @param y The y-coordinate of the new point.
	 */
	public void updateLatestPoint(int x, int y) {
		latestX = x;
		latestY = y;
		repaint();
	}
}