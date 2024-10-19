import javax.swing.*;
import java.awt.*;

/**
 * Menu bar and slider panel.
 *
 * @author Yayun Tan
 * @author Javier Gonzalez-Sanchez
 *
 * @version 2.0
 */
public class Main extends JFrame {
	
	public Main() {
		createMenuBar();
		SliderPanel sliderPanel = new SliderPanel();
    setLayout(new BorderLayout());
		add(sliderPanel, BorderLayout.CENTER);
	}
	
	private void createMenuBar() {
		Controller controller = new Controller();
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem startMenuItem = new JMenuItem("Start client");
		startMenuItem.addActionListener(controller);
		JMenuItem stopMenuItem = new JMenuItem("Stop client");
		stopMenuItem.addActionListener(controller);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(controller);
		fileMenu.add(startMenuItem);
		fileMenu.add(stopMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("Affect Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.setVisible(true);
	}
 
}
