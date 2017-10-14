package com.bitdecay.helm.system;

import com.bitdecay.helm.component.ExplosionComponent;
import com.bitdecay.helm.component.PartOfPlayerComponent;

/**
 * Created by Monday on 4/16/2017.
 */

public class ResolvePlayerExplosionSystem extends AbstractIteratingGameSystem {
    public ResolvePlayerExplosionSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);

        if (explosion.particleSize <= 0) {
            pilot.requestRestartLevel();
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                ExplosionComponent.class,
                PartOfPlayerComponent.class
        );
    }
}
