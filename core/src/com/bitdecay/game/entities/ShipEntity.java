package com.bitdecay.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.GravityComponent;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.RotationComponent;
import com.bitdecay.game.component.ShipBodyComponent;
import com.bitdecay.game.component.SteeringTouchArea;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipEntity extends GameEntity {

    public ShipEntity() {
        addComponent(new BoosterComponent(20));
        addComponent(new BoostActivateButton(new Rectangle(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));

        addComponent(new ShipBodyComponent(new Vector2[]{new Vector2(-10, -5), new Vector2(-10, 5), new Vector2(10, 0)}));
        addComponent(new SteeringTouchArea(new Rectangle(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));
        addComponent(new PositionComponent(new Vector2(0, 0)));
        addComponent(new RotationComponent(Geom.ROTATION_UP));
        addComponent(new VelocityComponent());

        addComponent(new GravityComponent());
    }
}
