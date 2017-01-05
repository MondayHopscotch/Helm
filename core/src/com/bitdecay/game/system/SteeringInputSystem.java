package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/16/2016.
 */
public class SteeringInputSystem extends AbstractIteratingGameSystem implements InputProcessor {

    public static float STEERING_SENSITIVITY = 50;

    TouchTracker tracker = new TouchTracker(5);

    private Vector2 simpleSteeringStartVector = new Vector2();

    public SteeringInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);
        control.sensitivity = STEERING_SENSITIVITY;

        boolean dynamicSteering = Helm.prefs.getBoolean(GamePrefs.USE_DYNAMIC_STEERING_CONTROLS, GamePrefs.USE_DYNAMIC_STEERING_CONTROLS_DEFAULT);

        if (dynamicSteering) {
            control.startPoint = null;
        } else {
            setSimpleSteeringStartPoint(control);
        }
        control.endPoint = null;

        for (ActiveTouch touch : tracker.activeTouches) {
            if (control.activeArea.contains(touch.startingLocation)) {

                if (dynamicSteering) {
                    updateDynamicControls(control, touch);
                } else {
                    updateSimpleControls(control, touch);
                }

                float deltaX = control.endPoint.x - control.startPoint.x;
                float deltaY = control.endPoint.y - control.startPoint.y;
                Vector2 touchVector = new Vector2(deltaX, deltaY);
                if (touchVector.len() > STEERING_SENSITIVITY) {
                    float angle = (float) Math.atan2(touchVector.y, touchVector.x);
                    control.angle = angle;
                }
            }
        }
    }

    private void setSimpleSteeringStartPoint(SteeringControlComponent control) {
        float height_ratio = Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_HEIGHT, .3f);
        float width_ratio = Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_WIDTH, .3f);
        control.startPoint = control.activeArea.getSize(simpleSteeringStartVector).scl(width_ratio, height_ratio);
    }

    private void updateDynamicControls(SteeringControlComponent control, ActiveTouch touch) {
        control.startPoint = touch.startingLocation;
        control.endPoint = touch.currentLocation;
    }

    private void updateSimpleControls(SteeringControlComponent control, ActiveTouch touch) {
        control.endPoint = touch.currentLocation;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(SteeringControlComponent.class);
    }

    @Override
    public void reset() {
        // Don't want to reset the steering system?
        tracker = new TouchTracker(5);
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
