package javiergs.cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * StatusBarPanel class to display status messages.
 *
 * @author Javier Gonzalez-Sanchez
 *
 * @version 1.0
 */
public class StatusBarPanel extends JPanel implements PropertyChangeListener {
	
	private JLabel statusLabel;
	
	public StatusBarPanel(String status) {
		// height panel to 22
		setPreferredSize(new Dimension(getWidth(), 22));
		setLayout(new GridLayout(1, 1));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		statusLabel = new JLabel(" Status: " + status);
		add(statusLabel);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("status"))
			statusLabel.setText("Status: " + evt.getNewValue());
	}

}