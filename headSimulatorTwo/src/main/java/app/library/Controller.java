package app.library;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `Controller` class manages the eye tracking simulation by responding to mouse
 * movements and user actions from a dropdown menu. It updates the eye position in the
 * `TrackArea` and sends data to the `Server` for processing.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Controller implements MouseMotionListener, ActionListener {
	
	private TrackArea trackArea;
	private Server server;
	private JComboBox<String> menu;
	private boolean init_connected = false;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	public Controller(TrackArea trackArea, Server server, JComboBox<String> menu) {
		this.trackArea = trackArea;
		this.server = server;
		this.menu = menu;
		DataRepository.getInstance().addPropertyChangeListener(trackArea);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (server.isRunning()) {
			int x = e.getX();
			int y = e.getY();
			trackArea.moveEyes(x, y);
			int[] newPoint = {x, y};
			DataRepository.getInstance().addPoint(newPoint); // Assuming addPoint triggers the property change
			logger.info("Mouse moved to: ({}, {})", x, y);
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String selectedAction = (String) menu.getSelectedItem();
		if (selectedAction.equals("Start")) {
			server.start();
			logger.info("app.library.Server started by user action.");
		} else if (selectedAction.equals("Stop")) {
			server.stop();
			logger.info("app.library.Server stopped by user action.");
		}
	}
}
