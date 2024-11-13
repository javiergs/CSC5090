package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProgressBar extends JPanel implements PropertyChangeListener {

    private final JProgressBar PROGRESS_BAR;

    public ProgressBar() {
        PROGRESS_BAR = new JProgressBar(0, 100);
        PROGRESS_BAR.setValue(0);
        PROGRESS_BAR.setStringPainted(true);

        this.setLayout(new BorderLayout());
        this.add(PROGRESS_BAR, BorderLayout.CENTER);

        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("ProgressUpdated".equals(evt.getPropertyName())) {
            int newProgress = (int) evt.getNewValue();
            setProgress(newProgress);
        }
    }

    public void setProgress(int progress) {
        PROGRESS_BAR.setValue(progress);
    }
}
