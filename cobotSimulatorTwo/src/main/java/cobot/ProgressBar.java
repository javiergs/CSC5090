package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The ProgressBar class represents a progress bar component that listens to property change events from a
 * blackboard and updates its progress based on the received values.
 *
 * @author Reza Mousakhani
 * @author Damian Dhesi
 * @author Shiv Panchal
 * @version 2.0
 */
public class ProgressBar extends JPanel implements PropertyChangeListener {

    private final JProgressBar PROGRESS_BAR;

    /**
     * Constructs a new ProgressBar. Initializes the JProgressBar component, sets up its display
     * properties, and adds it to the panel. Also registers this ProgressBar as a listener to the Blackboard.
     */
    public ProgressBar() {
        PROGRESS_BAR = new JProgressBar(0, 100);
        PROGRESS_BAR.setValue(0);
        PROGRESS_BAR.setStringPainted(true);

        this.setLayout(new BorderLayout());
        this.add(PROGRESS_BAR, BorderLayout.CENTER);

        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    /**
     * Called when a property change event is received. Updates the progress bar if the event's property
     * name is "ProgressUpdated".
     *
     * @param evt the property change event containing the new progress value
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("ProgressUpdated".equals(evt.getPropertyName())) {
            int newProgress = (int) evt.getNewValue();
            setProgress(newProgress);
        }
    }

    /**
     * Sets the progress value of the progress bar.
     *
     * @param progress the new progress value to be displayed, ranging from 0 to 100
     */
    public void setProgress(int progress) {
        PROGRESS_BAR.setValue(progress);
    }
}