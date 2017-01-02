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

        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/levelX.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level2.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level3.json")));
        levels.add(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/level999.json")));
    }
}
