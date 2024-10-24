package app.Data;

import app.Model.RawDataProcessor;
import app.Model.ViewDataProcessor;

import java.util.List;

/**
 * The {@code ProcessedDataObject} record represents the processed data for a particular
 * set of coordinates and emotions. It stores the x and y coordinates, the prominent emotion
 * detected from the data, and the list of emotion scores.
 * <p>
 * This record is used to encapsulate the data produced by the {@link RawDataProcessor} and
 * passed to other components such as the {@link ViewDataProcessor} for further processing and visualization.
 *
 * @param xCoord the x-coordinate of the processed data, representing the x-position on the display
 * @param yCoord the y-coordinate of the processed data, representing the y-position on the display
 * @param prominentEmotion the most prominent emotion determined from the emotion scores
 * @param emotionScores the list of emotion scores associated with the data
 *
 * @author Andrew Estrada
 * @author Sean Sponsler
 * @version 1.0
 */
public record ProcessedDataObject
	(int xCoord, int yCoord, Emotion prominentEmotion, List<Float> emotionScores) {
}