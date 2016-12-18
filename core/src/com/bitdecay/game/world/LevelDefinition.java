package com.bitdecay.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/8/2016.
 */
public class LevelDefinition {
    public Array<LineSegment> levelLines;

    public Rectangle finishPlatform = new Rectangle();
    public Vector2 startPosition;
}
