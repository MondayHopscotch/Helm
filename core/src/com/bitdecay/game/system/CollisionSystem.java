package com.bitdecay.game.system;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CollidedWithComponent;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.math.Geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionSystem extends AbstractIteratingGameSystem {

    List<GameEntity> allCollisionEntities = new ArrayList<>();

    @Override
    public void before() {
        allCollisionEntities.clear();
    }

    @Override
    public void after() {
        for (GameEntity entity1 : allCollisionEntities) {
            for (GameEntity entity2 : allCollisionEntities) {
                if (entity1 != entity2) {
                    CollisionGeometryComponent geom1 = entity1.getComponent(CollisionGeometryComponent.class);
                    CollisionKindComponent kind1 = entity1.getComponent(CollisionKindComponent.class);

                    CollisionGeometryComponent geom2 = entity2.getComponent(CollisionGeometryComponent.class);
                    CollisionKindComponent kind2 = entity2.getComponent(CollisionKindComponent.class);


                    float[] geom1WorkingSet = getWorkingGeom(geom1);
                    float[] geom2WorkingSet = getWorkingGeom(geom2);

                    boolean geom1IsSolid = false;
                    boolean geom2IsSolid = false;
                    if (geom1WorkingSet.length > Geom.DATA_POINTS_FOR_LINE) {
                        geom1IsSolid = true;
                    }
                    if (geom2WorkingSet.length > Geom.DATA_POINTS_FOR_LINE) {
                        geom2IsSolid = true;
                    }

                    boolean collisionFound = false;
                    if (geom1IsSolid && geom2IsSolid) {
                        // TODO: Figure out if this is true: this doesn't appear to be commutative operation
                        collisionFound = Intersector.intersectPolygons(new Polygon(geom1WorkingSet), new Polygon(geom2WorkingSet), new Polygon());
                    } else if (geom1IsSolid && !geom2IsSolid) {
                        Vector2 geom2Start = new Vector2(geom2WorkingSet[0], geom2WorkingSet[1]);
                        Vector2 geom2End = new Vector2(geom2WorkingSet[2], geom2WorkingSet[3]);
                        collisionFound = Intersector.intersectSegmentPolygon(geom2Start, geom2End, new Polygon(geom1WorkingSet));
                    } else if (!geom1IsSolid && geom2IsSolid) {
                        Vector2 geom1Start = new Vector2(geom1WorkingSet[0], geom1WorkingSet[1]);
                        Vector2 geom1End = new Vector2(geom1WorkingSet[2], geom1WorkingSet[3]);
                        collisionFound = Intersector.intersectSegmentPolygon(geom1Start, geom1End, new Polygon(geom2WorkingSet));
                    }

                    if (collisionFound) {
                        geom1.colliding = true;
                        geom2.colliding = true;
                        if (CollisionGeometryComponent.Direction.RECEIVES.equals(geom1.direction) &&
                            CollisionGeometryComponent.Direction.DELIVERS.equals(geom2.direction)) {
                            entity1.addComponent(new CollidedWithComponent(kind2.kind));
                        } else if (CollisionGeometryComponent.Direction.DELIVERS.equals(geom1.direction) &&
                                CollisionGeometryComponent.Direction.RECEIVES.equals(geom2.direction)) {
                            entity2.addComponent(new CollidedWithComponent(kind1.kind));
                        }
                    }
                }
            }
        }
    }

    private float[] getWorkingGeom(CollisionGeometryComponent geomComponent) {
        float[] workingGeom = new float[geomComponent.originalGeom.length];
        System.arraycopy(geomComponent.originalGeom, 0, workingGeom, 0, geomComponent.originalGeom.length);

        // apply rotation first
        workingGeom = Geom.rotatePoints(workingGeom, geomComponent.rotation);

        // then apply position
        for (int i = 0; i < workingGeom.length; i += 2) {
            workingGeom[i] = workingGeom[i] + geomComponent.posX;
            workingGeom[i+1] = workingGeom[i+1] + geomComponent.posY;
        }

        return workingGeom;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        geom.colliding = false;
        allCollisionEntities.add(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(CollisionKindComponent.class);
    }
}
