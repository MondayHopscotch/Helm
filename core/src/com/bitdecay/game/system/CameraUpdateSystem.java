package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.TransformComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CameraUpdateSystem extends AbstractIteratingGameSystem {

    private FollowOrthoCamera cam;

    public CameraUpdateSystem(GamePilot pilot, FollowOrthoCamera cam) {
        super(pilot);
        this.cam = cam;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        CameraFollowComponent camera = entity.getComponent(CameraFollowComponent.class);

        cam.addFollowPoint(new Vector2(transform.position).add(camera.offset));
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CameraFollowComponent.class) &&
                entity.hasComponent(TransformComponent.class);
    }
}
