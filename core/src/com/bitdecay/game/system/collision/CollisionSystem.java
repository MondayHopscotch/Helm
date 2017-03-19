package com.bitdecay.game.system.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.collision.Collider;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.NoOpCollider;
import com.bitdecay.game.collision.SolidToCircleCollider;
import com.bitdecay.game.collision.SolidToLineCollider;
import com.bitdecay.game.collision.SolidToSolidCollider;
import com.bitdecay.game.component.collide.CollidedWithComponent;
import com.bitdecay.game.component.collide.CollisionGeometryComponent;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionSystem extends AbstractIteratingGameSystem {

    Map<CollisionDirection, List<GameEntity>> allCollisionEntities = new HashMap<>();

    public CollisionSystem(GamePilot pilot) {
        super(pilot);
        allCollisionEntities.put(CollisionDirection.DELIVERS, new ArrayList<GameEntity>());
        allCollisionEntities.put(CollisionDirection.RECEIVES, new ArrayList<GameEntity>());
    }

    @Override
    public void before() {
        for (Map.Entry<CollisionDirection, List<GameEntity>> collisionKind : allCollisionEntities.entrySet()) {
            collisionKind.getValue().clear();
        }

    }

    @Override
    public void after() {
        if (allCollisionEntities.get(CollisionDirection.RECEIVES).isEmpty() || allCollisionEntities.get(CollisionDirection.DELIVERS).isEmpty()) {
            // no possible collisions
            return;
        }

        for (GameEntity entity1 : allCollisionEntities.get(CollisionDirection.RECEIVES)) {
            for (GameEntity entity2 : allCollisionEntities.get(CollisionDirection.DELIVERS)) {
                if (entity1 != entity2) {
                    CollisionGeometryComponent geom1 = entity1.getComponent(CollisionGeometryComponent.class);
                    CollisionKindComponent kind1 = entity1.getComponent(CollisionKindComponent.class);

                    CollisionGeometryComponent geom2 = entity2.getComponent(CollisionGeometryComponent.class);
                    CollisionKindComponent kind2 = entity2.getComponent(CollisionKindComponent.class);

                    Collider collider = getCollider(geom1, geom2);

                    if (collider.collisionFound()) {
                            geom1.colliding = true;
                            geom2.colliding = true;
                            if (CollisionDirection.RECEIVES.equals(geom1.direction) &&
                                    CollisionDirection.DELIVERS.equals(geom2.direction)) {
                                entity1.addComponent(new CollidedWithComponent(geom2, kind2.kind, collider.getGeom2WorkingSet()));
                            } else if (CollisionDirection.DELIVERS.equals(geom1.direction) &&
                                    CollisionDirection.RECEIVES.equals(geom2.direction)) {
                                entity2.addComponent(new CollidedWithComponent(geom1, kind1.kind, collider.getGeom1WorkingSet()));
                            }
                    }
                }
            }
        }
    }

    private Collider getCollider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        int length1 = geom1.originalGeom.length;
        int length2 = geom2.originalGeom.length;
        if (length1 > 4) {
            if (length2 == 1) {
                // poly vs circle
                return new SolidToCircleCollider(geom1, geom2);
            } else if (length2 == 4) {
                return new SolidToLineCollider(geom1, geom2);
            } else {
                return new SolidToSolidCollider(geom1, geom2);
            }
        }
        // I think we can get away with ignoring all other collisions.
        return new NoOpCollider(geom1, geom2);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        geom.colliding = false;
        if (!allCollisionEntities.containsKey(geom.direction)) {
            allCollisionEntities.put(geom.direction, new ArrayList<GameEntity>());
        }
        allCollisionEntities.get(geom.direction).add(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(CollisionKindComponent.class);
    }
}
