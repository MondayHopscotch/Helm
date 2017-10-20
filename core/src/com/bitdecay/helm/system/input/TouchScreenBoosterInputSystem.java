package com.bitdecay.helm.system.input;

import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.component.BoostCountComponent;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.PlayerActiveComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.input.TouchTracker;

/**
 * Created by Monday on 12/8/2016.
 */
public class TouchScreenBoosterInputSystem extends AbstractInputSystem {

    TouchTracker tracker = new TouchTracker(5);

    boolean pressTrack = false;

    BoostCountComponent boostCounter;

    public TouchScreenBoosterInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        BoostControlComponent button = entity.getComponent(BoostControlComponent.class);
        pressTrack = button.pressed;
        button.pressed = false;
        for (com.bitdecay.helm.input.ActiveTouch touch : tracker.activeTouches) {
            if (button.activeArea.contains(touch.currentLocation)) {
                button.pressed = true;
            }
        }
        if (pressTrack != button.pressed) {
            if (pressTrack == false) {
                // we just pushed boost
                if (entity.hasComponent(BoostCountComponent.class)) {
                    boostCounter = entity.getComponent(BoostCountComponent.class);
                    boostCounter.boostCount++;
                }
            }
            levelPlayer.recordNewBoostToggle();
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                BoosterComponent.class,
                BoostControlComponent.class,
                PlayerActiveComponent.class
        );
    }

    @Override
    public void reset() {
        tracker = new TouchTracker(5);

        if (boostCounter != null) {
            boostCounter.boostCount = 0;
            boostCounter = null;
        }
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
