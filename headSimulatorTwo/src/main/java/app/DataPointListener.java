package app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The `DataPointListener` class listens for property change events from the
 * `DataRepository` and updates the `TrackArea` with the latest data point.
 */
public class DataPointListener implements PropertyChangeListener {

    private final TrackArea trackArea;

    /**
     * Constructs a `DataPointListener`.
     *
     * @param trackArea The `TrackArea` to update with new data points.
     */
    public DataPointListener(TrackArea trackArea) {
        this.trackArea = trackArea;
    }

    /**
     * Called when a property change event is fired.
     * Updates the `TrackArea` if the event is a "newPoint" event.
     *
     * @param evt The `PropertyChangeEvent` object.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newPoint".equals(evt.getPropertyName())) {
            int[] point = (int[]) evt.getNewValue();
            trackArea.updateLatestPoint(point[0], point[1]);
        }
    }
}