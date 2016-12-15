package com.bitdecay.game.camera;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Wrapper class for gdx.Rectangle that allows for rotation and origin calculations with intersects/contains/intersection methods
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class RectangleExt
{
    public float x = 0;
    public float y = 0;
    public float width = 0;
    public float height = 0;
    public Vector2 origin = new Vector2(0, 0);
    public float rotation = 0;
    protected Rectangle _rect = null;
    /**
     * Init with a rectangle object, null will initialize with (0, 0, 0, 0)
     *
     * @param rectangle
     */
    public RectangleExt(Rectangle rectangle)
    {
        if (rectangle == null)
        {
            initialize(0, 0, 0, 0, new Vector2(0, 0), 0);
        } else
        {
            initialize(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Vector2(0, 0), 0);
        }
    }
    /**
     * Init with a given x, y, width, and height
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public RectangleExt(float x, float y, float width, float height)
    {
        initialize(x, y, width, height, new Vector2(0, 0), 0);
    }
    /**
     * Init with a given x, y, width, height, and origin
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param origin
     */
    public RectangleExt(float x, float y, float width, float height, Vector2 origin)
    {
        initialize(x, y, width, height, origin, 0);
    }


    /**
     * Init with a given x, y, width, height, origin, and rotation
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param origin
     * @param rotation
     */
    public RectangleExt(float x, float y, float width, float height, Vector2 origin, float rotation)
    {
        initialize(x, y, width, height, origin, rotation);
    }


    /**
     * Init with a given x, y, width, height, and rotation
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param rotation
     */
    public RectangleExt(float x, float y, float width, float height, float rotation)
    {
        initialize(x, y, width, height, new Vector2(0, 0), rotation);
    }

    /**
     * Build a rectangle with (0, 0, 0, 0);
     *
     * @return
     */
    public static RectangleExt empty() {
        return new RectangleExt(0, 0, 0, 0);
    }

    /**
     * Gets the (x, y) position of the rectangle
     *
     * @return
     */
    public Vector2 position() {
        return new Vector2(x, y);
    }

    /**
     * Returns a the same rectangle but rotation set to 0 and in the gdx Rectangle class format
     *
     * @return
     */
    public Rectangle rect() {
        _rect.x = x - origin.x;
        _rect.y = y - origin.y;
        _rect.width = width;
        _rect.height = height;
        return _rect;
    }

    /**
     * Gets the four points that define this rectangle
     *
     * @return
     */
    public Vector2[] points() {
        Vector2[] points = new Vector2[4];
        float localX = -origin.x;
        float localY = -origin.y;
        points[0] = new Vector2(localX, localY);
        points[1] = new Vector2(localX + width, localY);
        points[2] = new Vector2(localX + width, localY + height);
        points[3] = new Vector2(localX, localY + height);

        if (rotation != 0) {
            points[0] = VectorMath.rotatePointByDegreesAroundZero(points[0], rotation);
            points[1] = VectorMath.rotatePointByDegreesAroundZero(points[1], rotation);
            points[2] = VectorMath.rotatePointByDegreesAroundZero(points[2], rotation);
            points[3] = VectorMath.rotatePointByDegreesAroundZero(points[3], rotation);
        }

        Vector2 worldPoint = new Vector2(x, y);
        points[0] = points[0].add(worldPoint);
        points[1] = points[1].add(worldPoint);
        points[2] = points[2].add(worldPoint);
        points[3] = points[3].add(worldPoint);

        return points;
    }

    protected void initialize(float x, float y, float width, float height, Vector2 origin, float rotation)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.origin = origin;
        this.rotation = rotation;
        _rect = new Rectangle(0, 0, 0, 0);
    }


    /**
     * Sets the origin at the center of the rectangle
     */
    public RectangleExt setOriginAtCenter()
    {
        origin = localCenter();
        return this;
    }


    /**
     * Gets the point at the local center of the rectangle
     *
     * @return
     */
    public Vector2 localCenter()
    {
        return new Vector2(width / 2f, height / 2f);
    }


    public Vector2 center()
    {
        return position().sub(origin).add(localCenter());
    }


    public Vector2 originPercentage()
    {
        if (width == 0)
        {
            return new Vector2(0, origin.y / height);
        } else if (height == 0)
        {
            return new Vector2(origin.x / width, 0);
        }
        return new Vector2(origin.x / width, origin.y / height);
    }


    /**
     * Checks if a point (x, y) is contained in the rectangle
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y)
    {
        return contains((float) x, (float) y);
    }


    /**
     * Checks if a Vector2 is contained in the rectangle
     *
     * @param point
     * @return
     */
    public boolean contains(Vector2 point)
    {
        return contains(point.x, point.y);
    }


    /**
     * Checks if a point (x, y) is contained in the rectangle
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y)
    {
        if (this.rotation == 0 && origin.x == 0.0f && origin.y == 0.0f)
        {
            return rect().contains(x, y);
        } else
        {
            float localX = x - this.x;
            float localY = y - this.y;

            Vector2 rot = VectorMath.rotatePointByDegreesAroundZero(localX, localY, -this.rotation);

            return rect().contains(rot.x + this.x, rot.y + this.y);
        }
    }


    /**
     * Checks if a rectangle is contained within the rectangle
     *
     * @param rectangle
     * @return
     */
    public boolean contains(Rectangle rectangle)
    {
        return contains(new RectangleExt(rectangle));
    }


    /**
     * Checks if a rectangle is contained within the rectangle
     *
     * @param rectangle
     * @return
     */
    public boolean contains(RectangleExt rectangle)
    {
        Vector2[] points = rectangle.points();
        for (Vector2 point : points)
            if (!contains(point))
                return false;
        return true;
    }


    /**
     * Checks if a rectangle intersects with the rectangle
     *
     * @param rectangle
     * @return
     */
    public boolean intersects(RectangleExt rectangle)
    {
        Vector2[] theirPoints = rectangle.points();
        Vector2[] myPoints = points();
        boolean[] theirResults = new boolean[4];
        boolean[] myResults = new boolean[4];
        for (int i = 0; i < 4; i++)
        {
            theirResults[i] = contains(theirPoints[i]);
            myResults[i] = rectangle.contains(myPoints[i]);
        }

        boolean theirAllTrue = true;
        boolean theirAllFalse = true;
        for (int i = 0; i < 4; i++)
        {
            if (theirResults[i])
            {
                theirAllFalse = false;
            } else
            {
                theirAllTrue = false;
            }
        }

        boolean myAllTrue = true;
        boolean myAllFalse = true;
        for (int i = 0; i < 4; i++)
        {
            if (myResults[i])
            {
                myAllFalse = false;
            } else
            {
                myAllTrue = false;
            }
        }

        if (theirAllTrue && myAllFalse)
            return false;
        else if (theirAllFalse && myAllTrue)
            return false;
        else return !(theirAllFalse && myAllFalse);
    }


    /**
     * Checks if a rectangle intersects with the rectangle
     *
     * @param rectangle
     * @return
     */
    public boolean intersects(Rectangle rectangle)
    {
        return intersects(new RectangleExt(rectangle));
    }


    /**
     * Returns true if this rectangle contains, is contained, or intersects
     *
     * @param rectangle
     * @return
     */
    public boolean overlaps(RectangleExt rectangle)
    {
        Vector2[] theirPoints = rectangle.points();
        Vector2[] myPoints = points();
        boolean[] results = new boolean[8];
        for (int i = 0; i < 4; i++)
        {
            results[i] = contains(theirPoints[i]);
            results[i + 4] = rectangle.contains(myPoints[i]);
        }
        for (int i = 0; i < 8; i++)
        {
            if (results[i])
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns true if this rectangle contains, is contained, or intersects
     *
     * @param rectangle
     * @return
     */
    public boolean overlaps(Rectangle rectangle)
    {
        return overlaps(new RectangleExt(rectangle));
    }


    /**
     * Gets a rectangle outlining the space where this rectangle overlaps the given rectangle (ignores rotation)
     *
     * @param rectangle
     * @return
     */
    public Rectangle intersection(Rectangle rectangle)
    {
        if (!intersects(rectangle))
        {
            return new Rectangle(0, 0, 0, 0);
        }
        Rectangle r = new Rectangle(0, 0, 0, 0);
        Rectangle rect = rect();
        //
        r.x = Math.max(rect.x, rectangle.x);
        r.y = Math.max(rect.y, rectangle.y);
        //
        r.width = Math.min((rect.x + rect.width) - r.x, (rectangle.x + rectangle.width) - r.x);
        r.height = Math.min((rect.y + rect.height) - r.y, (rectangle.y + rectangle.height) - r.y);
        return r;
    }


    /**
     * Gets a rectangle outlining the space where this rectangle overlaps the given rectangle (ignores rotation)
     *
     * @param rectangle
     * @return
     */
    public Rectangle intersection(RectangleExt rectangle)
    {
        return intersection(rectangle.rect());
    }


    /**
     * Creates a deep copy clone of this rectangle (same as cpy)
     */
    @Override
    public RectangleExt clone()
    {
        RectangleExt clone = new RectangleExt(x, y, width, height, origin.cpy(), rotation);
        return clone;
    }


    /**
     * Creates a deep copy clone of this rectangle (same as clone)
     */
    public RectangleExt cpy()
    {
        return clone();
    }


    /**
     * To string
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("x:").append(NumberMath.round(x, 2)).append(", ");
        sb.append("y:").append(NumberMath.round(y, 2)).append(", ");
        sb.append("width:").append(NumberMath.round(width, 2)).append(", ");
        sb.append("height:").append(NumberMath.round(height, 2)).append(", ");
        sb.append("rotation:").append(NumberMath.round(rotation, 2)).append(", ");
        sb.append("origin:(").append(NumberMath.round(origin.x, 2)).append(", ").append(NumberMath.round(origin.y, 2)).append(")]");
        return sb.toString();
    }
}