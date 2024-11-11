package app.library;



import java.util.ArrayList;
import java.util.List;

/**
 * app.library.Blackboard class used as a centralized observer/observable manager. Maintains list of observers that update when changes
 * are made to app.library.TrackArea. Follows Observer/Observable pattern.
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
