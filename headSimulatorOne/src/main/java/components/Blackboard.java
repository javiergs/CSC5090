package components;

import java.awt.*;
import java.beans.PropertyChangeSupport;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;

    private Blackboard() {
        super(new Object());
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }
}
