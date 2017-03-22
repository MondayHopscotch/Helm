package com.bitdecay.game.system.util;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.component.GameComponent;

/**
 * Created by Monday on 3/19/2017.
 */

public class SecondLocationComponent extends GameComponent {
    public Vector2 position;

    public SecondLocationComponent(Vector2 position) {
        this.position = position;
    }
}
