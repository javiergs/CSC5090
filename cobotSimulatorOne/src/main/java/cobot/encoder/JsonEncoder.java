package cobot.encoder;

import cobot.blackboard.Blackboard;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonEncoder implements EncoderHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonEncoder.class);

//    Example JSON Format:
//    { "angle1": 30, "angle2": 45, "angle3": 60, "angle4": 90, "angle5": 120, "angle6": 150 }

    @Override
    public int[] parse(String command) {
        int armCount = Blackboard.getInstance().getArmCount();
        int[] angles = new int[armCount];
        try {
            JSONObject jsonObject = new JSONObject(command);
            for (int i = 0; i < 6; i++) {
                angles[i] = jsonObject.getInt("angle" + (i + 1));
            }
        } catch (Exception e) {
            logger.error("Error parsing JSON command", e);
        }
        return angles;
    }

    @Override
    public String encode(int[] angles) {
        JSONObject json = new JSONObject();
        for (int i = 0; i < angles.length; i++) {
            json.put("angle" + (i + 1), angles[i]);
        }

        logger.info("Encoded into JSON: " + json);
        return json.toString();
    }

}
