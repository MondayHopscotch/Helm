package com.bitdecay.helm.system.util;

import com.bitdecay.helm.component.ForcedRemoveComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/12/2017.
 */
public class RemoveComponentSystem extends AbstractIteratingGameSystem {
    public RemoveComponentSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        ForcedRemoveComponent remove = entity.getComponent(ForcedRemoveComponent.class);
        entity.removeComponent(remove.componentClazz);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(ForcedRemoveComponent.class);
    }
}
