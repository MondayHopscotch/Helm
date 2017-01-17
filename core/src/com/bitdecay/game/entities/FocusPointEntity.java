package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.ForcedRemoveComponent;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 1/12/2017.
 */
public class FocusPointEntity extends GameEntity {

    public FocusPointEntity(Circle focusPoint) {
        addComponent(new TransformComponent(new Vector2(focusPoint.x, focusPoint.y), Geom.NO_ROTATION));
        addComponent(new ProximityComponent(focusPoint.radius, new ForcedRemoveComponent(CameraFollowComponent.class), false));
        addComponent(new CameraFollowComponent());
    }
}
