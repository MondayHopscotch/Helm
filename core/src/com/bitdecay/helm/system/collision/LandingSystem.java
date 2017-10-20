package com.bitdecay.helm.system.collision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.BoostCountComponent;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.CrashComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.RateLandingComponent;
import com.bitdecay.helm.component.TimerComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.scoring.LandingScore;
import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;
import com.bitdecay.helm.unlock.StatName;

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

    public static final float LANDING_ACCURACY_MERCY = .05f;

    public static final String PERFECT_LANDING_MESSAGE = "Perfect Landing!";
    public static final String ZERO_FUEL_LANDING_MESSAGE = "Zero Fuel Landing!";
    public static final String SINGLE_THRUST_LANDING = "Single Thrust!";

    public LandingSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        // just ensure the entity can't boost
        entity.removeComponent(BoosterComponent.class);
        pilot.doMusic(SoundMode.STOP, com.bitdecay.helm.sound.MusicLibrary.SHIP_BOOST);

        com.bitdecay.helm.component.RateLandingComponent landing = entity.getComponent(com.bitdecay.helm.component.RateLandingComponent.class);
        entity.removeComponent(com.bitdecay.helm.component.RateLandingComponent.class);

        FuelComponent fuel = entity.getComponent(FuelComponent.class);

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        // we don't want the ship to move any more once it's landed
        entity.removeComponent(VelocityComponent.class);

        TransformComponent transform = entity.getComponent(TransformComponent.class);

        // check landing and either pass/fail

        // our relative 'up' from the perspective of the landing platform
        float landingPlatformNormal = landing.geom.rotation + Geom.ROTATION_UP;
        float radsAwayFromStraightUp = Math.abs(transform.angle - landingPlatformNormal);

        Vector2 normalVector = new Vector2(0, 1).rotateRad(landing.geom.rotation).nor();
        float minBodyReference = Geom.getMinAlongVector(landing.playerGeom, normalVector);
        float maxPlatformReference = Geom.getMaxAlongVector(landing.landingGeometry, normalVector);
        float surfaceDifference = maxPlatformReference - minBodyReference;

        System.out.println("LANDING VECTOR: " + velocity.currentVelocity);
        System.out.println("LANDING ANGLE: " + radsAwayFromStraightUp);
        System.out.println("LANDING SURFACE DIFFERENCE: " + surfaceDifference);

        // velocity checks
        float impactSpeed = velocity.currentVelocity.dot(normalVector);
        if (Math.abs(impactSpeed) > MAX_LANDING_SPEED || impactSpeed > 0) {
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED TO0 HARD: " + velocity.currentVelocity + " impactVelocify: " + impactSpeed);
            return;
        }

        // angle checks
        if (radsAwayFromStraightUp > MAX_LANDING_ANGLE) {
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED CROOKED: " + radsAwayFromStraightUp);
            return;
        }

        // positional checks
        if (surfaceDifference > 5) {
            // TODO: Tune this value. This represents how far into the platform the player can be to still count
            // as hitting it from the top
            entity.addComponent(new CrashComponent(CollisionKind.LANDING_PLATFORM));
            System.out.println("LANDED FROM WRONG SIDE OF PLAT. Surface Difference: " + surfaceDifference);
            return;
        }

        entity.removeComponent(SteeringControlComponent.class);

        com.bitdecay.helm.component.TimerComponent timer = entity.getComponent(com.bitdecay.helm.component.TimerComponent.class);
        entity.removeComponent(com.bitdecay.helm.component.TimerComponent.class);

        LandingScore score = new LandingScore();
        score.angleScore = rateAngle(radsAwayFromStraightUp);
        score.speedScore = rateSpeed(impactSpeed);
        score.accuracyScore = rateAccuracy(transform, landing);
        score.fuelLeft = fuel.fuelRemaining / fuel.maxFuel;
        score.fuelScore = rateFuelRemaining(fuel);

        score.timeTaken = timer.secondsElapsed;

        levelPlayer.countStat(StatName.LANDINGS, 1);
        if (score.isLandingPerfect()) {
            ScoreStamps.addPendingStamp(PERFECT_LANDING_MESSAGE);
            levelPlayer.countStat(StatName.PERFECT_LANDINGS, 1);
        }
        if (score.fuelLeft == 0) {
            ScoreStamps.addPendingStamp(ZERO_FUEL_LANDING_MESSAGE);
            levelPlayer.countStat(StatName.ZERO_FUEL_LANDINGS, 1);
        }

        if (entity.hasComponent(BoostCountComponent.class)) {
            BoostCountComponent boostCounter = entity.getComponent(BoostCountComponent.class);
            if (boostCounter.boostCount == 1) {
                ScoreStamps.addPendingStamp(SINGLE_THRUST_LANDING);
            }
        }

        levelPlayer.rollStat(StatName.FLIGHT_TIME, timer.secondsElapsed);
        pilot.finishLevel(score);
    }

    private int rateFuelRemaining(FuelComponent fuel) {
        System.out.println("Fuel: " + fuel.fuelRemaining + "/" + fuel.maxFuel);
        // higher starting fuel and higher burn rate have higher reward for remaining fuel
        return (int) ((fuel.maxFuel * fuel.burnRate) * (fuel.fuelRemaining / fuel.maxFuel));
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

    private int rateSpeed(float impactSpeed) {
        float absImpactSpeed = Math.abs(impactSpeed);
        System.out.println("Landing Velocity: " + impactSpeed);
        if (absImpactSpeed > MAX_LANDING_SPEED_FOR_SCORE) {
            return 0;
        } else {
            float scalar = (MAX_LANDING_SPEED_FOR_SCORE - absImpactSpeed) / MAX_LANDING_SPEED_FOR_SCORE;

            //a zero-speed landing is impossible, so make a full scalar doable
            scalar = Math.min(scalar + LANDING_SPEED_MERCY, 1);
            return (int) (LandingScore.MAX_SPEED_SCORE * scalar);
        }
    }

    private int rateAccuracy(TransformComponent transform, com.bitdecay.helm.component.RateLandingComponent landing) {
        Vector2 surfaceVector = new Vector2(1, 0).rotateRad(landing.geom.rotation).nor();
        float shipCenter = surfaceVector.dot(transform.position);
        float platformMiddle = Float.NEGATIVE_INFINITY;

        float platformMin = Float.POSITIVE_INFINITY;
        float platformMax = Float.NEGATIVE_INFINITY;

        for (int i = 1; i < landing.landingGeometry.length; i += 2) {
            platformMin = Math.min(platformMin, surfaceVector.dot(landing.landingGeometry[i - 1], landing.landingGeometry[i]));
            platformMax = Math.max(platformMax, surfaceVector.dot(landing.landingGeometry[i - 1], landing.landingGeometry[i]));
        }

        System.out.println("PlatformMin: " + platformMin + "   PlatformMax: " + platformMax);
        System.out.println("shipCenter: " + shipCenter);

        platformMiddle = (platformMin + platformMax) / 2;

        float maxDistanceToScore = (platformMax - platformMin) / 2;
        float landingDistance = Math.abs(shipCenter - platformMiddle);

        // can only be at most half the distance from the center
        float scalar = (maxDistanceToScore - landingDistance) / maxDistanceToScore;
        scalar = Math.min(scalar + LANDING_ACCURACY_MERCY, 1);
        scalar = Math.max(0, scalar);
        return (int) (LandingScore.MAX_ACCURACY_SCORE * scalar);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                RateLandingComponent.class,
                VelocityComponent.class,
                TransformComponent.class,
                FuelComponent.class,
                TimerComponent.class
        );
    }
}
