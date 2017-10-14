package com.bitdecay.helm.system.input;

import com.badlogic.gdx.InputProcessor;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 3/12/2017.
 */

public abstract class AbstractInputSystem extends AbstractIteratingGameSystem implements InputProcessor {
    public AbstractInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }
}
