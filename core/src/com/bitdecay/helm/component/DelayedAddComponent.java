package com.bitdecay.helm.component;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/16/2016.
 */
public class DelayedAddComponent extends GameComponent {
    public Array<DelayedAdd> delays;

    public DelayedAddComponent(DelayedAdd ...delays) {
        this.delays = Array.with(delays);
    }

    public static class DelayedAdd {
        public GameComponent component;
        public float delay;

        public DelayedAdd(GameComponent component, float delayInSeconds) {
            this.component = component;
            this.delay = delayInSeconds;
        }
    }
}
