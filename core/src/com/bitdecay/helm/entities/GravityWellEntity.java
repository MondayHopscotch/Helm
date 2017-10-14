package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.component.GravityProducerComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.collide.GeometryComponentFactory;
import com.bitdecay.helm.math.Geom;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityWellEntity extends GameEntity {

    public GravityWellEntity(Circle well, boolean repels) {
        addComponent(new TransformComponent(new Vector2(well.x, well.y), Geom.NO_ROTATION));
        addComponent(new GravityProducerComponent(well.radius, repels));
        addComponent(GeometryComponentFactory.getCircleGeomComponent(well.radius, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.GRAVITY_WELL));
    }
}
