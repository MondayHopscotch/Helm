package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.ExplosionComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 2/16/2017.
 */

public class ExplosionEntity extends GameEntity {

    public ExplosionEntity(Vector2 position) {
        addComponent(new TransformComponent(position.cpy(), Geom.NO_ROTATION));
        addComponent(new ExplosionComponent());
        addComponent(new CameraFollowComponent());
    }
}
