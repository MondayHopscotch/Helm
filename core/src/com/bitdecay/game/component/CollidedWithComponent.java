package com.bitdecay.game.component;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollidedWithComponent extends GameComponent {

    public CollisionKindComponent.CollisionKind with;

    public CollidedWithComponent(CollisionKindComponent.CollisionKind kind) {
        this.with = kind;
    }
}
