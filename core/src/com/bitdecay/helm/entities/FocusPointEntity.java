package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.ForcedRemoveComponent;

/**
 * Created by Monday on 1/12/2017.
 */
public class FocusPointEntity extends com.bitdecay.helm.GameEntity {

    public FocusPointEntity(Circle focusPoint) {
        addComponent(new com.bitdecay.helm.component.TransformComponent(new Vector2(focusPoint.x, focusPoint.y), com.bitdecay.helm.math.Geom.NO_ROTATION));
        addComponent(new com.bitdecay.helm.component.ProximityComponent(focusPoint.radius, new ForcedRemoveComponent(com.bitdecay.helm.component.CameraFollowComponent.class), false));
        addComponent(new com.bitdecay.helm.component.CameraFollowComponent());
    }
}
