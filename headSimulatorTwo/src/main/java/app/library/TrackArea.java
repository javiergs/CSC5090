package app.library;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 /**
 * The `TrackArea` class is a visual component that displays a face with eyes that
 * track the mouse cursor. It receives eye tracking data from a `DataRepository` and
 * visualizes it by moving the pupils within the eyes. It also interacts with a
 * `Blackboard` object to display status information.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class TrackArea extends JPanel implements Blackboard.Observer, PropertyChangeListener {

	private int leftX = 315;
	private int leftY = 215;
	private int rightX = 465;
	private int rightY = 215;
	private int latestX, latestY;
	private String drawingState;

	public TrackArea(Server server, JComboBox<String> menu, Blackboard blackboard) {
		setSize(800, 500);
		setVisible(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		Controller c = new Controller(this, server, menu);
		addMouseMotionListener(c);
		BorderLayout b = new BorderLayout();
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 5);
		setBorder(blackLine);

		// Register as observer to the app.library.Blackboard
		blackboard.addObserver(this);
		this.drawingState = blackboard.getDrawingState();  // Initial state
	}

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(255, 233, 0));
		g.fillOval(200, 100, 400, 400);

		g.setColor(Color.WHITE);
		g.fillOval(275, 175, 100, 100);
		g.fillOval(425, 175, 100, 100);

		g.setColor(Color.WHITE);
		g.fillOval(275, 350, 250, 50);

		g.setColor(Color.BLACK);
		g.fillOval(leftX, leftY, 20, 20);
		g.fillOval(rightX, rightY, 20, 20);

		g.setColor(Color.BLACK);
		g.drawString("Latest Point: (" + latestX + ", " + latestY + ")", 50, 50);
		g.drawString("Drawing State: " + drawingState, 50, 70);  // Display drawing state
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("newPoint".equals(evt.getPropertyName())) {
			int[] point = (int[]) evt.getNewValue();
			latestX = point[0];
			latestY = point[1];
			repaint();
		}
	}

	// updates app.library.Blackboard
	@Override
	public void update(String drawingState) {
		this.drawingState = drawingState;
		repaint();
	}
}