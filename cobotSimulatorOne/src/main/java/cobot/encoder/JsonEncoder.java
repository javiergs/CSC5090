package cobot.encoder;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonEncoder implements EncoderHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonEncoder.class);

    @Override
    public int[] parse(String command) {
        int[] angles = new int[6];
        try {
            JSONArray jsonArray = new JSONArray(command);
            for (int i = 0; i < 6; i++) {
                angles[i] = jsonArray.getInt(i);
            }
        } catch (Exception e) {
            logger.error("Error parsing JSON command", e);
        }
        return angles;
    }
}
