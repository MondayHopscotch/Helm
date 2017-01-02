package com.bitdecay.game.system;

import com.badlogic.gdx.math.MathUtils;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.FuelComponent;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.RateLandingComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SoundMode;

/**
 * Created by Monday on 12/17/2016.
 */
public class LandingSystem extends AbstractIteratingGameSystem {

    public static final float MAX_LANDING_SPEED = 10;
    public static final float MAX_LANDING_SPEED_FOR_SCORE = 7;
    public static final float LANDING_SPEED_MERCY = .225f;

    public static final float MAX_LANDING_ANGLE = MathUtils.PI / 10;
    public static final float MAX_LANDING_ANGLE_FOR_SCORE = MathUtils.PI / 15;
    public static final float LANDING_ANGLE_MERCY = .02f;

    public LandingSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        // just ensure the entity can't boost
        entity.removeComponent(BoosterComponent.class);
        pilot.doMusic(SoundMode.STOP, MusicLibrary.SHIP_BOOST);

        entity.removeComponent(RateLandingComponent.class);

        FuelComponent fuel = entity.getComponent(FuelComponent.class);

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        // we don't want the ship to move any more once it's landed
        entity.removeComponent(VelocityComponent.class);

        TransformComponent transform = entity.getComponent(TransformComponent.class);

        // check landing and either pass/fail
        float radsAwayFromStraightUp = Math.abs(transform.angle - Geom.ROTATION_UP);

        if (Math.abs(velocity.currentVelocity.y) > MAX_LANDING_SPEED ||
                radsAwayFromStraightUp > MAX_LANDING_ANGLE) {
            entity.addComponent(new CrashComponent());
            return;
        }

        System.out.println("Landing Velocity: " + velocity.currentVelocity.y);
        System.out.println("Landing Angle: " + radsAwayFromStraightUp);

        LandingScore score = new LandingScore();

        float angleScoreScalar;
        if (radsAwayFromStraightUp > MAX_LANDING_ANGLE_FOR_SCORE) {
            angleScoreScalar = 0;
        } else {
            angleScoreScalar = (MAX_LANDING_ANGLE_FOR_SCORE - radsAwayFromStraightUp) / MAX_LANDING_ANGLE_FOR_SCORE;
            // let's just be nice and give the player some angle mercy
            angleScoreScalar = Math.min(angleScoreScalar + LANDING_ANGLE_MERCY, 1);

        }

        float speedScoreScalar;
        if (velocity.currentVelocity.len() > MAX_LANDING_SPEED_FOR_SCORE) {
            speedScoreScalar = 0;
        } else {
            speedScoreScalar = (MAX_LANDING_SPEED_FOR_SCORE - velocity.currentVelocity.len()) / MAX_LANDING_SPEED_FOR_SCORE;

            //a zero-speed landing is impossible, so make a full score doable
            speedScoreScalar = Math.min(speedScoreScalar + LANDING_SPEED_MERCY, 1);
        }

        float fuelPercentage = fuel.fuelRemaining / fuel.maxFuel;

        score.angleScore = (int) (LandingScore.MAX_ANGLE_SCORE * angleScoreScalar);
        score.speedScore = (int)(LandingScore.MAX_SPEED_SCORE * speedScoreScalar);
        score.fuelLeft = fuelPercentage;

        pilot.finishLevel(score);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(RateLandingComponent.class) &&
                entity.hasComponent(VelocityComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(FuelComponent.class);
    }
}
