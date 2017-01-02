package com.bitdecay.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Monday on 12/8/2016.
 */
public class LevelDefinition {
    public Array<LineSegment> levelLines = new Array<>();

    public Rectangle finishPlatform = new Rectangle();
    public Vector2 startPosition = new Vector2();
    public int startingFuel = 300; // placeholder till I figure out what I want to do with this

    public LevelDefinition() {
        // Here for JSON
    }
}
