package com.bitdecay.helm.system.movement;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.component.GravityProducerComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class GravityFinderSystem extends AbstractIteratingGameSystem {


    public static Array<GameEntity> foundGravitySources = new Array<>(10);

    public GravityFinderSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void before() {
        foundGravitySources.clear();
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        foundGravitySources.add(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                GravityProducerComponent.class,
                TransformComponent.class
        );
    }
}
