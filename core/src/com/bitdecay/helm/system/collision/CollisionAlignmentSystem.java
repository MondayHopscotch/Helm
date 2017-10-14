package com.bitdecay.helm.system.collision;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionAlignmentSystem extends com.bitdecay.helm.system.AbstractIteratingGameSystem {
    public CollisionAlignmentSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.collide.CollisionGeometryComponent geom = entity.getComponent(com.bitdecay.helm.component.collide.CollisionGeometryComponent.class);
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

        geom.posX = transform.position.x;
        geom.posY = transform.position.y;
        geom.rotation = transform.angle;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.collide.CollisionGeometryComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class);
    }
}