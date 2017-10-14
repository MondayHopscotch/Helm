package com.bitdecay.helm.persist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitdecay.helm.input.InputReplay;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayUtils {

    public static final String REPLAY_DIR = "replays";

    public static void saveReplay(String name, InputReplay inputReplay) {
        String marshaled = JsonUtils.marshal(inputReplay);
        FileHandle replayFile = Gdx.files.local("replays\\" + name);
        replayFile.writeString(marshaled, false);
    }

    public static void deleteReplay(FileHandle replayFile) {
        replayFile.delete();
    }
}
