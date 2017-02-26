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

//    @Override
//    public void write(Json json) {
//        json.writeObjectStart("levelDef");
//        json.writeValue(JsonUtils.marshalLevel(levelDef));
//        json.writeObjectEnd();
//        json.writeArrayStart("inputs");
//        for (InputRecord inputRecord : inputRecords) {
//            json.writeValue(inputRecord.tick);
//            json.writeValue(inputRecord.angle);
//            json.writeValue(inputRecord.boosting);
//        }
//        json.writeArrayEnd();
//    }
//
//    @Override
//    public void read(Json json, JsonValue jsonData) {
//        levelDef = JsonUtils.unmarshalLevel(jsonData.get("levelDef").asString());
//        String[] inputs = jsonData.get("inputs").asStringArray();
//        for (int index = 0; index < inputs.length;) {
//            addNewInputRecord(inputs[index], inputs[index+1], inputs[index+2]);
//            index += 3;
//        }
//
//    }

//    private void addNewInputRecord(String tick, String angle, String boost) {
//        inputRecords.add(new InputRecord(Integer.parseInt(tick), Float.parseFloat(angle), Boolean.parseBoolean(boost)));
//    }
}
