package app.View;

import java.awt.*;
import javax.swing.*;

/**
 * The {@code ColorKeyPanel} class represents a panel that displays a key of emotion labels
 * and their corresponding colors. This panel provides a visual reference for users to understand
 * which colors correspond to specific emotions in the graphical interface.
 * Each label is paired with a colored square representing the emotion.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class ColorKeyPanel extends JPanel {
	
	public ColorKeyPanel() {
		setLayout(new GridLayout(0, 2));
		
		add(createCenteredLabel("None"));
		add(createColorLabel(Color.GRAY));
		
		add(createCenteredLabel("Focus"));
		add(createColorLabel(Color.YELLOW));
		
		add(createCenteredLabel("Stress"));
		add(createColorLabel(Color.RED));
		
		add(createCenteredLabel("Engagement"));
		add(createColorLabel(Color.BLUE));
		
		add(createCenteredLabel("Excitement"));
		add(createColorLabel(Color.GREEN));
		
		add(createCenteredLabel("Interest"));
		add(createColorLabel(Color.MAGENTA));
	}
	
	private JLabel createCenteredLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
	private JLabel createColorLabel(Color color) {
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setBackground(color);
		label.setPreferredSize(new Dimension(20, 20)); // size of color square
		return label;
	}
	
}
