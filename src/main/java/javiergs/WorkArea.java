package javiergs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WorkArea extends JPanel implements MouseListener {
    private List<Double> armAngles;
    private List<Double> targetAngles; // for smooth animation
    private List<Color> armColors;
    private static int armCount = 5;
    private static int armLength = 80;  // (updated via server)
    private static final int armThickness = 8;
    private static final int jointRadius = 10;
    private Timer animationTimer;
    private static final int animationSpeed = 10; // higher = slower
    private Point clickPoint = null; // store the point where the user clicked
    private Point origin; // origin of the work area (center of the window)
    private Client client; // to send data to server
    private PropertyChangeListener listener;
    private static final Logger LOGGER = Logger.getLogger(WorkArea.class.getName());

    public WorkArea() {
        addMouseListener(this);
        initializeArms();
        startAnimation();

        // Detect window resizing and send the updated origin to the server
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateOrigin();
            }
        });

        updateOrigin(); // Set the initial origin
    }

    void startClient() {
        if (client == null) {
            client = new Client(this);
            client.addPropertyChangeListener(listener);
        } else {
            client.setRunning(true);
        }
        new Thread(client).start();
    }

    void pauseClient() {
        if (client == null) {
            LOGGER.warning("No client initialized");
        } else {
            client.setRunning(false);
        }
    }

    private void updateOrigin() {
        origin = new Point(getWidth() / 2, getHeight() / 2);
        sendOriginToServer();
    }

    private void sendOriginToServer() {
        if (client != null) {
            String originMessage = "ORIGIN " + origin.x + " " + origin.y;
            client.sendOriginData(origin);
            LOGGER.info("Updated origin sent to server: " + originMessage);
        }
    }

    private void startAnimation() {
        animationTimer = new Timer(30, e -> updateArmAngles());
        animationTimer.start();
    }

    // initialize arms with default or received server properties
    public void initializeArms() {
        armAngles = new ArrayList<>();
        targetAngles = new ArrayList<>();
        armColors = new ArrayList<>();
        Random random = new Random();

        // initialize angles and colors for the arms
        for (int i = 0; i < armCount; i++) {
            armAngles.add(0.0);
            targetAngles.add(0.0);
            armColors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

    public void updateArmProperties(int newArmCount, int newArmLength) {
        armCount = newArmCount;
        armLength = newArmLength;
        initializeArms();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawCobot(g2d);
    }

    private void drawCobot(Graphics2D g2d) {
        drawXYAxes(g2d);
        drawReachableRadius(g2d);
        drawUserClick(g2d);

        // first joint at center of window
        Point prevPoint = origin;
        double cumulativeAngle = 0;

        for (int i = 0; i < armAngles.size(); i++) {
            cumulativeAngle += armAngles.get(i);
            prevPoint = drawArmAndJoint(g2d, prevPoint, cumulativeAngle, armColors.get(i));
        }

        drawJoint(g2d, prevPoint); // final joint at end of last arm
    }

    private void drawUserClick(Graphics2D g2d) {
        // Draw the point where the user clicked, if available
        if (clickPoint != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(clickPoint.x - 5, clickPoint.y - 5, 10, 10);
        }
    }

    private void drawReachableRadius(Graphics2D g2d) {
        int totalArmLength = armCount * armLength;

        g2d.setColor(new Color(200, 200, 200, 100)); // Light gray, semi-transparent
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(origin.x - totalArmLength, origin.y - totalArmLength, totalArmLength * 2, totalArmLength * 2);
    }

    private void drawXYAxes(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));
        // X-axis
        g2d.drawLine(0, origin.y, getWidth(), origin.y);
        // Y-axis
        g2d.drawLine(origin.x, 0, origin.x, getHeight());
    }

    private Point drawArmAndJoint(Graphics2D g2d, Point prevPoint, double angle, Color armColor) {
        // calc new endpoint
        int endX = (int) (prevPoint.x + armLength * Math.cos(Math.toRadians(angle)));
        int endY = (int) (prevPoint.y + armLength * Math.sin(Math.toRadians(angle)));
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
        if (armIndex >= 0 && armIndex < armAngles.size()) {
            targetAngles.set(armIndex, (double) angle);
        }
    }

    private void updateArmAngles() {
        boolean repaintNeeded = false;

        for (int i = 0; i < armAngles.size(); i++) {
            double currentAngle = armAngles.get(i);
            double targetAngle = targetAngles.get(i);

            if (Math.abs(currentAngle - targetAngle) > 0.1) {
                repaintNeeded = true;

                if (currentAngle < targetAngle) {
                    armAngles.set(i, Math.min(currentAngle + animationSpeed * 0.1, targetAngle));
                } else if (currentAngle > targetAngle) {
                    armAngles.set(i, Math.max(currentAngle - animationSpeed * 0.1, targetAngle));
                }
            }
        }
        if (repaintNeeded) repaint();
    }

    public int getArmCount() {
        return armAngles.size();
    }

    public void toggleMode() {
        client.toggleMode();
    }
    public String getCurrentMode() {
        return client.getCurrentMode();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (client.getOut() != null) {
            int x = e.getX();
            int y = e.getY();

            client.sendMouseClickData(new Point(x, y));
            LOGGER.info("Clicked at: (" + x + ", " + y + ")");

            // Store the click point and repaint component
            clickPoint = new Point(x, y);
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (client == null) this.listener = listener;
        else client.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        client.removePropertyChangeListener(listener);
    }
}
