package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ActiveComponent;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/8/2016.
 */
public class BoosterActivationSystem extends AbstractIteratingGameSystem implements InputProcessor {

    TouchTracker tracker = new TouchTracker(5);

    public BoosterActivationSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ActiveComponent active = entity.getComponent(ActiveComponent.class);
        if (!active.active) {
            return;
        }

        BoostActivateButton button = entity.getComponent(BoostActivateButton.class);
        button.pressed = false;

        for (ActiveTouch touch : tracker.activeTouches) {
            if (button.activeArea.contains(touch.currentLocation)) {
                button.pressed = true;
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoostActivateButton.class) &&
                entity.hasComponent(ActiveComponent.class);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tracker.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // booster doesn't track drags
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tracker.touchUp(screenX, screenY, pointer, button);
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
}
