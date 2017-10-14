package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.component.collide.GeometryComponentFactory;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.util.SecondLocationComponent;
import com.bitdecay.helm.world.WormholePair;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholeEntity extends com.bitdecay.helm.GameEntity {

    public WormholeEntity(WormholePair pair) {
        addComponent(new com.bitdecay.helm.component.TransformComponent(new Vector2(pair.entrance.x, pair.entrance.y), Geom.NO_ROTATION));
        addComponent(GeometryComponentFactory.getCircleGeomComponent(pair.entrance.radius, CollisionDirection.RECEIVES));
        addComponent(new CollisionKindComponent(CollisionKind.WORMHOLE));
        addComponent(new SecondLocationComponent(new Vector2(pair.exit.x, pair.exit.y)));
        addComponent(new com.bitdecay.helm.component.WormholeComponent(pair.entrance.radius + 75, pair.exit.radius + 25));
    }
}
