package com.bitdecay.game.persist;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.bitdecay.game.input.InputReplay;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 2/26/2017.
 */

public class JsonUtils {
    public static Json json;

    private static void initJson() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);
        json.setElementType(LevelDefinition.class, "levelLines", LineSegment.class);
    }

    public static String marshalLevel(LevelDefinition levelDef) {
        if (json == null) {
            initJson();
        }
        return json.toJson(levelDef);
    }

    public static LevelDefinition unmarshalLevel(String asJson) {
        if (json == null) {
            initJson();
        }
        return json.fromJson(LevelDefinition.class, asJson);
    }

    public static String marshalReplay(InputReplay inputReplay) {
        if (json == null) {
            initJson();
        }
        return json.prettyPrint(inputReplay);
    }

    public static InputReplay unmarshalReplay(FileHandle replayHandle) {
        if (json == null) {
            initJson();
        }
        return json.fromJson(InputReplay.class, replayHandle);
    }
}
