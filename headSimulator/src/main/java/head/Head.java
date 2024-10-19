package head;

import java.awt.*;

/**
 * Head class that draws the head, eyes, and mouth of the character.
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Head {
	
	private static final Color HEAD_COLOR = new Color(232, 220, 202);
	private static final Color EYE_BACKGROUND_COLOR = Color.WHITE;
	private static final Color EYE_COLOR = Color.BLACK;
	private static final Color PUPIL_COLOR = Color.BLUE;
	private static final Color MOUTH_COLOR = new Color(255, 105, 180);
	
	private int headX;
	private int headY;
	private int mouseX;
	private int mouseY;
	private int eyeWidth;
	private int eyeSize;
	private int eyeDistance;
	private int width;
	private int height;
	
	public Head() {
		this.width = 150; // Increased width
		this.height = 200; // Increased height
		this.headX = 400;
		this.headY = 300;
		this.mouseX = 0;
		this.mouseY = 0;
		this.eyeWidth = 10; // Increased eye width
		this.eyeSize = 10; // Increased eye size
		this.eyeDistance = 30; // Increased eye distance
	}
	
	public void draw(Graphics g, int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
		double distance = Math.hypot(this.headX - this.mouseX, this.headY - this.mouseY);
		double xPos = (this.headX - this.mouseX) / distance * this.eyeWidth;
		double yPos = (this.headY - this.mouseY) / distance * this.eyeWidth;
		drawHead(g);
		drawEyes(g, xPos, yPos);
		drawMouth(g);
	}
	
	private void drawHead(Graphics g) {
		g.setColor(HEAD_COLOR);
		g.fillOval(this.headX - (this.width / 2), this.headY - (this.height / 2), this.width, this.height);
	}
	
	private void drawEyes(Graphics g, double xPos, double yPos) {
		g.setColor(EYE_BACKGROUND_COLOR);
		g.fillOval(this.headX - eyeDistance - 20, this.headY - 40, this.eyeSize + 40, this.eyeSize + 40);
		g.fillOval(this.headX + eyeDistance - 20, this.headY - 40, this.eyeSize + 40, this.eyeSize + 40);
		g.setColor(EYE_COLOR);
		g.fillOval(this.headX - (int) xPos - eyeDistance, this.headY - (int) yPos - 20, this.eyeSize, this.eyeSize);
		g.fillOval(this.headX - (int) xPos + eyeDistance, this.headY - (int) yPos - 20, this.eyeSize, this.eyeSize);
		g.setColor(PUPIL_COLOR);
		g.fillOval(this.headX - (int) xPos - eyeDistance + 4, this.headY - (int) yPos - 16, this.eyeSize - 8, this.eyeSize - 8);
		g.fillOval(this.headX - (int) xPos + eyeDistance + 4, this.headY - (int) yPos - 16, this.eyeSize - 8, this.eyeSize - 8);
	}
	
	private void drawMouth(Graphics g) {
		g.setColor(MOUTH_COLOR);
		g.fillArc(this.headX - 40, this.headY + 40, 80, 40, 0, -180);
	}

}
