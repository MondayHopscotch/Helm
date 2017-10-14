package com.bitdecay.helm.persist;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by Monday on 2/26/2017.
 */

public class JsonUtils {
    public static Json json;

    private static void initJson() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);
        json.setElementType(com.bitdecay.helm.world.LevelDefinition.class, "levelLines", com.bitdecay.helm.world.LineSegment.class);
    }

    public static String marshal(Object object) {
        if (json == null) {
            initJson();
        }
        return json.toJson(object);
    }

    public static <T> T unmarshal(Class<T> clazz, FileHandle file) {
        if (json == null) {
            initJson();
        }
        return json.fromJson(clazz, file);
    }

    public static <T> T unmarshal(Class<T> clazz, String asJson) {
        if (json == null) {
            initJson();
        }
        return json.fromJson(clazz, asJson);
    }
}
