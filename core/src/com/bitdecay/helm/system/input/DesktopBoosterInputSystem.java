package com.bitdecay.helm.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;

/**
 * Created by Monday on 3/12/2017.
 */

public class DesktopBoosterInputSystem extends AbstractInputSystem {
    boolean pressTrack = false;

    public DesktopBoosterInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public boolean shouldReset() {
        return true;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        BoostControlComponent button = entity.getComponent(BoostControlComponent.class);
        pressTrack = button.pressed;
        button.pressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (pressTrack != button.pressed) {
            levelPlayer.recordNewBoostToggle();
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                BoosterComponent.class,
                BoostControlComponent.class,
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
