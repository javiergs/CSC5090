package components;

import components.ThePublisher;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Encoder implements PropertyChangeListener{

	private static final Logger logger = LoggerFactory.getLogger(ThePublisher.class);
	private Point point;
	private String x;
	private String y;

	public Encoder() {
		this.x = "";
		this.y = "";
	}


	public String getData() {
		// System.out.println("Encoder: " + x + "," + y);
		return this.x + ","  + this.y;
	}

	
	@Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("point".equals(evt.getPropertyName())) {
            point = (Point) evt.getNewValue();
			setX(Integer.toString((int) point.getX()));
			setY(Integer.toString((int) point.getY()));
        }
    }

	private void setX(String x) {
		this.x = x;
	}
	private void setY(String y) {
		this.y = y;
	}
}
