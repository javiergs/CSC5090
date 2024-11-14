package cobot.blackboard;

import java.awt.Point;

/**
 * Calculates and stores the center point of a display area used as the origin
 * @author Jack Ortega
 * @author Neeraja Beesetti
 * @author Saanvi Dua
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
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
