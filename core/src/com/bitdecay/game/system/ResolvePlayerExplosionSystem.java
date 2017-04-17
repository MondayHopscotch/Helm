package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ExplosionComponent;
import com.bitdecay.game.component.PartOfPlayerComponent;

/**
 * Created by Monday on 4/16/2017.
 */

public class ResolvePlayerExplosionSystem extends AbstractIteratingGameSystem {
    public ResolvePlayerExplosionSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);

        if (explosion.particleSize <= 0) {
            pilot.requestRestartLevel();
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                ExplosionComponent.class,
                PartOfPlayerComponent.class
        );
    }
}
