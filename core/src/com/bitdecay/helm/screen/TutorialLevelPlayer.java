package com.bitdecay.helm.screen;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.GamePilot;

/**
 * Created by Monday on 10/23/2017.
 */

public class TutorialLevelPlayer extends LevelPlayer {

    public final Vector2 originalGravity;

    public TutorialLevelPlayer(GamePilot pilot) {
        super(pilot, false);
        originalGravity = new Vector2(universalGravity);
        universalGravity.set(0, 0);
        gameCam.maxZoom = 1f;
    }
}
