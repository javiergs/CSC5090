package cobot;

public class AngleBlackboard extends Blackboard {
    private static AngleBlackboard instance;

    private AngleBlackboard() {
        super(new Object());
    }

    public static AngleBlackboard getInstance() {
        if (instance == null) {
            instance = new AngleBlackboard();
        }
        return instance;
    }

    public void setAngles(int[] angles) {
        super.setAngles(angles);
    }
}
