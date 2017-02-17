package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 1/10/2017.
 */
public class ExtremeWorld extends LevelWorld {

    public ExtremeWorld() {
        super(2);
        Json json = new Json();
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/extreme/level_hard2.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/extreme/level_hard1_focus.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/extreme/level_hard3.json")));
    }

    @Override
    public String getWorldName() {
        return "Extreme";
    }
}
