package head;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * This class is an encoder, it listens to the blackboard and encodes the data to be sent to the server.
 *
 * @author Samuel Fox Gar Kaplan
 * @author Javier Gonzalez-Sanchez
 * @author Luke Aitchison
 * @author Ethan Outangoun
 *
 * @version 2.0
 */

public class Encoder implements PropertyChangeListener{

	private Point point;
	private String x;
	private String y;

	public Encoder() {
		this.x = "";
		this.y = "";
	}


	public String getData() {
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
