package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.screen.LevelPlayer;

/**
 * Created by Monday on 10/26/2017.
 */

public interface TutorialPhase {
    void start(LevelPlayer player);
    boolean update(ShapeRenderer shaper);
}
