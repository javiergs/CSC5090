package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class WorkArea extends JPanel implements MouseListener, BlackboardObserver {

    private static final Logger logger = LoggerFactory.getLogger(WorkArea.class);
    private EventManager eventManager;

    public WorkArea(EventManager eventManager) {
        this.eventManager = eventManager;
        eventManager.addObserver(this);
        addMouseListener(this);
        logger.debug("WorkArea initialized and observing EventManager.");
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
            eventManager.notifyObservers();
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
    public void update() {
        repaint();
    }
}
