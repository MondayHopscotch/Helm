package com.bitdecay.game.world;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/14/2016.
 */
public class LineSegment {
    public Vector2 startPoint;
    public Vector2 endPoint;

    public LineSegment(Vector2 start, Vector2 end) {
        startPoint = start;
        endPoint = end;
    }
}
