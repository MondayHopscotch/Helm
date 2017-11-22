package com.bitdecay.helm.system.collision;

import com.bitdecay.helm.component.TimerComponent;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.collision.CollisionKind;

/**
 * Created by Monday on 12/18/2016.
 */
public class CrashSystem extends com.bitdecay.helm.system.AbstractIteratingGameSystem {
    public CrashSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        entity.removeComponent(com.bitdecay.helm.component.control.SteeringControlComponent.class);
        pilot.doMusic(com.bitdecay.helm.sound.SoundMode.STOP, com.bitdecay.helm.sound.MusicLibrary.SHIP_BOOST);
        pilot.doSound(com.bitdecay.helm.sound.SoundMode.PLAY, SFXLibrary.SHIP_CRASH);
        pilot.doSound(com.bitdecay.helm.sound.SoundMode.PLAY, SFXLibrary.SHIP_EXPLODE);

        if (entity.hasComponent(TimerComponent.class)) {
            TimerComponent timer = entity.getComponent(TimerComponent.class);
            levelPlayer.rollStat(com.bitdecay.helm.unlock.StatName.FLIGHT_TIME, timer.secondsElapsed);
        }

        com.bitdecay.helm.component.TransformComponent transformComponent = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

        com.bitdecay.helm.component.CrashComponent crash = entity.getComponent(com.bitdecay.helm.component.CrashComponent.class);

        switch (crash.with) {
            case WALL:
                levelPlayer.countStat(com.bitdecay.helm.unlock.StatName.WALL_CRASHES, 1);
                break;
            case LANDING_PLATFORM:
                levelPlayer.countStat(com.bitdecay.helm.unlock.StatName.LANDING_PLAT_CRASHES, 1);
                break;
            case LEVEL_BOUNDARY:
                levelPlayer.countStat(com.bitdecay.helm.unlock.StatName.OOB_CRASHES, 1);
                break;
            case GRAVITY_WELL:
                levelPlayer.countStat(com.bitdecay.helm.unlock.StatName.GRAV_WELL_CRASHES, 1);
                break;
            default:
                // do nothing
        }

        com.bitdecay.helm.entities.PlayerExplosionEntity playerExplosionEntity = new com.bitdecay.helm.entities.PlayerExplosionEntity(transformComponent.position);
        levelPlayer.addEntity(playerExplosionEntity);
        levelPlayer.removeEntity(entity);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.CrashComponent.class,
                com.bitdecay.helm.component.TransformComponent.class
        );
    }
}
