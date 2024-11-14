package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * StatusBarPanel class to display status messages.
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class StatusBarPanel extends JPanel implements PropertyChangeListener {
	
	private JLabel statusLabel;
	
	public StatusBarPanel(String status) {
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