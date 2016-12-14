package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.RotationComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.input.ActiveTouch;
import com.bitdecay.game.input.TouchTracker;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/8/2016.
 */
public class BoosterActivationSystem extends AbstractIteratingGameSystem implements InputProcessor {

    TouchTracker tracker = new TouchTracker(5);

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoosterComponent boost = entity.getComponent(BoosterComponent.class);

        BoostActivateButton button = entity.getComponent(BoostActivateButton.class);
        button.pressed = false;

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        RotationComponent rotation = entity.getComponent(RotationComponent.class);

        for (ActiveTouch touch : tracker.activeTouches) {
            if (button.activeArea.contains(touch.currentLocation)) {
                Vector2 boostVector = Geom.rotateSinglePoint(new Vector2( boost.strength, 0), rotation.angle);
                velocity.currentVelocity.add(boostVector.scl(delta));
                button.pressed = true;
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(BoostActivateButton.class) &&
                entity.hasComponent(RotationComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
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
