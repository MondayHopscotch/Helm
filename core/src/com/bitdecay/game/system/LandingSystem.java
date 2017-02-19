package com.bitdecay.game.system;

import com.badlogic.gdx.math.MathUtils;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.FuelComponent;
import com.bitdecay.game.component.RateLandingComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.component.TimerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.unlock.StatName;

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

        RateLandingComponent landing = entity.getComponent(RateLandingComponent.class);
        entity.removeComponent(RateLandingComponent.class);

        FuelComponent fuel = entity.getComponent(FuelComponent.class);

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        // we don't want the ship to move any more once it's landed
        entity.removeComponent(VelocityComponent.class);

        TransformComponent transform = entity.getComponent(TransformComponent.class);

        // check landing and either pass/fail
        float radsAwayFromStraightUp = Math.abs(transform.angle - Geom.ROTATION_UP);

        float minBodyY = Geom.getMinY(landing.playerGeom);
        float maxPlatY = Geom.getMaxY(landing.landingGeometry);
        float yDifference = maxPlatY - minBodyY;

        System.out.println("LANDING VECTOR: " + velocity.currentVelocity);
        System.out.println("LANDING ANGLE: " + radsAwayFromStraightUp);
        System.out.println("LANDING Y-DIFFERENCE: " + yDifference);

        // velocity checks
        if (Math.abs(velocity.currentVelocity.y) > MAX_LANDING_SPEED ||
                velocity.currentVelocity.y > 0) {
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED TO0 HARD: " + velocity.currentVelocity);
            return;
        }

        // angle checks
        if (radsAwayFromStraightUp > MAX_LANDING_ANGLE) {
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED CROOKED: " + radsAwayFromStraightUp);
            return;
        }

        // positional checks
        if (yDifference > 5) {
            // TODO: Tune this value. This represents how far into the platform the player can be to still count
            // as hitting it from the top
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED FROM WRONG SIDE OF PLAT. Y-Difference: " + (maxPlatY - minBodyY));
            return;
        }

        entity.removeComponent(SteeringControlComponent.class);

        TimerComponent timer = entity.getComponent(TimerComponent.class);
        entity.removeComponent(TimerComponent.class);

        LandingScore score = new LandingScore();
        score.angleScore = rateAngle(radsAwayFromStraightUp);
        score.speedScore = rateSpeed(velocity);
        score.accuracyScore = rateAccuracy(transform, landing);
        score.fuelLeft = fuel.fuelRemaining / fuel.maxFuel;
        score.fuelScore = rateFuelRemaining(fuel);

        score.timeTaken = timer.secondsElapsed;

        Helm.stats.add(StatName.LANDINGS, 1);
        pilot.finishLevel(score);
    }

    private int rateFuelRemaining(FuelComponent fuel) {
        System.out.println("Fuel: " + fuel.fuelRemaining + "/" + fuel.maxFuel);
        // higher starting fuel and higher burn rate have higher reward for remaining fuel
        return (int) ( (fuel.maxFuel * fuel.burnRate) * (fuel.fuelRemaining / fuel.maxFuel));
    }

    private int rateAngle(float radsAwayFromStraightUp) {
        System.out.println("Landing Angle: " + radsAwayFromStraightUp);
        if (radsAwayFromStraightUp > MAX_LANDING_ANGLE_FOR_SCORE) {
            return 0;
        } else {
            float scalar = (MAX_LANDING_ANGLE_FOR_SCORE - radsAwayFromStraightUp) / MAX_LANDING_ANGLE_FOR_SCORE;
            // let's just be nice and give the player some angle mercy
            scalar = Math.min(scalar + LANDING_ANGLE_MERCY, 1);
            return (int) (LandingScore.MAX_ANGLE_SCORE * scalar);
        }
    }

    private int rateSpeed(VelocityComponent velocity) {
        System.out.println("Landing Velocity: " + velocity.currentVelocity.y);
        if (velocity.currentVelocity.len() > MAX_LANDING_SPEED_FOR_SCORE) {
            return 0;
        } else {
            float scalar = (MAX_LANDING_SPEED_FOR_SCORE - velocity.currentVelocity.len()) / MAX_LANDING_SPEED_FOR_SCORE;

            //a zero-speed landing is impossible, so make a full scalar doable
            scalar = Math.min(scalar + LANDING_SPEED_MERCY, 1);
            return (int)(LandingScore.MAX_SPEED_SCORE * scalar);
        }
    }

    private int rateAccuracy(TransformComponent transform, RateLandingComponent landing) {
        float shipX = transform.position.x;
        float middleLandingX = Float.NEGATIVE_INFINITY;

        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < landing.landingGeometry.length; i += 2) {
            minX = Math.min(minX, landing.landingGeometry[i]);
            maxX = Math.max(maxX, landing.landingGeometry[i]);
        }

        System.out.println("MinX: " + minX + "   MaxX: " + maxX);
        System.out.println("shipX: " + shipX);

        middleLandingX = (minX + maxX) / 2;

        float maxDistanceToScore = (maxX - minX) / 2;
        float landingDistance = Math.abs(shipX - middleLandingX);

        // can only be at most half the distance from the center
        float scalar = (maxDistanceToScore - landingDistance) / maxDistanceToScore;
        scalar = Math.min(1, scalar);
        scalar = Math.max(0, scalar);
        return (int) (LandingScore.MAX_ACCURACY_SCORE * scalar);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(RateLandingComponent.class) &&
                entity.hasComponent(VelocityComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(FuelComponent.class) &&
                entity.hasComponent(TimerComponent.class);
    }
}
