package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.entities.ExplosionEntity;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;

/**
 * Created by Monday on 12/18/2016.
 */
public class CrashSystem extends AbstractIteratingGameSystem {
    public CrashSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        entity.removeComponent(SteeringControlComponent.class);
        pilot.doMusic(SoundMode.STOP, MusicLibrary.SHIP_BOOST);
        pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_CRASH);

        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

        ExplosionEntity explosionEntity = new ExplosionEntity(transformComponent.position);
        levelPlayer.addEntity(explosionEntity);
        levelPlayer.removeEntity(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                CrashComponent.class,
                TransformComponent.class
        );
    }
}
