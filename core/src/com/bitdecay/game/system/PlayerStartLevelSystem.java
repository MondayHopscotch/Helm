package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoostControlComponent;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.DelayedAddComponent;
import com.bitdecay.game.component.SteeringComponent;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.WaitingToStartComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    TouchTracker tracker = new TouchTracker(5);

    public PlayerStartLevelSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        WaitingToStartComponent wait = entity.getComponent(WaitingToStartComponent.class);
        wait.delayBeforeStartAllowed -= delta;
        if (wait.delayBeforeStartAllowed > 0) {
            return;
        }

        BoostControlComponent button = entity.getComponent(BoostControlComponent.class);
        SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);

        pilot.doMusic(SoundMode.PAUSE, MusicLibrary.SHIP_BOOST);

        for (ActiveTouch touch : tracker.activeTouches) {
            if (button.activeArea.contains(touch.startingLocation)) {
                steering.angle = SteeringControlComponent.ANGLE_NOT_SET;
                VelocityComponent velocity = new VelocityComponent();
                velocity.currentVelocity.set(LAUNCH_VELOCITY);
                entity.addComponent(velocity);

                DelayedAddComponent.DelayedAdd boosterDelay = new DelayedAddComponent.DelayedAdd(new BoosterComponent(25), PLAYER_CONTROL_DELAY);
                DelayedAddComponent.DelayedAdd steeringDelay = new DelayedAddComponent.DelayedAdd(new SteeringComponent(), PLAYER_CONTROL_DELAY / 2);

                entity.addComponent(new DelayedAddComponent(boosterDelay, steeringDelay));
                entity.removeComponent(WaitingToStartComponent.class);
                pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_LAUNCH);
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(WaitingToStartComponent.class) &&
                entity.hasComponent(BoostControlComponent.class) &&
                entity.hasComponent(SteeringControlComponent.class);
    }

    @Override
    public void reset() {
        tracker = new TouchTracker(5);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tracker.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tracker.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // booster doesn't track drags
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
