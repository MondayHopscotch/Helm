package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.ExplosionComponent;
import com.bitdecay.game.component.PartOfPlayerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.GameColors;

/**
 * Created by Monday on 2/16/2017.
 */

public class PlayerExplosionEntity extends GameEntity {

    public PlayerExplosionEntity(Vector2 position) {
        addComponent(new TransformComponent(position.cpy(), Geom.NO_ROTATION));
        addComponent(new ExplosionComponent(GameColors.EXPLOSION));
        addComponent(new CameraFollowComponent());
        addComponent(new PartOfPlayerComponent());
    }
}
