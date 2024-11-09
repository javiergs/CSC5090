package cobot;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProgressBar extends JPanel implements PropertyChangeListener {

    private JProgressBar progressBar;

    public ProgressBar() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);


        this.setLayout(new BorderLayout());
        this.add(progressBar, BorderLayout.CENTER);


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
        progressBar.setValue(progress);
    }
}
