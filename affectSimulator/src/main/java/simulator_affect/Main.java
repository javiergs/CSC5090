package simulator_affect;

import javax.swing.*;
import java.awt.*;

/**
 * Main class that initializes and runs the Affect Recognition Simulator application.
 * It creates a GUI with controls to start and stop MQTT and TCP transmissions, and manages
 * the communication threads for both protocols.
 *
 * @author javiergs
 * @author Yayun Tan
 * @author Zexu Wei
 *
 * @version 2024.12.10
 */

public class Main extends JFrame {
	
	public static void main(String[] args) {
		Main main = new Main();
		main.setTitle ("Simulator :: Affective Signals");
		main.setSize(600, 400);
		main.setVisible(true);
	}
	
	public Main() {
		JMenuBar menuBar = new JMenuBar();
		SliderListener sliderListener = new SliderListener();
		menuBar.add(new CustomMenu("Control", sliderListener));
		setJMenuBar(menuBar);
		SliderPanel sliderPanel = new SliderPanel(sliderListener);
		add(sliderPanel, BorderLayout.CENTER);
	}
	
}