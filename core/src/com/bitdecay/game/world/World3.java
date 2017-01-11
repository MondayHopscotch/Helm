package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 1/10/2017.
 */
public class World3 extends LevelWorld {

    public World3() {
        super(2);
        Json json = new Json();
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_hard2.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_hard1.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_hard3.json")));
    }

    @Override
    public String getWorldName() {
        return "Extreme";
    }
}
