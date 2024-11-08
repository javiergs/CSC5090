package encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringEncoder implements EncoderHelper {
    private static final Logger logger = LoggerFactory.getLogger(StringEncoder.class);

    @Override
    public int[] parse(String command) {
        int[] angles = new int[6];
        try {
            String[] tokens = command.split(",");
            for (int i = 0; i < 6; i++) {
                angles[i] = Integer.parseInt(tokens[i]);
            }
        } catch (NumberFormatException e) {
            logger.error("Error parsing string command", e);
        }
        return angles;
    }
}
