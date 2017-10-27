package com.bitdecay.helm.screen;

import com.bitdecay.helm.GamePilot;

/**
 * Created by Monday on 10/23/2017.
 */

public class TutorialLevelPlayer extends LevelPlayer {
    public TutorialLevelPlayer(GamePilot pilot) {
        super(pilot, false);
        universalGravity.set(0, 0);
        gameCam.maxZoom = 1f;
    }
}
