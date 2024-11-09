package app.library;

import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The app.library.DataRepository class is responsible for storing the last 10 points and notifying observers when a new point is added.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class DataRepository extends PropertyChangeSupport implements DataDestination {

	private int[][] points = new int[10][2];
	private int currentIndex = 0;
	private static DataRepository instance;
	private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

	public static DataRepository getInstance() {
		if (instance == null) {
			instance = new DataRepository();
		}
		return instance;
	}

	private DataRepository() {
		super(new Object());
	}

	public void addPoint(int[] newPoint) {
		int[] oldPoint = points[currentIndex];
		points[currentIndex] = newPoint;
		currentIndex = (currentIndex + 1) % points.length;
		firePropertyChange("newPoint", oldPoint, points[(currentIndex - 1 + points.length) % points.length]);
		logger.debug("New point added to repository: {}", newPoint);
	}

	@Override
	public void addSubscriberData(String data) {
		String[] parts = data.split("~");
		if (parts.length == 2) {
			try {
				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				addPoint(new int[]{x, y});
			} catch (NumberFormatException e) {
				logger.warn("Failed to parse data: " + data);
			}
		}
	}

	@Override
	public void alertError(String error) {
		logger.error("Received error: " + error);
	}
}