package cobot.blackboard;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.Random;

class ArmHelper {
    private List<Double> armAngles;
    private List<Double> targetAngles;
    private List<Color> colors;

    // private constructor to restrict instantiation
    private ArmHelper(int count) {
        armAngles = new ArrayList<>();
        targetAngles = new ArrayList<>();
        colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            armAngles.add(0.0);
            targetAngles.add(0.0);
            colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

    static ArmHelper init(int count) {
        return new ArmHelper(count);
    }

    void updateAngles(int[] numbers) {
        armAngles.clear();
        for (int num : numbers) {
            armAngles.add((double) num);
        }
    }

    List<Double> getArmAngles() {
        return armAngles;
    }

    List<Double> getTargetAngles() {
        return targetAngles;
    }

    List<Color> getColors() {
        return colors;
    }
}
