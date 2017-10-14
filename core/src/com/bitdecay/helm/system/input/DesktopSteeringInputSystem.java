package com.bitdecay.helm.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Monday on 3/12/2017.
 */

public class DesktopSteeringInputSystem extends AbstractInputSystem {
    float tempAngle = 0;

    float rotationSpeed = 3;

    public DesktopSteeringInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.control.SteeringControlComponent control = entity.getComponent(com.bitdecay.helm.component.control.SteeringControlComponent.class);

        // track our angle for recording purposes
        tempAngle = control.angle;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            control.angle += (rotationSpeed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            control.angle -= (rotationSpeed * delta);
        }

        if (control.angle != com.bitdecay.helm.component.control.SteeringControlComponent.ANGLE_NOT_SET) {
            while (control.angle > MathUtils.PI2) {
                control.angle -= MathUtils.PI2;
            }

            while (control.angle < 0) {
                control.angle += MathUtils.PI2;
            }
        }

        if (tempAngle != control.angle) {
            levelPlayer.recordNewAngle(control.angle);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.control.SteeringControlComponent.class,
                com.bitdecay.helm.component.PlayerActiveComponent.class
        );
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
