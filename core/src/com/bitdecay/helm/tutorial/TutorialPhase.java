package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.screen.LevelPlayer;

/**
 * Created by Monday on 10/26/2017.
 */

public interface TutorialPhase {
    void start(Helm game, LevelPlayer player, Stage stage);
    boolean update(ShapeRenderer shaper);
}
