package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * JPanel that displays click positions on the UI and handles mouse clicks
 * for tracking positions.
 *
 * @version 1.2
 * @authors
 * Monish Suresh
 * Christine Widden
 * Luca Ornstil
 */

public class WorkArea extends JPanel implements MouseListener, PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkArea.class);

    public WorkArea() {
        Blackboard.getInstance().addObserver(this);
        addMouseListener(this);
        logger.debug("WorkArea initialized and observing Blackboard.");
    }

    @Override
    protected void paintComponent(Graphics g) {
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
            logger.debug("Click recorded at position ({}, {}).", clickPosition.x, clickPosition.y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Work Area detected property change on property {}", evt.getPropertyName());
        repaint();
    }
}
