package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.CrashComponent;
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
        pilot.doMusic(SoundMode.STOP, MusicLibrary.SHIP_BOOST);
        pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_CRASH);
        pilot.requestRestartLevel();
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CrashComponent.class);
    }
}
