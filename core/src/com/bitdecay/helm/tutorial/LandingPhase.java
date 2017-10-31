package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/30/2017.
 */

public class LandingPhase implements TutorialPhase {
    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/testCollisions.json"));
        player.loadLevel(tutorial1);
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        return false;
    }
}
