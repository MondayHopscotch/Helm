package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 12/23/2016.
 */
public class World1 extends LevelWorld {

    public World1() {
        super(2);
        Json json = new Json();

        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/easy/level_easy1.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/easy/level_easy2.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/easy/level_easy3.json")));
    }

    @Override
    public String getWorldName() {
        return "The Basics";
    }
}
