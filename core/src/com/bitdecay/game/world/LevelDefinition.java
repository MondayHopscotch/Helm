package com.bitdecay.game.world;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/8/2016.
 */
public class LevelDefinition {
    public String name = "Unnamed Level";

    public Array<LineSegment> levelLines = new Array<>();
    public Array<Circle> gravityWells = new Array<>();
    public Array<Circle> repulsionFields = new Array<>();
    public Array<WormholePair> wormholes = new Array<>();
    public Rectangle finishPlatform = new Rectangle();
    public float finishPlatformRotation = Geom.NO_ROTATION;

    public Vector2 startPosition = new Vector2();

    public Array<Circle> focusPoints = new Array<>();
    public int startingFuel = 300;

    public Vector2 gravity = new Vector2(0, -10);

    public LevelDefinition() {
        // Here for JSON
    }
}
