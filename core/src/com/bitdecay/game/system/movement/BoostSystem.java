package com.bitdecay.game.system.movement;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.control.BoostControlComponent;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.FuelComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/14/2016.
 */
public class BoostSystem extends AbstractIteratingGameSystem {

    public BoostSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoosterComponent boost = entity.getComponent(BoosterComponent.class);
        FuelComponent fuel = entity.getComponent(FuelComponent.class);

        BoostControlComponent button = entity.getComponent(BoostControlComponent.class);

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        TransformComponent transform = entity.getComponent(TransformComponent.class);

        if (button.pressed && fuel.fuelRemaining > 0) {
            fuel.fuelRemaining = Math.max(fuel.fuelRemaining - fuel.burnRate * delta, 0);

            Vector2 boostVector = Geom.rotateSinglePoint(new Vector2(boost.strength, 0), transform.angle);
            velocity.currentVelocity.add(boostVector.scl(delta));

            boost.engaged = true;

            pilot.doMusic(SoundMode.RESUME, MusicLibrary.SHIP_BOOST);
        } else {
            boost.engaged = false;

            pilot.doMusic(SoundMode.PAUSE, MusicLibrary.SHIP_BOOST);
        }
        // button being pressed will update every frame
//        button.pressed = false;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(BoostControlComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class) &&
                entity.hasComponent(FuelComponent.class);
    }
}
