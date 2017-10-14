package com.bitdecay.helm.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/16/2016.
 */
public class TouchScreenSteeringInputSystem extends AbstractInputSystem {

    private static final int REFERENCE_SCREEN_WIDTH = 1920;
    private static final int REFERENCE_SCREEN_HEIGHT = 1080;

    public static int BASE_JOYSTICK_SENSITIVITY = 75;

    public static int BASE_LINEARITY = 400;
    private static float BASE_INTERSECTION = .5f;

    com.bitdecay.helm.input.TouchTracker tracker = new com.bitdecay.helm.input.TouchTracker(5);

    Vector2 deltaVector = new Vector2();

    float tempAngle = 0;

    private Vector2 joystickSteeringStartVector = new Vector2();

    public TouchScreenSteeringInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.control.SteeringControlComponent control = entity.getComponent(com.bitdecay.helm.component.control.SteeringControlComponent.class);

        boolean joystickSteering = com.bitdecay.helm.Helm.prefs.getBoolean(com.bitdecay.helm.prefs.GamePrefs.USE_JOYSTICK_STEERING, com.bitdecay.helm.prefs.GamePrefs.USE_JOYSTICK_STEERING_DEFAULT);

        int prefSensitivity = com.bitdecay.helm.Helm.prefs.getInteger(com.bitdecay.helm.prefs.GamePrefs.SENSITIVITY, com.bitdecay.helm.prefs.GamePrefs.SENSITIVITY_DEFAULT);

        int joystickSensitivity = BASE_JOYSTICK_SENSITIVITY;

        if (joystickSteering) {
            setJoystickSteeringStartPoint(control);
            control.sensitivity = joystickSensitivity;
        }
        control.endPoint = null;

        // track our angle for recording purposes
        tempAngle = control.angle;

        for (com.bitdecay.helm.input.ActiveTouch touch : tracker.activeTouches) {
            if (control.activeArea.contains(touch.startingLocation)) {

                if (joystickSteering) {
                    updateJoystickControls(control, touch);
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
                }
                if (control.angle != com.bitdecay.helm.component.control.SteeringControlComponent.ANGLE_NOT_SET) {
                    while (control.angle > MathUtils.PI2) {
                        control.angle -= MathUtils.PI2;
                    }

                    while (control.angle < 0) {
                        control.angle += MathUtils.PI2;
                    }
                }
                break;
            }
        }

        if (tempAngle != control.angle) {
            levelPlayer.recordNewAngle(control.angle);
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

    private void setJoystickSteeringStartPoint(com.bitdecay.helm.component.control.SteeringControlComponent control) {
        float height_ratio = com.bitdecay.helm.Helm.prefs.getFloat(com.bitdecay.helm.prefs.GamePrefs.JOYSTICK_STEERING_HEIGHT, com.bitdecay.helm.prefs.GamePrefs.JOYSTICK_STEERING_HEIGHT_DEFAULT);
        float width_ratio = com.bitdecay.helm.Helm.prefs.getFloat(com.bitdecay.helm.prefs.GamePrefs.JOYSTICK_STEERING_WIDTH, com.bitdecay.helm.prefs.GamePrefs.JOYSTICK_STEERING_WIDTH_DEFAULT);
        control.startPoint = control.activeArea.getSize(joystickSteeringStartVector).scl(width_ratio, height_ratio).add(control.activeArea.x, 0);
    }

    private void updateJoystickControls(com.bitdecay.helm.component.control.SteeringControlComponent control, com.bitdecay.helm.input.ActiveTouch touch) {
        control.endPoint = touch.currentLocation;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.control.SteeringControlComponent.class,
                com.bitdecay.helm.component.PlayerActiveComponent.class
        );
    }

    @Override
    public void reset() {
        tracker = new com.bitdecay.helm.input.TouchTracker(5);
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
