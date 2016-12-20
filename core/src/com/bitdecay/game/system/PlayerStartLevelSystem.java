package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.DelayedAddComponent;
import com.bitdecay.game.component.SteeringComponent;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.ShipLaunchComponent;
import com.bitdecay.game.input.TouchTracker;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    TouchTracker tracker = new TouchTracker(5);

    public PlayerStartLevelSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ShipLaunchComponent launchCountdown = entity.getComponent(ShipLaunchComponent.class);
        launchCountdown.countdown -= delta;
        if (launchCountdown.countdown > 0) {
            return;
        }

        entity.removeComponent(ShipLaunchComponent.class);

        // remove player input so the ship doesn't start pointing some weird direction
        SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
        steering.angle = SteeringControlComponent.ANGLE_NOT_SET;

        VelocityComponent velocity = new VelocityComponent();
        velocity.currentVelocity.set(LAUNCH_VELOCITY);
        entity.addComponent(velocity);

        DelayedAddComponent.DelayedAdd boosterDelay = new DelayedAddComponent.DelayedAdd(new BoosterComponent(25), PLAYER_CONTROL_DELAY);
        DelayedAddComponent.DelayedAdd steeringDelay = new DelayedAddComponent.DelayedAdd(new SteeringComponent(), PLAYER_CONTROL_DELAY / 2);

        entity.addComponent(new DelayedAddComponent(boosterDelay, steeringDelay));
        pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_LAUNCH);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ShipLaunchComponent.class) &&
                entity.hasComponent(SteeringControlComponent.class);
    }

    @Override
    public void reset() {
        tracker = new TouchTracker(5);
    }
}
