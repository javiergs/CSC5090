import java.beans.PropertyChangeSupport;

/**
 * The DataRepository class is responsible for storing the last 10 points and notifying observers when a new point is added.
 * Authors as listed in your README.md file.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class DataRepository extends PropertyChangeSupport {
	
	private int[][] points = new int[10][2];  // Holds the last 10 points
	private int currentIndex = 0;  // Track where to insert the next point
	private static DataRepository instance;
	
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
		int[] oldPoint = points[currentIndex]; // Store the old point
		points[currentIndex] = newPoint; // Update with the new point
		currentIndex = (currentIndex + 1) % points.length; // Cycle through points array
		firePropertyChange("newPoint", oldPoint, points[(currentIndex - 1 + points.length) % points.length]);
	}
 
}