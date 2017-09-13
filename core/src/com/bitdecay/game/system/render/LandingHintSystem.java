package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.component.collide.PlayerCollisionComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;
import com.bitdecay.game.system.collision.LandingSystem;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 9/4/2017.
 */

public class LandingHintSystem extends AbstractIteratingGameSystem {
    public static final float HINT_RANGE = 350;

    private ShapeRenderer renderer;

    private GameEntity player;
    private GameEntity platform;

    private static final float[] checkMark = new float[]{
            -8, -1,
            -6, 1,
            -3, -1,
            5, 6,
            7, 4,
            -3, -6,
            -8, -1
    };

    private static final float[] xMark = new float[]{
            -7, 5,
            -5, 7,
            0, 2,
            5, 7,
            7, 5,
            2, 0,
            7, -5,
            5, -7,
            0, -2,
            -5, -7,
            -7, -5,
            -2, 0,
            -7, 5
    };

    public LandingHintSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return isPlatform(entity) || isPlayer(entity);
    }

    private boolean isPlayer(GameEntity entity) {
        return entity.hasComponents(
                PlayerCollisionComponent.class,
                TransformComponent.class,
                VelocityComponent.class,
                BodyDefComponent.class
        );
    }

    private boolean isPlatform(GameEntity entity) {
        return entity.hasComponents(
                CollisionKindComponent.class,
                TransformComponent.class,
                BodyDefComponent.class
        );
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        if (isPlayer(entity)) {
            player = entity;
        } else if (isPlatform(entity)) {
            platform = entity;
        }
    }

    @Override
    public void after() {
        super.after();

        if (player == null || platform == null) {
            return;
        }

        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        BodyDefComponent playerBodyDef = player.getComponent(BodyDefComponent.class);
        TransformComponent platformTransform = platform.getComponent(TransformComponent.class);
        BodyDefComponent platformBodyDef = platform.getComponent(BodyDefComponent.class);

        float heightOfPlatform = Geom.getHeightOfPoints(platformBodyDef.bodyPoints);
        float widthOfPlatform = Geom.getWidthOfPoints(platformBodyDef.bodyPoints);

        Vector2 normalSearchVector = new Vector2(0, 1).rotateRad(platformTransform.angle);
        Vector2 tangentSearchVector = new Vector2(1, 0).rotateRad(platformTransform.angle);
        Vector2 relativePlayerPosition = new Vector2(playerTransform.position).sub(platformTransform.position);

        float[] platformGeom = Geom.transformPoints(platformBodyDef.bodyPoints, platformTransform);
        float[] playerGeom = Geom.transformPoints(playerBodyDef.bodyPoints, playerTransform);
        float minBodyReference = Geom.getMinAlongVector(playerGeom, normalSearchVector);
        float maxPlatformReference = Geom.getMaxAlongVector(platformGeom, normalSearchVector);

        float normalDistance = minBodyReference - maxPlatformReference;

        float tangentDistance = tangentSearchVector.dot(relativePlayerPosition);

        boolean withinNormal = normalDistance <= HINT_RANGE && normalDistance > 0;
        boolean withinTangent = tangentDistance > 0 && tangentDistance <= widthOfPlatform;

        float landingPlatformNormal = platformTransform.angle + Geom.ROTATION_UP;
        float radsAwayFromStraightUp = Math.abs(playerTransform.angle - landingPlatformNormal);

        boolean landable = radsAwayFromStraightUp <= LandingSystem.MAX_LANDING_ANGLE;

        Vector2 platCenter = new Vector2(widthOfPlatform / 2, heightOfPlatform / 2).rotateRad(platformTransform.angle).add(platformTransform.position);

        if (withinNormal && withinTangent) {
            if (landable) {
                renderer.setColor(pilot.getHelm().palette.get(GameColors.LANDING_PLATFORM));
                float[] checkPoints = Geom.rotatePoints(checkMark, platformTransform.angle);
                checkPoints = Geom.translatePoints(checkPoints, platCenter);
                renderer.polyline(checkPoints);
            } else {
                renderer.setColor(pilot.getHelm().palette.get(GameColors.LEVEL_SEGMENT));
                float[] xPoints = Geom.rotatePoints(xMark, platformTransform.angle);
                xPoints = Geom.translatePoints(xPoints, platCenter);
                renderer.polyline(xPoints);
            }
        }

        player = null;
        platform = null;
    }
}
