import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Controller class is responsible for controlling the eye tracking simulation.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Controller implements MouseMotionListener {
	
	private TrackArea trackArea;
	private Server server;
	private boolean init_connected = false;
	
	public Controller(TrackArea trackArea, Server server) {
		this.trackArea = trackArea;
		this.server = server;
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
			trackArea.moveEyes(x, y);
		}
	
  }
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
