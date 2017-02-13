package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 2/12/2017.
 */

public class IslandsWorld extends LevelWorld {
    public IslandsWorld() {
        super(1);
        Json json = new Json();

        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/islands/islands_1.json")));
    }

    @Override
    public String getWorldName() {
        return "Islands";
    }
}
