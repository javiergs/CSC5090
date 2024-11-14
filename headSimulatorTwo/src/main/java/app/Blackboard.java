package app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The `Blackboard` class acts as a central communication hub for managing and
 * notifying observers about changes in the drawing state of the `TrackArea`.
 * It follows the Observer pattern using `PropertyChangeSupport`.
 *
 * @author Ashton
 * @author David H.
 * @author Anthony C.
 * @version 1.0
 */
public class Blackboard {
    private static final Blackboard instance = new Blackboard();
    private final PropertyChangeSupport support;
    private String drawingState = "default";

    private Blackboard() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * Returns the singleton instance of `Blackboard`.
     *
     * @return The `Blackboard` instance.
     */
    public static Blackboard getInstance() {
        return instance;
    }

    /**
     * Adds a `PropertyChangeListener` to listen for changes in the drawing state.
     *
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a `PropertyChangeListener`.
     *
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Sets the new drawing state and notifies listeners about the change.
     *
     * @param newDrawingState The new drawing state.
     */
    public void setDrawingState(String newDrawingState) {
        String oldDrawingState = this.drawingState;
        this.drawingState = newDrawingState;
        support.firePropertyChange("drawingState", oldDrawingState, newDrawingState);
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
