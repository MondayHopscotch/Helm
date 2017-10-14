package com.bitdecay.helm.system.input;

import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;

/**
 * Created by Monday on 12/8/2016.
 */
public class TouchScreenBoosterInputSystem extends com.bitdecay.helm.system.input.AbstractInputSystem {

    com.bitdecay.helm.input.TouchTracker tracker = new com.bitdecay.helm.input.TouchTracker(5);

    boolean pressTrack = false;

    public TouchScreenBoosterInputSystem(com.bitdecay.helm.GamePilot pilot) {
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
