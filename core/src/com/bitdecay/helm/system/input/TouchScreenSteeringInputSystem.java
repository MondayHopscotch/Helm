package com.bitdecay.helm.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.BoostCountComponent;
import com.bitdecay.helm.component.HasSteeredComponent;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.input.ActiveTouch;
import com.bitdecay.helm.input.TouchTracker;
import com.bitdecay.helm.prefs.GamePrefs;

/**
 * Created by Monday on 12/16/2016.
 */
public class TouchScreenSteeringInputSystem extends AbstractInputSystem {

    private static final int REFERENCE_SCREEN_WIDTH = 1920;
    private static final int REFERENCE_SCREEN_HEIGHT = 1080;

    public static int BASE_LINEARITY = 400;
    private static float BASE_INTERSECTION = .5f;

    com.bitdecay.helm.input.TouchTracker tracker = new com.bitdecay.helm.input.TouchTracker(5);

    Vector2 deltaVector = new Vector2();

    float tempAngle = 0;

    private HasSteeredComponent hasSteered;

    public TouchScreenSteeringInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public boolean shouldReset() {
        return false;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);

        int prefSensitivity = Helm.prefs.getInteger(GamePrefs.SENSITIVITY, GamePrefs.SENSITIVITY_DEFAULT);

        control.endPoint = null;

        // track our angle for recording purposes
        tempAngle = control.angle;

        for (ActiveTouch touch : tracker.getTouches()) {
            if (control.activeArea.contains(touch.startingLocation)) {

                touch.consumeDeltaInto(deltaVector);
                int sensitivity = BASE_LINEARITY - prefSensitivity;
                System.out.println("Raw Delta: " + deltaVector);
                scaleBasedOnScreenSize(deltaVector);
                System.out.println("Scaled Delta: " + deltaVector);
                accelerate(deltaVector, sensitivity, BASE_INTERSECTION);
                System.out.println("Accel Delta: " + deltaVector);
                control.angle -= deltaVector.x / sensitivity;
                System.out.println();

                if (control.angle != SteeringControlComponent.ANGLE_NOT_SET) {
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
            // we just steered
            if (entity.hasComponent(HasSteeredComponent.class)) {
                hasSteered = entity.getComponent(HasSteeredComponent.class);
                hasSteered.playerHasSteered = true;
            }
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

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.control.SteeringControlComponent.class,
                com.bitdecay.helm.component.PlayerActiveComponent.class
        );
    }

    @Override
    public void reset() {
        tracker = new TouchTracker(5);

        if (hasSteered != null) {
            hasSteered.playerHasSteered = false;
            hasSteered = null;
        }
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
