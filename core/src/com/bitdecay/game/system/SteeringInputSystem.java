package com.bitdecay.game.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/16/2016.
 */
public class SteeringInputSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final int REFERENCE_SCREEN_WIDTH = 1920;
    private static final int REFERENCE_SCREEN_HEIGHT = 1080;

    public static int BASE_JOYSTICK_SENSITIVITY = 75;

    public static int BASE_LINEARITY = 400;
    private static float BASE_INTERSECTION = .5f;

    TouchTracker tracker = new TouchTracker(5);

    Vector2 deltaVector = new Vector2();

    private Vector2 simpleSteeringStartVector = new Vector2();

    public SteeringInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);

        boolean joystickSteering = Helm.prefs.getBoolean(GamePrefs.USE_JOYSTICK_STEERING, GamePrefs.USE_JOYSTICK_STEERING_DEFAULT);

        int prefSensitivity = Helm.prefs.getInteger(GamePrefs.SENSITIVITY, GamePrefs.SENSITIVITY_DEFAULT);

        int joystickSensitivity = BASE_JOYSTICK_SENSITIVITY - prefSensitivity;

        if (joystickSteering) {
            setSimpleSteeringStartPoint(control);
            control.sensitivity = joystickSensitivity;
        }
        control.endPoint = null;

        for (ActiveTouch touch : tracker.activeTouches) {
            if (control.activeArea.contains(touch.startingLocation)) {

                if (joystickSteering) {
                    updateSimpleControls(control, touch);
                    float deltaX = control.endPoint.x - control.startPoint.x;
                    float deltaY = control.endPoint.y - control.startPoint.y;
                    Vector2 touchVector = new Vector2(deltaX, deltaY);
                    if (touchVector.len() > joystickSensitivity) {
                        float angle = (float) Math.atan2(touchVector.y, touchVector.x);
                        control.angle = angle;
                    }
                } else {
                    // swipe steering
                    touch.consumeDeltaInto(deltaVector);
                    int sensitivity = BASE_LINEARITY - prefSensitivity;
                    scaleBasedOnScreenSize(deltaVector);
                    accelerate(deltaVector, sensitivity, BASE_INTERSECTION);
                    control.angle -= deltaVector.x / sensitivity;

                    if (control.angle != SteeringControlComponent.ANGLE_NOT_SET) {
                        while (control.angle > MathUtils.PI2) {
                            control.angle -= MathUtils.PI2;
                        }

                        while (control.angle < 0) {
                            control.angle += MathUtils.PI2;
                        }
                    }
                }
            }
        }
    }

    private void scaleBasedOnScreenSize(Vector2 deltaVector) {
        deltaVector.x = deltaVector.x / Gdx.graphics.getWidth() * REFERENCE_SCREEN_WIDTH;
        deltaVector.y = deltaVector.y / Gdx.graphics.getHeight() * REFERENCE_SCREEN_HEIGHT;
    }

    private void accelerate(Vector2 deltaVector, int linearity, float intersection) {
        //function is: 1/(k+j) * x * (|x|+j)
        // k is the intersection
        // j is the linearity
        deltaVector.x = (1f / (intersection + linearity)) * deltaVector.x * (Math.abs(deltaVector.x) + linearity);
        deltaVector.y = (1f / (intersection + linearity)) * deltaVector.y * (Math.abs(deltaVector.y) + linearity);
    }

    private void setSimpleSteeringStartPoint(SteeringControlComponent control) {
        float height_ratio = Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_HEIGHT, GamePrefs.SIMPLE_STEERING_HEIGHT_DEFAULT);
        float width_ratio = Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_WIDTH, GamePrefs.SIMPLE_STEERING_WIDTH_DEFAULT);
        control.startPoint = control.activeArea.getSize(simpleSteeringStartVector).scl(width_ratio, height_ratio);
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
