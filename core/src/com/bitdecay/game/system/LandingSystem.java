package com.bitdecay.game.system;

import com.badlogic.gdx.math.MathUtils;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.RateLandingComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.scoring.LandingScore;

/**
 * Created by Monday on 12/17/2016.
 */
public class LandingSystem extends AbstractIteratingGameSystem {

    public LandingSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        RateLandingComponent landing = entity.getComponent(RateLandingComponent.class);
        entity.removeComponent(RateLandingComponent.class);

        LandingScore score = new LandingScore();

        float radsAwayFromStraightUp = Math.abs(landing.landingAngle - Geom.ROTATION_UP);
        float maxAngleForScore = MathUtils.PI / 10;

        float angleScoreScalar;
        if (radsAwayFromStraightUp > maxAngleForScore) {
            angleScoreScalar = 0;
        } else {
            angleScoreScalar = (maxAngleForScore - radsAwayFromStraightUp) / maxAngleForScore;
        }

        float maxSpeedForScore = 4f;

        float speedScoreScalar;
        if (landing.landingVector.len() > maxSpeedForScore) {
            speedScoreScalar = 0;
        } else {
            speedScoreScalar = (maxSpeedForScore - landing.landingVector.len()) / maxSpeedForScore;
        }

        score.angleScore = (int) (LandingScore.MAX_ANGLE_SCORE * angleScoreScalar);
        score.speedScore = (int)(LandingScore.MAX_SPEED_SCORE * speedScoreScalar);

        pilot.finishLevel(score);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(RateLandingComponent.class);
    }
}
