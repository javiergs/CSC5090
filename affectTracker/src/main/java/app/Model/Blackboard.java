package app.Model;

import java.beans.PropertyChangeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.library.DataDestination;


/**
 * The {@code Blackboard} class serves as the central hub for managing data across different components
 * of the system. It holds data queues for eye-tracking and emotion information, a list of circles for the display,
 * and settings for server information and display behavior.
 * <p>
 * This class follows the singleton design pattern, ensuring that only one instance of {@code Blackboard}
 * exists during the application's lifecycle. It provides synchronized access to the data being exchanged
 * between components, and manages the state of data retrieval.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @author Xiuyuan Qiu
 * @version 1.0
 */
public class Blackboard extends PropertyChangeSupport implements DataDestination {
	private String eyeTrackingSocket_Host = "localhost";  // default for testing
	private int eyeTrackingSocket_Port = 6001;  // default for testing
	private String emotionSocket_Host = "localhost"; // default for testing
	private int emotionSocket_Port = 6000; // default for testing
   
	public static final String PROPERTY_NAME_PROCESSED_DATA = "processed data";

	private boolean started = false;
   private final Logger logger;
	public static final String STARTED = "STARTED";
	public static final String STOPPED = "STOPPED";

	public static final String PROPERTY_NAME_VIEW_DATA = "view data";
	public static final String EYE_DATA_LABEL = "EYE";
	public static final String EMOTION_DATA_LABEL = "EMOTION";
	public static final String MQTTBROKER_ERROR = "MQTTE";
	public static final int TIMEOUT_IN_MS = 500;
	private static final String PREFIX_DELIMITER = "~";
	private static final Blackboard INSTANCE = new Blackboard();

   private final ProcessedDataDelegate processedDataDelegate;
   private final EyeTrackingDataDelegate eyeTrackingDataDelegate;
   private final EmotionDataDelegate emotionDataDelegate;
   private final CircleDataDelegate circleDataDelegate;
	
	private Blackboard() {
		super(new Object());
      logger = LoggerFactory.getLogger(Blackboard.class);
      processedDataDelegate = new ProcessedDataDelegate();
      circleDataDelegate = new CircleDataDelegate();
      eyeTrackingDataDelegate = new EyeTrackingDataDelegate();
      emotionDataDelegate = new EmotionDataDelegate();
	}
	
	public static Blackboard getInstance() {
		return INSTANCE;
	}

   public ProcessedDataDelegate getProcessedDataDelegate() {
      return processedDataDelegate;
   }

   public EyeTrackingDataDelegate getEyeTrackingDataDelegate() {
      return eyeTrackingDataDelegate;
   }
   
   public EmotionDataDelegate getEmotionDataDelegate() {
      return emotionDataDelegate;
   }

   public CircleDataDelegate getCircleDataDelegate() {
      return circleDataDelegate;
   }
	/**
	 * parses prefix and calls the method to add the data to the appropriate data structure
	 *
	 * @param dataWithPrefix string of data with a prefix to denote the source
	 */
	public void addSubscriberData(String dataWithPrefix){
		if (isValidMessage(dataWithPrefix)) {
			String[] prefixAndData = dataWithPrefix.split(PREFIX_DELIMITER, 2);
			try{
				switch (prefixAndData[0]) {
					case EYE_DATA_LABEL -> eyeTrackingDataDelegate.addToEyeTrackingQueue(prefixAndData[1]);
               case EMOTION_DATA_LABEL -> emotionDataDelegate.addToEmotionQueue(prefixAndData[1]);
					default -> logger.warn("Data from unknown source with prefix \"" + prefixAndData[0]
							+ "\" : " + prefixAndData[1]);
				}
			} catch (InterruptedException e) {
				logger.warn("Data with prefix \"" + prefixAndData[0] +
						"\" was interrupted and was unable to added to the queue  : " + prefixAndData[1]);
            }
        } else {
			logger.warn("Data with invalid format : " + dataWithPrefix);
		}
	}

