package com.bitdecay.game.camera;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Common vector math algorithms
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class VectorMath
{
    /**
     * Fills a vector2 with values from a vector3
     *
     * @param v1
     * @return
     */
    public static Vector2 toVector2(Vector3 v1)
    {
        return toVector2(v1.x, v1.y);
    }


    /**
     * Fills a vector2 with values from a vector4
     *
     * @param v1
     * @return
     */
    public static Vector2 toVector2(Quaternion v1)
    {
        return toVector2(v1.x, v1.y);
    }


    /**
     * Returns new Vector2(x, y)
     *
     * @param x
     * @param y
     * @return
     */
    public static Vector2 toVector2(float x, float y)
    {
        return new Vector2(x, y);
    }


    /**
     * Returns new Vector2(x, 0);
     *
     * @param x
     * @return
     */
    public static Vector2 toVector2(float x)
    {
        return toVector2(x, 0);
    }


    /**
     * Fills a vector3 with values from a vector2
     *
     * @param v1
     * @return
     */
    public static Vector3 toVector3(Vector2 v1)
    {
        return toVector3(v1.x, v1.y, 0);
    }


    /**
     * Fills a vector3 with values from a vector4
     *
     * @param v1
     * @return
     */
    public static Vector3 toVector3(Quaternion v1)
    {
        return toVector3(v1.x, v1.y, v1.z);
    }


    /**
     * Returns new Vector3(x, y, z)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Vector3 toVector3(float x, float y, float z)
    {
        return new Vector3(x, y, z);
    }


    /**
     * Returns new Vector3(x, y, 0)
     *
     * @param x
     * @param y
     * @return
     */
    public static Vector3 toVector3(float x, float y)
    {
        return toVector3(x, y, 0);
    }


    /**
     * Returns new Vector3(x, 0, 0)
     *
     * @param x
     * @return
     */
    public static Vector3 toVector3(float x)
    {
        return toVector3(x, 0, 0);
    }


    /**
     * Fills a vector4 with values from a vector2
     *
     * @param v1
     * @return
     */
    public static Quaternion toVector4(Vector2 v1)
    {
        return toVector4(v1.x, v1.y, 0, 0);
    }


    /**
     * Fills a vector4 with values from a vector3
     *
     * @param v1
     * @return
     */
    public static Quaternion toVector4(Vector3 v1)
    {
        return toVector4(v1.x, v1.y, v1.z);
    }


    /**
     * Returns new Quaternion(x, y, z, w)
     *
     * @param x
     * @param y
     * @param z
     * @param w
     * @return
     */
    public static Quaternion toVector4(float x, float y, float z, float w)
    {
        return new Quaternion(x, y, z, w);
    }


    /**
     * Returns new Quaternion(x, y, z, 0)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Quaternion toVector4(float x, float y, float z)
    {
        return toVector4(x, y, z, 0);
    }


    /**
     * Returns new Quaternion(x, y, 0, 0)
     *
     * @param x
     * @param y
     * @return
     */
    public static Quaternion toVector4(float x, float y)
    {
        return toVector4(x, y, 0, 0);
    }


    /**
     * Returns new Quaternion(x, 0, 0, 0)
     *
     * @param x
     * @return
     */
    public static Quaternion toVector4(float x)
    {
        return toVector4(x, 0, 0, 0);
    }


    /**
     * Multiplies a vector by scalars for x and y
     *
     * @param v1
     * @param scaleX
     * @param scaleY
     * @return
     */
    public static Vector2 multiply(Vector2 v1, float scaleX, float scaleY)
    {
        return toVector2(v1.x * scaleX, v1.y * scaleY);
    }


    /**
     * Multiplies a vector by a vector
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Vector2 multiply(Vector2 v1, Vector2 v2)
    {
        return multiply(v1, v2.x, v2.y);
    }


    /**
     * Multiplies a vector by a scalar
     *
     * @param v1
     * @param scalar
     * @return
     */
    public static Vector2 multiply(Vector2 v1, float scalar)
    {
        return multiply(v1, scalar, scalar);
    }


    /**
     * Gets the angle between two Vector2's in radians
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float angleInRadians(Vector2 v1, Vector2 v2)
    {
        // float perpDot = (v1.x * v2.x) - (v1.y * v2.y);
        // return (float) Math.atan2(perpDot, v1.dot(v2));
        return (float) (Math.atan2(v2.y, v2.x) - Math.atan2(v1.y, v1.x));
    }


    /**
     * Gets the angle between two Vector2's in degrees
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float angleInDegrees(Vector2 v1, Vector2 v2)
    {
        return (float) Math.toDegrees(angleInRadians(v1, v2));
    }


    /**
     * Gets the angle between two Vector3's in radians
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float angleInRadians(Vector3 v1, Vector3 v2)
    {
        if (v1 == Vector3.Zero || v2 == Vector3.Zero)
        {
            return 0;
        }
        Vector3 v1N = v1;
        v1N.nor();
        Vector3 v2N = v2;
        v2N.nor();
        if (v1N == v2N)
        {
            return 0;
        }
        float dot = v1N.dot(v2N);
        float angle = (float) Math.acos(dot);
        return angle;
    }


    /**
     * Gets the angle between two Vector3's in degrees
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float angleInDegrees(Vector3 v1, Vector3 v2)
    {
        return (float) Math.toDegrees(angleInRadians(v1, v2));
    }


    /**
     * Rotates a 2D point around (0, 0) by a given rotation in degrees
     *
     * @param point
     * @param rotation
     * @return
     */
    public static Vector2 rotatePointByDegreesAroundZero(Vector2 point, float rotation)
    {
        return rotatePointByDegreesAroundZero(point.x, point.y, rotation);
    }


    /**
     * Rotates a 2D point around (x, y) by a given rotation in degrees
     *
     * @param x
     * @param y
     * @param rotation
     * @return
     */
    public static Vector2 rotatePointByDegreesAroundZero(float x, float y, float rotation)
    {
        float rot = (float) Math.toRadians(rotation);
        float rotX = (float) Math.cos(rot) * x - (float) Math.sin(rot) * y;
        float rotY = (float) Math.sin(rot) * x + (float) Math.cos(rot) * y;
        return toVector2(rotX, rotY);
    }


    /**
     * Checks if a point is within a triangle defined by three Vector2's
     *
     * @param a first point in triangle
     * @param b second point in triangle
     * @param c third point in triangle
     * @param point the point to check if in the triangle
     * @return
     */
    public static boolean isPointInTriangle(Vector2 a, Vector2 b, Vector2 c, Vector2 point)
    {
        float ab = sign(point, a, b);
        float bc = sign(point, b, c);
        float ca = sign(point, c, a);
        boolean b1 = ab < 0f;
        boolean b2 = bc < 0f;
        boolean b3 = ca < 0f;
        return (b1 == b2 && b2 == b3);
    }


    private static float sign(Vector2 p1, Vector2 p2, Vector2 p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }
}