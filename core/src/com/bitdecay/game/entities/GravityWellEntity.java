package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.component.GravityProducerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.collide.GeometryComponentFactory;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityWellEntity extends GameEntity {

    public GravityWellEntity(Circle well) {
        addComponent(new TransformComponent(new Vector2(well.x, well.y), Geom.NO_ROTATION));
        addComponent(new GravityProducerComponent(well.radius));
        addComponent(GeometryComponentFactory.getCircleGeomComponent(well.radius, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.GRAVITY_WELL));
    }
}
