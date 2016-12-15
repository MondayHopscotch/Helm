package com.bitdecay.game.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/13/2016.
 */
public class Geom {

    public static final float NO_ROTATION = 0;
    public static final float ROTATION_RIGHT = NO_ROTATION;
    public static final float ROTATION_UP = MathUtils.PI / 2;
    public static final float ROTATION_LEFT = MathUtils.PI;
    public static final float ROTATION_DOWN = ROTATION_UP + MathUtils.PI;

    public static final int DATA_POINTS_FOR_LINE = 4;


    /**
     * Rotates a list of points by the given angle around the origin.
     * @see Geom#getRotatedPoint(Vector2, float, Vector2)
     * @param points
     * @param angle
     * @return A list of points corresponding to the rotated version of the provided points
     */
    public static Vector2[] rotatePoints(Vector2[] points, float angle) {
        Vector2[] newPoints = new Vector2[points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = getRotatedPoint(points[i].x, points[i].y, angle, Vector2.Zero);
        }
        return newPoints;
    }

    /**
     * Rotates a list of points by the given angle around the origin.
     * @param points
     * @param angle
     * @return A array of coordinates corresponding to the rotated version of the provided points
     */
    public static float[] rotatePoints(float[] points, float angle) {
        float[] newPoints = new float[points.length];
        for (int i = 0; i < points.length; i += 2) {
            Vector2 rotated = getRotatedPoint(points[i], points[i+1], angle, Vector2.Zero);
            newPoints[i] = rotated.x;
            newPoints[i+1] = rotated.y;
        }
        return newPoints;
    }

    public static Vector2 rotateSinglePoint(Vector2 point, float angle) {
        return getRotatedPoint(point.x, point.y, angle, Vector2.Zero);
    }

    /**
     * Given a working point and a reference point, rotate the working point by
     * the given angle around the reference point.
     * @param p
     * @param angle
     * @param around
     * @return A new point with the rotation applied
     */
    private static Vector2 getRotatedPoint(float x, float y, float angle, Vector2 around) {
        Vector2 rotated = new Vector2(x, y);
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        rotated.x -= around.x;
        rotated.y -= around.y;

        // rotate point
        double xnew = rotated.x * c - rotated.y * s;
        double ynew = rotated.x * s + rotated.y * c;

        // translate point back:
        rotated.x = (float) (xnew + around.x);
        rotated.y = (float) (ynew + around.y);
        return rotated;
    }
}
