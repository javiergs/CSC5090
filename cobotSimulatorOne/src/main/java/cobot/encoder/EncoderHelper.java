package cobot.encoder;

/**
 * Interface for encoder to standardize andn ensure parse and encode methods implemented
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public interface EncoderHelper {
    int[] parse(String command);

    String encode(int[] angles);
}
