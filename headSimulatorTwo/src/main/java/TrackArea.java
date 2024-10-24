import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The TrackArea class is responsible for drawing the face and eyes in the GUI.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class TrackArea extends JPanel implements PropertyChangeListener {
	
	private int leftX = 315;
	private int leftY = 215;
	private int rightX = 465;
	private int rightY = 215;
	private int latestX, latestY;  // To store the latest coordinates
	
	public TrackArea(Server server) {
		setSize(800, 500);
		setVisible(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		Controller c = new Controller(this, server);
		addMouseMotionListener(c);
		BorderLayout b = new BorderLayout();
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 5);
		setBorder(blackLine);
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
		int leftEyeCenterX = 275 + 50; // Center of the left eye
		int leftEyeCenterY = 175 + 50;
		int rightEyeCenterX = 425 + 50; // Center of the right eye
		int rightEyeCenterY = 175 + 50;
		int eyeRadius = 50;
		// Move the left pupil within the left eye
		int[] constrainedLeftPupil = constrainToCircle(x, y, leftEyeCenterX, leftEyeCenterY, eyeRadius - 10);
		leftX = constrainedLeftPupil[0] - 10; // Adjust for pupil size (20x20)
		leftY = constrainedLeftPupil[1] - 10;
		// Move the right pupil within the right eye
		int[] constrainedRightPupil = constrainToCircle(x, y, rightEyeCenterX, rightEyeCenterY, eyeRadius - 10);
		rightX = constrainedRightPupil[0] - 10; // Adjust for pupil size (20x20)
		rightY = constrainedRightPupil[1] - 10;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw the face
		g.setColor(new Color(255, 233, 0));
		g.fillOval(200, 100, 400, 400);
		// Draw the eyes
		g.setColor(Color.WHITE);
		g.fillOval(275, 175, 100, 100); // left eye
		g.fillOval(425, 175, 100, 100); // right eye
		// Draw the mouth
		g.setColor(Color.WHITE);
		g.fillOval(275, 350, 250, 50);
		// Draw the pupils
		g.setColor(Color.BLACK);
		g.fillOval(leftX, leftY, 20, 20);  // Left pupil
		g.fillOval(rightX, rightY, 20, 20); // Right pupil
		// Draw the latest coordinates (for debugging or visualization)
		g.setColor(Color.BLACK);
		g.drawString("Latest Point: (" + latestX + ", " + latestY + ")", 50, 50);  // Display the latest x, y on the panel
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
	
}