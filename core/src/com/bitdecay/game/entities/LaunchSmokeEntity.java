package com.bitdecay.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.ExplosionComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 4/16/2017.
 */

public class LaunchSmokeEntity extends GameEntity {

    public  LaunchSmokeEntity(Vector2 position) {
        ExplosionComponent explosion = new ExplosionComponent(GameColors.LAUNCH_SMOKE);
        explosion.spreadCount = 2;
        explosion.particleSize = 75;
        explosion.decay = 3;
        addComponent(explosion);

        addComponent(new TransformComponent(position.cpy(), Geom.NO_ROTATION));
    }
}
