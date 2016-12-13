package com.bitdecay.game.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class PositionComponent extends GameComponent {
    public Vector2 position = new Vector2();

    public PositionComponent(Vector2 startingPosition) {
        position.set(startingPosition);
    }
}
