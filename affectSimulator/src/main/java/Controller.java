import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class that listens for button clicks and calls the appropriate methods in the Client class.
 *
 * @author Javier Gonzalez-Sanchez
 *
 * @version 2.0
 */
public class Controller implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start client")) {
			System.out.println("Start menu item clicked");
		} else if (e.getActionCommand().equals("Stop client")) {
			System.out.println("Stop menu item clicked");
		} else if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
	}
	
}
