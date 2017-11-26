package com.bitdecay.helm.system.collision;

import com.badlogic.gdx.utils.TimeUtils;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.collision.Collider;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.collision.NoOpCollider;
import com.bitdecay.helm.collision.SolidToCircleCollider;
import com.bitdecay.helm.collision.SolidToLineCollider;
import com.bitdecay.helm.collision.SolidToSolidCollider;
import com.bitdecay.helm.component.collide.CollidedWithComponent;
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

    private Collider lastCollider;
    private CollisionGeometryComponent geom1;
    private CollisionGeometryComponent geom2;

    @Override
    public void after() {
        if (allCollisionEntities.get(CollisionDirection.RECEIVES).isEmpty() || allCollisionEntities.get(CollisionDirection.DELIVERS).isEmpty()) {
            // no possible collisions
            return;
        }

        for (GameEntity entity1 : allCollisionEntities.get(CollisionDirection.RECEIVES)) {
            for (GameEntity entity2 : allCollisionEntities.get(CollisionDirection.DELIVERS)) {
                if (entity1 != entity2) {
                    geom1 = entity1.getComponent(CollisionGeometryComponent.class);
                    geom2 = entity2.getComponent(CollisionGeometryComponent.class);

                    if ((geom1.direction & CollisionDirection.PLAYER) == 0 && (geom2.direction & CollisionDirection.PLAYER) == 0) {
                        // if neither is a player-collision, we can just drop it since the player will be involved in all
                        // collisions of interest
                        continue;
                    }

                    lastCollider = getCollider(geom1, geom2);
                    if (lastCollider.collisionFound()) {
                        geom1.colliding = true;
                        geom2.colliding = true;

                        entity1.addComponent(new CollidedWithComponent(entity2, geom2, entity2.getComponent(CollisionKindComponent.class).kind, lastCollider.getGeom2WorkingSet()));
                    }
                }
            }
        }
    }

    private SolidToCircleCollider solidVCircle = new SolidToCircleCollider();
    private Collider solidVLine = new SolidToLineCollider();
    private Collider solidVSolid = new SolidToSolidCollider();
    private Collider noOpCollider = new NoOpCollider();

    int length1;
    int length2;

    private Collider getCollider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        length1 = geom1.originalGeom.length;
        length2 = geom2.originalGeom.length;
        if (length1 > 4) {
            if (length2 == 1) {
                // poly vs circle
                solidVCircle.setGeom1(geom1);
                solidVCircle.setGeom2(geom2);
                solidVCircle.setFlipped(false);
                return solidVCircle;
            } else if (length2 == 4) {

                solidVLine.setGeom1(geom1);
                solidVLine.setGeom2(geom2);
                return solidVLine;
            } else {
                solidVSolid.setGeom1(geom1);
                solidVSolid.setGeom2(geom2);
                return solidVSolid;
            }
        }

        if (length1 == 1) {
            if (length2 > 4) {
                solidVCircle.setGeom1(geom2);
                solidVCircle.setGeom2(geom1);
                solidVCircle.setFlipped(true);
                return solidVCircle;
            }
        }
        // I think we can get away with ignoring all other collisions.
        return noOpCollider;
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
