package com.bitdecay.helm.system.collision;

import com.bitdecay.helm.collision.Collider;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.collision.SolidToCircleCollider;
import com.bitdecay.helm.collision.SolidToLineCollider;
import com.bitdecay.helm.collision.SolidToSolidCollider;
import com.bitdecay.helm.component.collide.CollisionGeometryComponent;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionSystem extends AbstractIteratingGameSystem {

    Map<Integer, List<com.bitdecay.helm.GameEntity>> allCollisionEntities = new HashMap<>();

    public CollisionSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
        allCollisionEntities.put(CollisionDirection.DELIVERS, new ArrayList<com.bitdecay.helm.GameEntity>());
        allCollisionEntities.put(CollisionDirection.RECEIVES, new ArrayList<com.bitdecay.helm.GameEntity>());
    }

    @Override
    public void before() {
        for (Map.Entry<Integer, List<com.bitdecay.helm.GameEntity>> collisionKind : allCollisionEntities.entrySet()) {
            collisionKind.getValue().clear();
        }

    }

    @Override
    public void after() {
        if (allCollisionEntities.get(CollisionDirection.RECEIVES).isEmpty() || allCollisionEntities.get(CollisionDirection.DELIVERS).isEmpty()) {
            // no possible collisions
            return;
        }

        for (com.bitdecay.helm.GameEntity entity1 : allCollisionEntities.get(CollisionDirection.RECEIVES)) {
            for (com.bitdecay.helm.GameEntity entity2 : allCollisionEntities.get(CollisionDirection.DELIVERS)) {
                if (entity1 != entity2) {
                    CollisionGeometryComponent geom1 = entity1.getComponent(CollisionGeometryComponent.class);
                    CollisionGeometryComponent geom2 = entity2.getComponent(CollisionGeometryComponent.class);

                    if ((geom1.direction & CollisionDirection.PLAYER) == 0 && (geom2.direction & CollisionDirection.PLAYER) == 0) {
                        // if neither is a player-collision, we can just drop it since the player will be involved in all
                        // collisions of interest
                        continue;
                    }

                    CollisionKindComponent kind2 = entity2.getComponent(CollisionKindComponent.class);

                    Collider collider = getCollider(geom1, geom2);

                    if (collider.collisionFound()) {
                        geom1.colliding = true;
                        geom2.colliding = true;
                        entity1.addComponent(new com.bitdecay.helm.component.collide.CollidedWithComponent(entity2, geom2, kind2.kind, collider.getGeom2WorkingSet()));
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
                return new SolidToCircleCollider(geom1, geom2, false);
            } else if (length2 == 4) {
                return new SolidToLineCollider(geom1, geom2);
            } else {
                return new SolidToSolidCollider(geom1, geom2);
            }
        }

        if (length1 == 1) {
            if (length2 > 4) {
                return new SolidToCircleCollider(geom2, geom1, true);
            }
        }
        // I think we can get away with ignoring all other collisions.
        return new com.bitdecay.helm.collision.NoOpCollider(geom1, geom2);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        geom.colliding = false;
        if ((geom.direction & CollisionDirection.RECEIVES) == CollisionDirection.RECEIVES) {
            allCollisionEntities.get(CollisionDirection.RECEIVES).add(entity);
        }
        if ((geom.direction & CollisionDirection.DELIVERS) == CollisionDirection.DELIVERS) {
            allCollisionEntities.get(CollisionDirection.DELIVERS).add(entity);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(CollisionKindComponent.class);
    }
}
