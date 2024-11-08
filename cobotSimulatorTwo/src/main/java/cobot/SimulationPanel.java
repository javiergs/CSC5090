package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The SimulationPanel class is responsible for drawing the robot arm in the GUI.
 *
 */
public class SimulationPanel extends JPanel implements PropertyChangeListener {
	private JLabel staticStatusLabel;
	private JLabel dynamicStatusLabel;

	private static boolean RUNNING = false;
	public SimulationPanel() {
		staticStatusLabel = new JLabel("Status: ");
		staticStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
		staticStatusLabel.setForeground(Color.BLACK);  // Always black

		dynamicStatusLabel = new JLabel("Idle");
		dynamicStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
		dynamicStatusLabel.setForeground(Color.RED);

		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(staticStatusLabel);
		this.add(dynamicStatusLabel);
	}

	public static void startRunning() {
		RUNNING = true;
	}

	public static void stopRunning() {
		RUNNING = false;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (RUNNING) {
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawArm(g);
	}

	public void drawArm(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(6));

		int x1 = 300, y1 = 450;
		int length = 50;


		int x2 = x1 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[0])));
		int y2 = y1 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[0])));
		g2d.setColor(Color.decode("#6667ab"));
		g2d.drawLine(x1, y1, x2, y2);

		int x3 = x2 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[1])));
		int y3 = y2 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[1])));
		g2d.setColor(Color.decode("#f18aad"));
		g2d.drawLine(x2, y2, x3, y3);

		int x4 = x3 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[2])));
		int y4 = y3 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[2])));
		g2d.setColor(Color.decode("#ea6759"));
		g2d.drawLine(x3, y3, x4, y4);

		int x5 = x4 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[3])));
		int y5 = y4 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[3])));
		g2d.setColor(Color.decode("#f88f58"));
		g2d.drawLine(x4, y4, x5, y5);

		int x6 = x5 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[4])));
		int y6 = y5 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[4])));
		g2d.setColor(Color.decode("#f3c65f"));
		g2d.drawLine(x5, y5, x6, y6);

		int x7 = x6 + (int) (length * Math.cos(Math.toRadians(Blackboard.getInstance().getAngles()[5])));
		int y7 = y6 - (int) (length * Math.sin(Math.toRadians(Blackboard.getInstance().getAngles()[5])));
		g2d.setColor(Color.decode("#8bc28c"));
		g2d.drawLine(x6, y6, x7, y7);

		// Draw joints as blue circles
		g2d.setColor(Color.WHITE);
		g2d.fillOval(x1 - 5, y1 - 5, 10, 10); // Joint 1
		g2d.fillOval(x2 - 5, y2 - 5, 10, 10); // Joint 2
		g2d.fillOval(x3 - 5, y3 - 5, 10, 10); // Joint 3
		g2d.fillOval(x4 - 5, y4 - 5, 10, 10); // Joint 4
		g2d.fillOval(x5 - 5, y5 - 5, 10, 10); // Joint 5
		g2d.fillOval(x6 - 5, y6 - 5, 10, 10); // Joint 6
		g2d.fillOval(x7 - 5, y7 - 5, 10, 10); // End Effector
	}

}
