package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 4/16/2017.
 */

public class LaunchSmokeEntity extends com.bitdecay.helm.GameEntity {

    public  LaunchSmokeEntity(Vector2 position) {
        com.bitdecay.helm.component.ExplosionComponent explosion = new com.bitdecay.helm.component.ExplosionComponent(com.bitdecay.helm.unlock.palette.GameColors.LAUNCH_SMOKE);
        explosion.spreadCount = 2;
        explosion.particleSize = 75;
        explosion.decay = 3;
        addComponent(explosion);

        addComponent(new com.bitdecay.helm.component.TransformComponent(position.cpy(), com.bitdecay.helm.math.Geom.NO_ROTATION));
    }
}
