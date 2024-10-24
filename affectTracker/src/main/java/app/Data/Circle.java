package app.Data;

import app.Model.Blackboard;

import java.awt.*;

/**
 * The {@code Circle} class represents a circle with a specified center (x, y), color, and radius.
 * This class is used for drawing and managing circle objects in the graphical interface.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class Circle {
	
	private final int xCoord;
	private final int yCoord;
	private Color color;
	private int radius;
	
	public Circle(int xCoord, int yCoord, Color color, int radius) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.color = color;
		this.radius = radius;
	}
	
	public int getX() {
		return xCoord;
	}
	
	public int getY() {
		return yCoord;
	}
 
	public void increaseRadius(int increment) {
		this.radius += increment;
	}
	
	public void drawCircle(Graphics g) {
		if (xCoord - radius >= Blackboard.paddingFromTop) {
			g.setColor(color);
			g.fillOval(xCoord - radius, yCoord - radius, 2 * radius, 2 * radius);
			g.setColor(Color.BLACK);
			g.drawOval(xCoord - radius, yCoord - radius, 2 * radius, 2 * radius);
		}
	}
	
}
