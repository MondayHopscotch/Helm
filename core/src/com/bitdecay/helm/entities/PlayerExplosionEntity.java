package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.PartOfPlayerComponent;

/**
 * Created by Monday on 2/16/2017.
 */

public class PlayerExplosionEntity extends com.bitdecay.helm.GameEntity {

    public PlayerExplosionEntity(Vector2 position) {
        addComponent(new com.bitdecay.helm.component.TransformComponent(position.cpy(), com.bitdecay.helm.math.Geom.NO_ROTATION));
        addComponent(new com.bitdecay.helm.component.ExplosionComponent(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION));
        addComponent(new com.bitdecay.helm.component.CameraFollowComponent());
        addComponent(new PartOfPlayerComponent());
    }
}
