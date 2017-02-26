package com.bitdecay.game.persist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.bitdecay.game.input.InputReplay;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayUtils {

    public static void saveReplay(String name, InputReplay inputReplay) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);
        String replay = json.toJson(inputReplay);
        FileHandle replayFile = Gdx.files.local(name);
        replayFile.writeString(replay, false);
    }

    public static void loadReplay(String replayName) {
        FileHandle replayFile = Gdx.files.local(replayName);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);
        json.fromJson(InputReplay.class, replayFile);
    }
}
