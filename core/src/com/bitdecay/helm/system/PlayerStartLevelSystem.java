package com.bitdecay.helm.system;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.ExplosionComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.entities.LaunchSmokeEntity;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.unlock.StatName;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem implements InputProcessor {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 5);
    private static final float PLAYER_CONTROL_DELAY = .7f;

    private float PLAYER_BOOST_STRENGTH = 25;

    private com.bitdecay.helm.input.TouchTracker touches = new com.bitdecay.helm.input.TouchTracker(5);

    public PlayerStartLevelSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        BoostControlComponent boostInput = entity.getComponent(BoostControlComponent.class);

        boolean launchTouchFound = false;

        for (com.bitdecay.helm.input.ActiveTouch touch : touches.activeTouches) {
            if (boostInput.activeArea.contains(touch.startingLocation)) {
                launchTouchFound = true;
            }
        }

        if (launchTouchFound) {
            levelPlayer.beginInputReplayCapture();
            entity.removeComponent(com.bitdecay.helm.component.ShipLaunchComponent.class);

            // reset player input so the ship doesn't start pointing some weird direction
            SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
            steering.angle = SteeringControlComponent.ANGLE_NOT_SET;

            VelocityComponent velocity = new VelocityComponent();
            velocity.currentVelocity.set(LAUNCH_VELOCITY);
            entity.addComponent(velocity);

            com.bitdecay.helm.component.TimerComponent timer = new com.bitdecay.helm.component.TimerComponent();
            entity.addComponent(timer);

            com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

            LaunchSmokeEntity launchExplosion = new LaunchSmokeEntity(transform.position.cpy().add(0, -100));
            ExplosionComponent explosion = launchExplosion.getComponent(ExplosionComponent.class);
            explosion.spreadCount = 2;
            levelPlayer.addEntity(launchExplosion);


            com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd boosterDelay = new com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd(new BoosterComponent(PLAYER_BOOST_STRENGTH), PLAYER_CONTROL_DELAY);
            com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd collisionDelay = new com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd(new CollisionKindComponent(CollisionKind.PLAYER), PLAYER_CONTROL_DELAY);
            com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd steeringDelay = new com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd(new com.bitdecay.helm.component.SteeringComponent(), PLAYER_CONTROL_DELAY / 2);

            com.bitdecay.helm.component.DelayedAddComponent delayedAddComponent = entity.getComponent(com.bitdecay.helm.component.DelayedAddComponent.class);
            delayedAddComponent.delays.add(boosterDelay);
            delayedAddComponent.delays.add(collisionDelay);
            delayedAddComponent.delays.add(steeringDelay);

            entity.addComponent(new com.bitdecay.helm.component.DelayedAddComponent(boosterDelay, collisionDelay, steeringDelay));

            pilot.doSound(com.bitdecay.helm.sound.SoundMode.PLAY, SFXLibrary.SHIP_LAUNCH);
            levelPlayer.countStat(StatName.LAUNCHES, 1);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(com.bitdecay.helm.component.ShipLaunchComponent.class,
                SteeringControlComponent.class,
                BoostControlComponent.class,
                com.bitdecay.helm.component.TransformComponent.class,
                com.bitdecay.helm.component.BodyDefComponent.class,
                com.bitdecay.helm.component.DelayedAddComponent.class);
    }

    @Override
    public void reset() {
        touches = new com.bitdecay.helm.input.TouchTracker(5);
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