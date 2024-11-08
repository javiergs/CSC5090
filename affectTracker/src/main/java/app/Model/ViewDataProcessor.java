package app.Model;

import app.Data.Circle;
import app.Data.ProcessedDataObject;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code ViewDataProcessor} is alerted of new processed data available in the {@link Blackboard},
 * converts it into the appropriate {@link Circle} data for visualization.
 * <p>
 * This class extends {@link CustomThread} and implements {@link PropertyChangeListener} and is intended to be run as a separate thread.
 * It handles the consolidation of circles based on proximity and dynamically updates the display.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class ViewDataProcessor extends Thread implements PropertyChangeListener {
	private static final String THREAD_NAME = "ViewLogic";
	private static final Logger LOGGER = LoggerFactory.getLogger(RawDataProcessor.class);

	public ViewDataProcessor() {
		super();
		super.setName(THREAD_NAME);
		Blackboard.getInstance().addPropertyChangeListener(Blackboard.PROPERTY_NAME_PROCESSED_DATA, this);
	}
	
	@Override
	public void run() {
		try {
			//keep the thread alive and idle while waiting for new data
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
			LOGGER.error(THREAD_NAME + " thread was interrupted", e);
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			LOGGER.warn(e.toString());
		} finally {
			cleanUpThread();
		}
	}
	
//	@Override
	public void cleanUpThread() {
		Blackboard.getInstance().removePropertyChangeListener(
			Blackboard.PROPERTY_NAME_PROCESSED_DATA, this);
	}
	
	private void handleProcessedData(ProcessedDataObject data) {
		Deque<Circle> circleList = Blackboard.getInstance().getCircleList();
		Color circleColor = data.prominentEmotion().getColor();
		Circle newCircle = new Circle(data.xCoord(), data.yCoord(), circleColor, Blackboard.getInstance().getCircleRadius());
		boolean consolidated = false;
		for (Circle circle : circleList) {
			if (isWithinThreshold(circle, newCircle)) {
				circle.increaseRadius(50); // Consolidate by increasing the radius
				consolidated = true;
				break;
			}
		}
		if (!consolidated) {
			if (circleList.size() == Blackboard.getInstance().getMaxCircles()) {
				circleList.pollFirst();
			}
			circleList.addLast(newCircle); // Add the new circle
		}
		Blackboard.getInstance().setCircleList(circleList);
	}
	
	private boolean isWithinThreshold(Circle existing, Circle newCircle) {
		int dx = existing.getX() - newCircle.getX();
		int dy = existing.getY() - newCircle.getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		return distance <= Blackboard.getInstance().getThresholdRadius();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ProcessedDataObject data = (ProcessedDataObject) evt.getNewValue();
		System.out.println("retrieved processed data: " + data);
		if (data != null) {
			handleProcessedData(data);
		}
	}

}