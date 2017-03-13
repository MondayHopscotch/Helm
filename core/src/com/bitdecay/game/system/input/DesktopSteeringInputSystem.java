package com.bitdecay.game.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.PlayerActiveComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;

/**
 * Created by Monday on 3/12/2017.
 */

public class DesktopSteeringInputSystem extends AbstractInputSystem {
    float tempAngle = 0;

    float rotationSpeed = 3;

    public DesktopSteeringInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);

        // track our angle for recording purposes
        tempAngle = control.angle;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            control.angle += (rotationSpeed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            control.angle -= (rotationSpeed * delta);
        }

        if (control.angle != SteeringControlComponent.ANGLE_NOT_SET) {
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
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                SteeringControlComponent.class,
                PlayerActiveComponent.class
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
