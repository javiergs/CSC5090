package app;

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

    /**
     * The `Observer` interface defines the method that observers must implement
     * to receive updates from the `Blackboard`.
     */
    public interface Observer {
        /**
         * Called to notify the observer about a change in the drawing state.
         *
         * @param drawingState The new drawing state.
         */
        void update(String drawingState);
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to add.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers about a change in the drawing state.
     */
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(drawingState);
        }
    }

    /**
     * Sets the new drawing state and notifies observers about the change.
     *
     * @param newDrawingState The new drawing state.
     */
    public void setDrawingState(String newDrawingState) {
        this.drawingState = newDrawingState;
        notifyObservers();
    }

    /**
     * Returns the current drawing state.
     *
     * @return The current drawing state.
     */
    public String getDrawingState() {
        return drawingState;
    }
}