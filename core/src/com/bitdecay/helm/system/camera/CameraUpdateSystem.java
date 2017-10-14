package com.bitdecay.helm.system.camera;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/14/2016.
 */
public class CameraUpdateSystem extends com.bitdecay.helm.system.AbstractIteratingGameSystem {

    private com.bitdecay.helm.camera.FollowOrthoCamera cam;

    public CameraUpdateSystem(com.bitdecay.helm.GamePilot pilot, com.bitdecay.helm.camera.FollowOrthoCamera cam) {
        super(pilot);
        this.cam = cam;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        com.bitdecay.helm.component.CameraFollowComponent camera = entity.getComponent(com.bitdecay.helm.component.CameraFollowComponent.class);

        cam.addFollowPoint(new Vector2(transform.position).add(camera.offset));
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.CameraFollowComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class);
    }
}
