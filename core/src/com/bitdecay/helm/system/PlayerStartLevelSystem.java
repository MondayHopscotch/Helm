package com.bitdecay.helm.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.DelayedAddComponent;
import com.bitdecay.helm.component.ExplosionComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.component.SteeringComponent;
import com.bitdecay.helm.component.TimerComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.entities.LaunchSmokeEntity;
import com.bitdecay.helm.input.ActiveTouch;
import com.bitdecay.helm.input.TouchTracker;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.unlock.StatName;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    public static final float PLAYER_BOOST_STRENGTH = 25;

    public TouchTracker touches = new TouchTracker(5);

    public PlayerStartLevelSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoostControlComponent boostInput = entity.getComponent(BoostControlComponent.class);

        boolean launchTouchFound = false;

        for (ActiveTouch touch : touches.activeTouches) {
            if (boostInput.activeArea.contains(touch.startingLocation)) {
                launchTouchFound = true;
            }
        }

        if (launchTouchFound) {
            levelPlayer.beginTickCapture();
            entity.removeComponent(ShipLaunchComponent.class);

            // reset player input so the ship doesn't start pointing some weird direction
            SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
            steering.angle = SteeringControlComponent.ANGLE_NOT_SET;

            VelocityComponent velocity = new VelocityComponent();
            velocity.currentVelocity.set(LAUNCH_VELOCITY);
            entity.addComponent(velocity);

            TimerComponent timer = new TimerComponent();
            entity.addComponent(timer);

            TransformComponent transform = entity.getComponent(TransformComponent.class);

            LaunchSmokeEntity launchExplosion = new LaunchSmokeEntity(transform.position.cpy().add(0, -100));
            ExplosionComponent explosion = launchExplosion.getComponent(ExplosionComponent.class);
            explosion.spreadCount = 2;
            levelPlayer.addEntity(launchExplosion);

            addPlayerStartComponents(entity.getComponent(DelayedAddComponent.class));

            pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_LAUNCH);
            levelPlayer.countStat(StatName.LAUNCHES, 1);
        }
    }

    public static DelayedAddComponent addPlayerStartComponents(DelayedAddComponent delay) {
        DelayedAddComponent.DelayedAdd boosterDelay = new DelayedAddComponent.DelayedAdd(new BoosterComponent(PLAYER_BOOST_STRENGTH), PLAYER_CONTROL_DELAY);
        DelayedAddComponent.DelayedAdd collisionDelay = new DelayedAddComponent.DelayedAdd(new CollisionKindComponent(CollisionKind.PLAYER), PLAYER_CONTROL_DELAY);
        DelayedAddComponent.DelayedAdd steeringDelay = new DelayedAddComponent.DelayedAdd(new SteeringComponent(), PLAYER_CONTROL_DELAY / 2);

        delay.delays.add(boosterDelay);
        delay.delays.add(collisionDelay);
        delay.delays.add(steeringDelay);

        return delay;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(ShipLaunchComponent.class,
                SteeringControlComponent.class,
                BoostControlComponent.class,
                TransformComponent.class,
                BodyDefComponent.class,
                DelayedAddComponent.class);
    }

    @Override
    public void reset() {
        touches = new TouchTracker(5);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touches.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touches.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touches.touchDragged(screenX, screenY, pointer);
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
