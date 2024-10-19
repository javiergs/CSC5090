package head;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class creates the canvas where the head will be drawn
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 *
 * @version 2.0
 */
public class Canvas extends JPanel implements MouseMotionListener, ActionListener {
	
	private Head head;
	
	public Canvas() {
		head = new Head();
		setBackground(Color.GRAY);
		addMouseMotionListener(this);
		Timer timer = new Timer(1000 / 30, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point p = Blackboard.getInstance().getPoint();
		if (p != null) head.draw (g, p.x, p.y);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Blackboard.getInstance().setPoint(new Point(e.getX(), e.getY()));
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
}