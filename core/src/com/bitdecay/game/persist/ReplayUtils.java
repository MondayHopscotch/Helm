package com.bitdecay.game.persist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitdecay.game.input.InputReplay;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayUtils {

    public static void saveReplay(String name, InputReplay inputReplay) {
        String marshaled = JsonUtils.marshalReplay(inputReplay);
        FileHandle replayFile = Gdx.files.local(name);
        replayFile.writeString(marshaled, false);
    }

    public static InputReplay loadReplay(String replayName) {
        FileHandle replayFile = Gdx.files.local(replayName);
        return JsonUtils.unmarshalReplay(replayFile);
    }
}
