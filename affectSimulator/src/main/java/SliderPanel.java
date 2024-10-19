import javax.swing.*;
import java.awt.*;

/**
 * Panel with sliders for the user to adjust the values of the affective states.
 * The panel contains sliders for focus, stress, engagement, excitement, and interest.
 * The sliders have a range of 0-100 and display tick marks and labels.
 *
 * @author Yayun Tan
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class SliderPanel extends JPanel {
	
	public static final String[] names = {"Focus", "Stress", "Engagement", "Excitement", "Interest"};
	
	public SliderPanel() {
		setLayout(new GridLayout(5, 2));
		for (String name : names) {
			JLabel label = new JLabel(name);
			JSlider slider = new JSlider(0, 100);
			slider.setMajorTickSpacing(100);
			slider.setPaintLabels(true);
			add(label, BorderLayout.WEST);
			add(slider, BorderLayout.CENTER);
		}
	}
	
}
