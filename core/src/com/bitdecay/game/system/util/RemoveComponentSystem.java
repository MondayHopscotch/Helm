package com.bitdecay.game.system.util;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ForcedRemoveComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/12/2017.
 */
public class RemoveComponentSystem extends AbstractIteratingGameSystem {
    public RemoveComponentSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ForcedRemoveComponent remove = entity.getComponent(ForcedRemoveComponent.class);
        entity.removeComponent(remove.componentClazz);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ForcedRemoveComponent.class);
    }
}
