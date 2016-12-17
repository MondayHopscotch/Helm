package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/16/2016.
 */
public class SteeringInputSystem extends AbstractIteratingGameSystem implements InputProcessor {

    public static float STEERING_SENSITIVITY = 25;

    TouchTracker tracker = new TouchTracker(5);

    public SteeringInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);

        for (ActiveTouch touch : tracker.activeTouches) {
            if (control.activeArea.contains(touch.startingLocation)) {
                float deltaX = touch.currentLocation.x - touch.startingLocation.x;
                float deltaY = touch.currentLocation.y - touch.startingLocation.y;
                Vector2 touchVector = new Vector2(deltaX, deltaY);
                if (touchVector.len() > STEERING_SENSITIVITY) {
                    float angle = (float) Math.atan2(touchVector.y, touchVector.x);
                    control.angle = angle;
                }
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(SteeringControlComponent.class);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tracker.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        tracker.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tracker.touchUp(screenX, screenY, pointer, button);
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
