package com.bitdecay.game.component;

import com.bitdecay.game.collision.CollisionKind;

/**
 * Created by Monday on 12/18/2016.
 */
public class CrashComponent extends GameComponent {
    public final CollisionKind with;

    public CrashComponent(CollisionKind with) {
        this.with = with;
    }
}
