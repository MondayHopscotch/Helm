package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.control.BoostControlComponent;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.component.DelayedAddComponent;
import com.bitdecay.game.component.SteeringComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.component.TimerComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.ShipLaunchComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.unlock.StatName;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    private float PLAYER_BOOST_STRENGTH = 25;

    private TouchTracker touches = new TouchTracker(5);

    public PlayerStartLevelSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {

        BoostControlComponent boostInput = entity.getComponent(BoostControlComponent.class);

        boolean launchTouchFound = false;

        for (ActiveTouch touch : touches.activeTouches) {
            if (boostInput.activeArea.contains(touch.startingLocation)) {
                launchTouchFound = true;
            }
        }

        if (launchTouchFound) {
            entity.removeComponent(ShipLaunchComponent.class);

            // reset player input so the ship doesn't start pointing some weird direction
            SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
            steering.angle = SteeringControlComponent.ANGLE_NOT_SET;

            VelocityComponent velocity = new VelocityComponent();
            velocity.currentVelocity.set(LAUNCH_VELOCITY);
            entity.addComponent(velocity);

            TimerComponent timer = new TimerComponent();
            entity.addComponent(timer);

            DelayedAddComponent.DelayedAdd boosterDelay = new DelayedAddComponent.DelayedAdd(new BoosterComponent(PLAYER_BOOST_STRENGTH), PLAYER_CONTROL_DELAY);
            DelayedAddComponent.DelayedAdd collisionDelay = new DelayedAddComponent.DelayedAdd(new CollisionKindComponent(CollisionKind.PLAYER), PLAYER_CONTROL_DELAY);
            DelayedAddComponent.DelayedAdd steeringDelay = new DelayedAddComponent.DelayedAdd(new SteeringComponent(), PLAYER_CONTROL_DELAY / 2);

            entity.addComponent(new DelayedAddComponent(boosterDelay, collisionDelay, steeringDelay));
            pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_LAUNCH);
            Helm.stats.add(StatName.LAUNCHES, 1);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ShipLaunchComponent.class) &&
                entity.hasComponent(SteeringControlComponent.class) &&
                entity.hasComponent(BoostControlComponent.class);
    }

    @Override
    public void reset() {
        touches = new TouchTracker(5);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touches.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touches.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touches.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
