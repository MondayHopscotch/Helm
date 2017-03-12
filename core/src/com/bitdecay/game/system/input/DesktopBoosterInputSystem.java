package com.bitdecay.game.system.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.PlayerActiveComponent;
import com.bitdecay.game.component.control.BoostControlComponent;

/**
 * Created by Monday on 3/12/2017.
 */

public class DesktopBoosterInputSystem extends AbstractInputSystem {
    boolean pressTrack = false;

    public DesktopBoosterInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoostControlComponent button = entity.getComponent(BoostControlComponent.class);
        pressTrack = button.pressed;
        button.pressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (pressTrack != button.pressed) {
            levelPlayer.recordNewBoostToggle();
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                BoosterComponent.class,
                BoostControlComponent.class,
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
