package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.PositionComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CameraUpdateSystem extends AbstractIteratingGameSystem {

    private FollowOrthoCamera cam;

    public CameraUpdateSystem(FollowOrthoCamera cam) {
        this.cam = cam;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        cam.addFollowPoint(new Vector2(position.position));
        cam.addFollowPoint(new Vector2(1000, 800));
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CameraFollowComponent.class) &&
                entity.hasComponent(PositionComponent.class);
    }
}
