package headSim;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `Blackboard` class acts as a central hub for storing and managing eye tracking data.
 * It maintains a history of the last 10 eye positions and notifies any registered observers (like
 * the `TrackArea`) whenever a new data point is added. This allows for real-time updates of the
 * visualization.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Blackboard extends PropertyChangeSupport implements DataDestination {

    private int[][] points = new int[10][2];
    private int currentIndex = 0;
    private static Blackboard instance;
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    private Blackboard() {
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