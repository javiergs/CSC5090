package cobot.blackboard;

import java.awt.Point;

class OriginHelper {
    private Point center;

    private OriginHelper() {
        // private constructor to restrict instantiation
    }

    static OriginHelper init() {
        return new OriginHelper();
    }

    void setCenter(int width, int height) {
        this.center = new Point(width / 2, height / 2 - 36); // adjust for menu size
    }

    Point getCenter() {
        return center;
    }
}
