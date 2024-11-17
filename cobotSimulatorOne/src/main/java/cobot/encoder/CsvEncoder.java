package cobot.encoder;

import cobot.blackboard.Blackboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles parsing and encoding angles commands using String in form of comma separated values
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class CsvEncoder implements EncoderHelper {
    private static final Logger logger = LoggerFactory.getLogger(CsvEncoder.class);

    @Override
    public int[] parse(String command) {
        int armCount = Blackboard.getInstance().getArmCount();
        int[] angles = new int[armCount];
        try {
            String[] tokens = command.split(",");
            for (int i = 0; i < armCount; i++) {
                angles[i] = Integer.parseInt(tokens[i]);
            }
        } catch (NumberFormatException e) {
            logger.error("Error parsing string command", e);
        }

        return angles;
    }

    @Override
    public String encode(int[] angles) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < angles.length; i++) {
            sb.append(angles[i]);
            if (i < angles.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}
