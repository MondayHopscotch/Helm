package com.bitdecay.game.component;

import com.bitdecay.game.collision.CollisionKind;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionKindComponent extends GameComponent {
    public CollisionKind kind;

    public CollisionKindComponent(CollisionKind kind) {
        this.kind = kind;
    }
}
