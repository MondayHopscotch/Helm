package com.bitdecay.game.component;

/**
 * Created by Monday on 12/15/2016.
 */
public class ActiveComponent extends GameComponent {
    public boolean active = false;
    public float flipControlTimer;

    public ActiveComponent(boolean hasControl, float secondsBeforeFlipping) {
        active = hasControl;
        this.flipControlTimer = secondsBeforeFlipping;
    }
}
