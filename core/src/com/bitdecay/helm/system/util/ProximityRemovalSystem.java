package com.bitdecay.helm.system.util;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.ProximityComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

import java.util.ArrayList;

/**
 * Created by Monday on 1/12/2017.
 */
public class ProximityRemovalSystem extends AbstractIteratingGameSystem {

    ArrayList<com.bitdecay.helm.GameEntity> proximityEntities = new ArrayList<>();

    public ProximityRemovalSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void before() {
        proximityEntities.clear();
    }

    @Override
    public void after() {
        TransformComponent firstTransform;
        ProximityComponent firstProximity;

        TransformComponent secondTransform;
        ProximityComponent secondProximity;

        Vector2 distanceBetween = new Vector2();

        for (com.bitdecay.helm.GameEntity first : proximityEntities) {
            for (com.bitdecay.helm.GameEntity second : proximityEntities) {
                if (second != first) {
                    firstTransform = first.getComponent(TransformComponent.class);
                    firstProximity = first.getComponent(ProximityComponent.class);

                    secondTransform = second.getComponent(TransformComponent.class);
                    secondProximity = second.getComponent(ProximityComponent.class);

                    distanceBetween.set(firstTransform.position).sub(secondTransform.position);
                    if (distanceBetween.len() < firstProximity.radius + secondProximity.radius) {
                        // This loop is n^2, so we'll hit the other object when it is 'first'
                        if (firstProximity.applyWhenTriggered != null) {
                            first.addComponent(firstProximity.applyWhenTriggered);
                        }
                        break;
                    }
                }
            }

        }

    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        proximityEntities.add(entity);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                ProximityComponent.class
        );
    }
}
