package components;

import java.awt.*;
import java.beans.PropertyChangeSupport;

public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private int[] headPos;

    private Blackboard() {
        super(new Object());
    }

    public static Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void setHead(int[] mousePos) {
      headPos = mousePos;
      firePropertyChange("Head Position", null, headPos);
	}
	
	public int[] getHead() {
		return headPos;
	}

}
