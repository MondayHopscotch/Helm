package com.bitdecay.game.component.collide;

import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.GameComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionKindComponent extends GameComponent {
    public CollisionKind kind;

    public CollisionKindComponent(CollisionKind kind) {
        this.kind = kind;
    }
}
