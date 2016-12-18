package com.bitdecay.game.component;

import com.bitdecay.game.collision.CollisionKind;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollidedWithComponent extends GameComponent {

    public CollisionKind with;
    public float[] delivererGeometry;

    public CollidedWithComponent(CollisionKind kind, float[] delivererGeometry) {
        this.with = kind;
        this.delivererGeometry = delivererGeometry;
    }
}
