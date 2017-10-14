package com.bitdecay.helm.input;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 2/26/2017.
 */

public class InputReplay {
    public com.bitdecay.helm.world.LevelDefinition levelDef;

    public Array<InputRecord> inputRecords = new Array<>(60*120);

    public InputReplay() {
        // Here for JSON
    }

    public void reset() {
        inputRecords.clear();
    }
}