	/**
	 * parses prefix and calls the appropriate method to alert listeners of the error
	 *
	 * @param messageWithPrefix string of data with a prefix to denote the source
	 */
	public void alertError(String messageWithPrefix){

		if (isValidMessage(messageWithPrefix)) {
			String[] prefixAndMessage = messageWithPrefix.split(PREFIX_DELIMITER, 2);

			switch (prefixAndMessage[0]) {
				case EYE_DATA_LABEL -> reportEyeThreadError(prefixAndMessage[1]);
				case EMOTION_DATA_LABEL -> reportEmotionThreadError(prefixAndMessage[1]);
				case MQTTBROKER_ERROR -> reportMQTTBrokerError(prefixAndMessage[1]);
				default -> logger.warn("Alerted of error with unknown prefix \"" + prefixAndMessage[0]
						+ "\" : " + prefixAndMessage[1]);
			}

		} else {
			logger.warn("Alerted of error without prefix : " + messageWithPrefix);
		}
	}

	/**
	 * ensures the message has a prefix and a body separated by the prefix delimiter
	 *
	 * @param messageWithPrefix string received from subscriber
	 * @return	true if message has prefix and body
	 */
	public boolean isValidMessage(String messageWithPrefix){
		return messageWithPrefix.split(PREFIX_DELIMITER).length == 2;
	}
	
	public String getFormattedConnectionSettings() {
		return String.format(
			"""
				\t\tEye Tracking Socket IP: %s:%s
				\t\tEmotion Tracking Socket IP: %s:%s
				""",
			eyeTrackingSocket_Host, eyeTrackingSocket_Port,
			emotionSocket_Host, emotionSocket_Port);
	}
	
	public String getEyeTrackingSocket_Host() {
		return eyeTrackingSocket_Host;
	}
	
	public int getEyeTrackingSocket_Port() {
		return eyeTrackingSocket_Port;
	}
	
	public String getEmotionSocket_Host() {
		return emotionSocket_Host;
	}
	
	public int getEmotionSocket_Port() {
		return emotionSocket_Port;
	}
	
	public void setEyeTrackingSocket_Host(String eyeTrackingSocket_Host) {
		this.eyeTrackingSocket_Host = eyeTrackingSocket_Host;
	}
	
	public void setEyeTrackingSocket_Port(int eyeTrackingSocket_Port) {
		this.eyeTrackingSocket_Port = eyeTrackingSocket_Port;
	}
	
	public void setEmotionSocket_Host(String emotionSocket_Host) {
		this.emotionSocket_Host = emotionSocket_Host;
	}
	
	public void setEmotionSocket_Port(int emotionSocket_Port) {
		this.emotionSocket_Port = emotionSocket_Port;
	}

   /* ----------------------------------------- */
   /* Methods for CircleDataDelegate */
   // delegates are package private, so we need to expose the methods to the view
   // specifically for PreferencePanel
	public int getMaxCircles() {
		return circleDataDelegate.getMaxCircles();
	}
	
	public void setMaxCircles(int maxCircles) {
		circleDataDelegate.setMaxCircles(maxCircles);
	}
	
	public int getThresholdRadius() {
		return circleDataDelegate.getThresholdRadius();
	}
	
	public void setThresholdRadius(int thresholdRadius) {
      circleDataDelegate.setThresholdRadius(thresholdRadius);
	}

	public int getCircleRadius() {
		return circleDataDelegate.getCircleRadius();
	}
   /* ----------------------------------------- */

	public void reportEyeThreadError(String ex_message) {
		firePropertyChange(EYE_DATA_LABEL, null, ex_message);
	}
	
	public void reportEmotionThreadError(String ex_message) {
		firePropertyChange(EMOTION_DATA_LABEL, null, ex_message);
	}

	public void reportMQTTBrokerError(String ex_message) {
		firePropertyChange(MQTTBROKER_ERROR, null, ex_message);
	}

	public void startedProcessing() {
		firePropertyChange(STARTED, started, true);
		started = true;
	}
	public void stoppedProcessing() {
		firePropertyChange(STOPPED, started, false);
		started = false;
	}
}