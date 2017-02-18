package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.component.GravityProducerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityWellEntity extends GameEntity {

    public GravityWellEntity(Vector2 position, float size) {
        addComponent(new TransformComponent(position, Geom.NO_ROTATION));
        addComponent(new GravityProducerComponent(size));
        addComponent(new CollisionGeometryComponent(new float[]{-size/2,-size/2, size/2, -size/2, size/2, size/2, -size/2, size/2},CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.GRAVITY_WELL));
    }
}
