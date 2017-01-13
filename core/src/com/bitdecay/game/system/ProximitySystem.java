package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.TransformComponent;

import java.util.ArrayList;

/**
 * Created by Monday on 1/12/2017.
 */
public class ProximitySystem extends AbstractIteratingGameSystem {

    ArrayList<GameEntity> proximityEntities = new ArrayList<>();

    public ProximitySystem(GamePilot pilot) {
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

        for (GameEntity first : proximityEntities) {
            for (GameEntity second : proximityEntities) {
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
    public void actOnSingle(GameEntity entity, float delta) {
        proximityEntities.add(entity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                ProximityComponent.class
        );
    }
}
