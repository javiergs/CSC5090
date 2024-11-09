package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProgressBar extends JPanel implements PropertyChangeListener {

    private JProgressBar progressBar;

    public ProgressBar() {
        // Initialize the progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); // Display the progress as a percentage

        // Set up layout and add progress bar to the panel
        this.setLayout(new BorderLayout());
        this.add(progressBar, BorderLayout.CENTER);

        // Register this handler as a listener to the Blackboard
        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Check if the event corresponds to a progress update
        if ("ProgressUpdated".equals(evt.getPropertyName())) {
            int newProgress = (int) evt.getNewValue();
            setProgress(newProgress);
        }
    }

    // Method to update progress bar value
    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}
