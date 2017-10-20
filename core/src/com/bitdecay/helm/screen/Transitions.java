package com.bitdecay.helm.screen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by Monday on 10/19/2017.
 */

public class Transitions {

    private static float fadeInTime = 0.3f;
    private static float fadeOutTime = 0.5f;

    public static Action getFadeIn() {
        return Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(fadeInTime));
    }

    public static Action getFadeOut(Runnable runnable) {
        return Actions.sequence(
                Actions.fadeOut(fadeOutTime),
                Actions.run(runnable)
        );
    }

    public static Action getQuickFadeOut(Runnable runnable) {
        return Actions.sequence(
                Actions.fadeOut(fadeOutTime / 3),
                Actions.run(runnable)
        );
    }

    public static Action getLongFadeOut(Runnable runnable) {
        return Actions.sequence(
                Actions.fadeOut(fadeOutTime * 3),
                Actions.run(runnable)
        );
    }
}
