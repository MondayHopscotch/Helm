package com.bitdecay.helm.component.collide;

import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.GameComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionKindComponent extends GameComponent {
    public CollisionKind kind;

    public CollisionKindComponent(CollisionKind kind) {
        this.kind = kind;
    }
}
