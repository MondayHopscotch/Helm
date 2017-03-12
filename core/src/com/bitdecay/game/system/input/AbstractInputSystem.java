package com.bitdecay.game.system.input;

import com.badlogic.gdx.InputProcessor;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 3/12/2017.
 */

public abstract class AbstractInputSystem extends AbstractIteratingGameSystem implements InputProcessor {
    public AbstractInputSystem(GamePilot pilot) {
        super(pilot);
    }
}
