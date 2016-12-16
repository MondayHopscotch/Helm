package com.bitdecay.game.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ActiveComponent;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.WaitingToStartComponent;
import com.bitdecay.game.input.TouchTracker;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    TouchTracker tracker = new TouchTracker(5);


    public PlayerStartLevelSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ActiveComponent active = entity.getComponent(ActiveComponent.class);
        active.active = false;
        active.flipControlTimer = 0;

        WaitingToStartComponent wait = entity.getComponent(WaitingToStartComponent.class);
        wait.delayBeforeStartAllowed -= delta;
        if (wait.delayBeforeStartAllowed > 0) {
            return;
        }

        if (tracker.activeTouches.size > 0) {
            VelocityComponent velocity = new VelocityComponent();
            velocity.currentVelocity.set(LAUNCH_VELOCITY);
            entity.addComponent(velocity);

            active.flipControlTimer = PLAYER_CONTROL_DELAY;

            entity.removeComponent(WaitingToStartComponent.class);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(WaitingToStartComponent.class) &&
                entity.hasComponent(ActiveComponent.class);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tracker.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tracker.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // booster doesn't track drags
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
