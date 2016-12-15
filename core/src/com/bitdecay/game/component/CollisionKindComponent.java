package com.bitdecay.game.component;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionKindComponent extends GameComponent {
    public enum CollisionKind {
        WALL,
        LANDING_PLATFORM, PLAYER;
    }

    public CollisionKind kind;

    public CollisionKindComponent(CollisionKind kind) {
        this.kind = kind;
    }
}
