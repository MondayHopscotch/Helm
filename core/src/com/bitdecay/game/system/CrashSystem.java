package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.TimerComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.entities.ExplosionEntity;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.unlock.StatName;

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
        pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_EXPLODE);

        TimerComponent timer = entity.getComponent(TimerComponent.class);
        Helm.stats.roll(StatName.FLIGHT_TIME, timer.secondsElapsed);

        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

        CrashComponent crash = entity.getComponent(CrashComponent.class);

        switch (crash.with) {
            case WALL:
                Helm.stats.count(StatName.WALL_CRASHES, 1);
                break;
            case LANDING_PLATFORM:
                Helm.stats.count(StatName.LANDING_PLAT_CRASHES, 1);
                break;
            case LEVEL_BOUNDARY:
                Helm.stats.count(StatName.OOB_CRASHES, 1);
                break;
            case GRAVITY_WELL:
                Helm.stats.count(StatName.GRAV_WELL_CRASHES, 1);
                break;
            default:
                // do nothing
        }

        ExplosionEntity explosionEntity = new ExplosionEntity(transformComponent.position);
        levelPlayer.addEntity(explosionEntity);
        levelPlayer.removeEntity(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                CrashComponent.class,
                TransformComponent.class,
                TimerComponent.class
        );
    }
}
