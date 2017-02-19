package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 2/18/2017.
 */

public class GravityWorld extends LevelWorld {
    public GravityWorld() {
        super(2);
        Json json = new Json();
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/gravity/easy_grav.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/gravity/tough_cookies.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/gravity/conveyor.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/gravity/shake_n_bake.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/gravity/needle.json")));
    }

    @Override
    public String getWorldName() {
        return "Black Hole";
    }
}
