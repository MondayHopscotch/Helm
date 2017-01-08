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

        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_easy1.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_easy2.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_easy3.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/islands_1.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level_hard1.json")));
    }
}
