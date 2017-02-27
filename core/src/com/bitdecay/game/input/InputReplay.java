package com.bitdecay.game.input;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.bitdecay.game.persist.JsonUtils;
import com.bitdecay.game.world.LevelDefinition;

/**
 * Created by Monday on 2/26/2017.
 */

public class InputReplay {
    public LevelDefinition levelDef;

    public Array<InputRecord> inputRecords = new Array<>(60*120);

    public InputReplay() {
        // Here for JSON
    }

    public void reset() {
        inputRecords.clear();
    }
}
