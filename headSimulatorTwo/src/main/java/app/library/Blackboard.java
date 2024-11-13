package app.library;



import java.util.ArrayList;
import java.util.List;

/**
 * The `Blackboard` class acts as a central communication hub for managing and
 * notifying observers about changes in the drawing state of the `TrackArea`.
 * It follows the Observer pattern, allowing components like the `TrackArea` to
 * register as observers and receive updates when the drawing state changes.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Blackboard {
    private List<Observer> observers = new ArrayList<>();
    private String drawingState = "default";

    public interface Observer {
        void update(String drawingState);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(drawingState);
        }
    }

    public void setDrawingState(String newDrawingState) {
        this.drawingState = newDrawingState;
        notifyObservers();
    }

    public String getDrawingState() {
        return drawingState;
    }
}
