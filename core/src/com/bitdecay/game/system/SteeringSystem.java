package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ActiveComponent;
import com.bitdecay.game.component.SteeringTouchArea;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringSystem extends AbstractIteratingGameSystem implements InputProcessor {

    public static float STEERING_SENSITIVITY = 25;

    TouchTracker tracker = new TouchTracker(5);

    public SteeringSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ActiveComponent active = entity.getComponent(ActiveComponent.class);
        if (!active.active) {
            return;
        }

        SteeringTouchArea area = entity.getComponent(SteeringTouchArea.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        for (ActiveTouch touch : tracker.activeTouches) {
            if (area.activeArea.contains(touch.startingLocation)) {
                float deltaX = touch.currentLocation.x - touch.startingLocation.x;
                float deltaY = touch.currentLocation.y - touch.startingLocation.y;
                Vector2 touchVector = new Vector2(deltaX, deltaY);
                if (touchVector.len() > STEERING_SENSITIVITY) {
                    float angle = (float) Math.atan2(touchVector.y, touchVector.x);
                    transform.angle = angle;
                }
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(SteeringTouchArea.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(ActiveComponent.class);
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
