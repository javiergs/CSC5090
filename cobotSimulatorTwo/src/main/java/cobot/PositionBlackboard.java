package cobot;

public class PositionBlackboard extends Blackboard {
    private static PositionBlackboard instance;

    private PositionBlackboard() {
        super(new Object());
    }

    public static PositionBlackboard getInstance() {
        if (instance == null) {
            instance = new PositionBlackboard();
        }
        return instance;
    }
}
