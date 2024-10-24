package app.Data;

import java.awt.*;


/**
 * Enum representing various emotions that can be processed.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public enum Emotion {
 
	NONE (Color.GRAY, -1),
	FOCUS (Color.YELLOW, 0),
	STRESS (Color.RED, 1),
	ENGAGEMENT (Color.BLUE, 2),
	EXCITEMENT (Color.GREEN, 3),
	INTEREST (Color.MAGENTA, 4);
	
	private final Color color;
	private final int value;
	
	Emotion(Color color, int value) {
		this.color = color;
		this.value = value;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Emotion getByValue(int number) {
		for (Emotion e : Emotion.values()) {
			if (e.getValue() == number) {
				return e;
			}
		}
		return null;
	}
	
}