package app.Model;

import app.Data.Emotion;
import app.Data.ProcessedDataObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

/**
 * The {@code RawDataProcessor} class processes both eye-tracking and emotion data from queues.
 * It validates, processes, and then converts this data into {@code ProcessedDataObject} instances
 * that can be used for further operations. The data includes integer coordinates for eye-tracking
 * and float-based emotion scores, with an emphasis on identifying the prominent emotion.
 * <p>
 * This class implements {@link Runnable} and is designed to run as a separate thread.
 * <p>
 * The class relies on a {@link Blackboard} to retrieve data from the input queues and add
 * processed data objects to the output queue.
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public class RawDataProcessor extends CustomThread {
	
	public static final String THREAD_NAME = "DataProcessor";
	
	public RawDataProcessor() {
		super();
		super.setLog(LoggerFactory.getLogger(RawDataProcessor.class.getName()));
		super.setName(THREAD_NAME);
	}
	
	@Override
	public void doYourWork() throws InterruptedException, IOException {
		// Poll with a timeout to prevent blocking indefinitely
		String eyeTrackingData = Blackboard.getInstance().pollEyeTrackingQueue();
		String emotionData = Blackboard.getInstance().pollEmotionQueue();
		if (eyeTrackingData != null) {
			super.getLog().info("ProcessingThread: Processing data pair: " + eyeTrackingData + ", " + emotionData);
			// Process the pair of data
			List<Integer> coordinates = convertToIntegerList(eyeTrackingData);
			List<Float> emotionScores = null;
			Emotion prominentEmotion;
			if (emotionData != null) {
				emotionScores = convertToFloatList(emotionData);
				//if the emotion data is invalid, use neutral
				if (!isValidEmotionData(emotionScores)) {
					logInvalidEmotionData(emotionData);
					prominentEmotion = Emotion.NONE;
				} else {
					prominentEmotion = getProminentEmotion(emotionScores);
				}
			} else {
				prominentEmotion = Emotion.NONE;
			}
			if (!isValidEyeTrackingData(coordinates)) {
				logInvalidEyeTrackingData(eyeTrackingData);
				return; //we can't do anything without eye tracking
			}
			ProcessedDataObject processedData = new ProcessedDataObject(
				coordinates.get(0),
				coordinates.get(1),
				prominentEmotion,
				emotionScores
			);
			
			Blackboard.getInstance().addToProcessedDataQueue(processedData);
		}
		// debugging client/server communication
		else if (emotionData != null) {
			super.getLog().warn(THREAD_NAME + ": Eye-tracking data is missing, but emotion data is present.");
		} else {
			// Handle timeout case or missing data
			super.getLog().warn(THREAD_NAME + ": Timed out waiting for data, or one client is slow.");
		}
	}
    
    @Override
    public void cleanUpThread() {
    }
    
    private boolean isValidEyeTrackingData(List<Integer> data) {
		return data != null && data.stream().allMatch(number -> number >= 0);
	}
	
	private List<Integer> convertToIntegerList(String data) {
		try {
			return Arrays.stream(data.split(","))
				.map(String::trim)
				.map(Integer::parseInt)
				.collect(Collectors.toList());
		} catch (NumberFormatException e) {
			logInvalidEyeTrackingData(data);
			return null;
		}
	}
	
	private void logInvalidEyeTrackingData(String data) {
		super.getLog().warn("Eye-tracking data must be in the form \"int, int\"\n where both are >= 0." +
			"Invalid eye-tracking data format: " + data);
	}
	
	private boolean isValidEmotionData(List<Float> data) {
		return data != null && data.stream().allMatch(number -> number >= 0 && number <= 1);
	}
	
	private List<Float> convertToFloatList(String data) {
		try {
			return Arrays.stream(data.split(","))
				.map(String::trim)
				.map(Float::parseFloat)
				.collect(Collectors.toList());
		} catch (NumberFormatException e) {
			logInvalidEmotionData(data);
			return null;  // Or return an empty list, or handle the error as needed
		}
	}
	
	public Emotion getProminentEmotion(List<Float> emotionScores) {
		if (emotionScores == null || emotionScores.isEmpty()) {
			throw new IllegalArgumentException("List must not be null or empty");
		}
		int maxIndex = 0;  // Assume the first element is the largest initially
		for (int i = 1; i < emotionScores.size(); i++) {
			// If current element is greater than the current max, update maxIndex
			if (emotionScores.get(i) > emotionScores.get(maxIndex)) {
				maxIndex = i;
			}
		}
		return Emotion.getByValue(maxIndex);
	}
	
	private void logInvalidEmotionData(String data) {
		super.getLog().warn("Emotion data is expected to be a comma seperated list of 5 floats between 0 and 1." +
			"Invalid emotion data format: " + data);
	}
	
}