package com.bitdecay.game.world;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/8/2016.
 */
public class LevelDefinition {
    public Array<LineSegment> levelLines;

    public LevelDefinition(Array<LineSegment> lines) {
        this.levelLines = lines;
    }
}
