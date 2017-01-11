package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 1/10/2017.
 */
public class World2 extends LevelWorld {

    public World2() {
        super(2);
        Json json = new Json();
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_medium1.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_medium2.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_medium3.json")));
    }

    @Override
    public String getWorldName() {
        return "Caves";
    }
}
