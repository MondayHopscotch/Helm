package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.component.collide.GeometryComponentFactory;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.util.SecondLocationComponent;
import com.bitdecay.game.world.WormholePair;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholeEntity extends GameEntity {

    public WormholeEntity(WormholePair pair) {
        addComponent(new TransformComponent(new Vector2(pair.entrance.x, pair.entrance.y), Geom.NO_ROTATION));
        addComponent(GeometryComponentFactory.getCircleGeomComponent(pair.entrance.radius, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.WORMHOLE));
        addComponent(new SecondLocationComponent(new Vector2(pair.exit.x, pair.exit.y)));
    }
}
