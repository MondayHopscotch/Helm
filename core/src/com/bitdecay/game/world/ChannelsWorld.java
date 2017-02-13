package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Monday on 2/12/2017.
 */

public class ChannelsWorld extends LevelWorld {

    public ChannelsWorld() {
        super(2);
        Json json = new Json();

        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/medium/choke_point.json")));
        addLevelInstance(json.fromJson(LevelDefinition.class, Gdx.files.internal("level/medium/pegleg.json")));
    }

    @Override
    public String getWorldName() {
        return "Channels";
    }
}
