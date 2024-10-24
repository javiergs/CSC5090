package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * The WorkArea class is responsible for managing the work area of the blackboard.
 * It is where the user can click to add points to the blackboard.
 *
 * @author Luca Ornstil,
 * @author Monish Suresh,
 * @author Christine Widden
 * @version 1.0
 */
public class WorkArea extends JPanel implements MouseListener {
	
	public WorkArea() {
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Point> clickPositions = Blackboard.getInstance().getClickPositions();
		for (Point clickPosition : clickPositions) {
			g.fillOval(clickPosition.x, clickPosition.y, 10, 10);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (Blackboard.getInstance().isTracking()) {
			Point clickPosition = e.getPoint();
        Blackboard.getInstance().addClick(clickPosition);
				repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
 
}
