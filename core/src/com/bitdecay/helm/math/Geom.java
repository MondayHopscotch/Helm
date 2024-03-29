package com.bitdecay.helm.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

    public static float distance(Vector2 point1, Vector2 point2) {
        return point1.cpy().sub(point2).len();
    }

    /**
     * Rotates a list of points by the given angle around the origin.
     *
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

    public static float[] rotatePoints(float[] points, float angle, Vector2 origin) {
        float[] newPoints = new float[points.length];
        for (int i = 0; i < points.length; i += 2) {
            Vector2 rotated = getRotatedPoint(points[i], points[i + 1], angle, origin);
            newPoints[i] = rotated.x;
            newPoints[i + 1] = rotated.y;
        }
        return newPoints;
    }

    /**
     * Rotates a list of points by the given angle around the origin.
     *
     * @param points
     * @param angle
     * @return A array of coordinates corresponding to the rotated version of the provided points
     */
    public static float[] rotatePoints(float[] points, float angle) {
        float[] newPoints = new float[points.length];
        for (int i = 0; i < points.length; i += 2) {
            Vector2 rotated = getRotatedPoint(points[i], points[i + 1], angle, Vector2.Zero);
            newPoints[i] = rotated.x;
            newPoints[i + 1] = rotated.y;
        }
        return newPoints;
    }

    public static Vector2 rotateSinglePoint(Vector2 point, float angle) {
        return getRotatedPoint(point.x, point.y, angle, Vector2.Zero);
    }

    /**
     * Given a working point and a reference point, rotate the working point by
     * the given angle around the reference point.
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

    public static float[] rectangleToFloatPoints(Rectangle shape, float rotation) {
        float[] points = new float[]{shape.x, shape.y,
                shape.x + shape.width, shape.y,
                shape.x + shape.width, shape.y + shape.height,
                shape.x, shape.y + shape.height};
        return rotatePoints(points, rotation, new Vector2(shape.x, shape.y));
    }

    public static float[] translatePoints(float[] points, Vector2 translation) {
        float[] translated = new float[points.length];
        for (int i = 0; i < points.length; i += 2) {
            translated[i] = points[i] + translation.x;
            translated[i+1] = points[i+1] + translation.y;
        }
        return translated;
    }

    public static Vector2 snap(Vector2 point, int snapSize) {
        return snap((int) point.x, (int) point.y, snapSize);
    }

    public static Vector2 snap(int x, int y, int snapSize) {
        int xSnapDir = x >= 0 ? 1 : -1;
        int ySnapDir = y >= 0 ? 1 : -1;
        int xOffset = 0 % snapSize;
        int yOffset = 0 % snapSize;
        int xSnap = (x + xSnapDir * snapSize / 2) / snapSize;
        xSnap *= snapSize;
        xSnap += xOffset;
        int ySnap = (y + ySnapDir * snapSize / 2) / snapSize;
        ySnap *= snapSize;
        ySnap += yOffset;

        return new Vector2(xSnap, ySnap);
    }

    // rotate first, then translate
    public static float[] transformPoints(float[] points, com.bitdecay.helm.component.TransformComponent transform) {
       return translatePoints(rotatePoints(points, transform.angle), transform.position);
    }

    public static float getMinAlongVector(float[] points, Vector2 referenceVector) {
        float min = Float.POSITIVE_INFINITY;
        for (int i = 1; i < points.length; i += 2) {
            min = Math.min(min, referenceVector.dot(points[i-1], points[i]));
        }
        return min;
    }

    public static float getMaxAlongVector(float[] points, Vector2 referenceVector) {
        float max = Float.NEGATIVE_INFINITY;
        for (int i = 1; i < points.length; i += 2) {
            max = Math.max(max, referenceVector.dot(points[i-1], points[i]));
        }
        return max;
    }

    public static float getMinY(float[] points) {
        float minY = Float.POSITIVE_INFINITY;
        for (int i = 1; i < points.length; i += 2) {
            minY = Math.min(minY, points[i]);
        }
        return minY;
    }

    public static float getMaxY(float[] points) {
        float maxY = Float.NEGATIVE_INFINITY;
        for (int i = 1; i < points.length; i += 2) {
            maxY = Math.max(maxY, points[i]);
        }
        return maxY;
    }

    public static float getMinX(float[] points) {
        float minX = Float.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i += 2) {
            minX = Math.min(minX, points[i]);
        }
        return minX;
    }

    public static float getMaxX(float[] points) {
        float maxX = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < points.length; i += 2) {
            maxX = Math.max(maxX, points[i]);
        }
        return maxX;
    }

    public static float getWidthOfPoints(float[] points) {
        return getMaxX(points) - getMinX(points);
    }

    public static float getHeightOfPoints(float[] points) {
        return getMaxY(points) - getMinY(points);
    }
}
