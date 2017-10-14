package com.bitdecay.helm.component.collide;

import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.GameComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollidedWithComponent extends GameComponent {

    public GameEntity entity;
    public CollisionGeometryComponent geom;
    public CollisionKind with;
    public float[] delivererGeometry;

    public CollidedWithComponent(GameEntity entity, CollisionGeometryComponent geom, CollisionKind kind, float[] delivererGeometry) {
        this.entity = entity;
        this.geom = geom;
        this.with = kind;
        this.delivererGeometry = delivererGeometry;
    }
}
