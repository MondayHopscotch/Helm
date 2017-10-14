package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 9/4/2017.
 */

public class LandingHintSystem extends AbstractIteratingGameSystem {
    public static final float HINT_RANGE = 350;

    private ShapeRenderer renderer;

    private com.bitdecay.helm.GameEntity player = null;
    private com.bitdecay.helm.GameEntity platform = null;

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

    public LandingHintSystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return isPlatform(entity) || isPlayer(entity);
    }

    private boolean isPlayer(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.collide.PlayerCollisionComponent.class,
                TransformComponent.class,
                com.bitdecay.helm.component.BodyDefComponent.class
        );
    }

    private boolean isPlatform(com.bitdecay.helm.GameEntity entity) {
        if (entity.hasComponents(
                CollisionKindComponent.class,
                TransformComponent.class,
                com.bitdecay.helm.component.BodyDefComponent.class
        )) {
            return CollisionKind.LANDING_PLATFORM.equals(entity.getComponent(CollisionKindComponent.class).kind);
        } else {
            return false;
        }
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        if (isPlayer(entity) && entity != player) {
            player = entity;
        } else if (isPlatform(entity) && entity != platform) {
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
        com.bitdecay.helm.component.BodyDefComponent playerBodyDef = player.getComponent(com.bitdecay.helm.component.BodyDefComponent.class);
        TransformComponent platformTransform = platform.getComponent(TransformComponent.class);
        com.bitdecay.helm.component.BodyDefComponent platformBodyDef = platform.getComponent(com.bitdecay.helm.component.BodyDefComponent.class);

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

        // we want to keep rendering once the player lands, so give them a little slack on the min
        boolean withinNormal = normalDistance <= HINT_RANGE && normalDistance > (-heightOfPlatform / 2);
        boolean withinTangent = tangentDistance > 0 && tangentDistance <= widthOfPlatform;

        float landingPlatformNormal = platformTransform.angle + Geom.ROTATION_UP;
        float radsAwayFromStraightUp = Math.abs(playerTransform.angle - landingPlatformNormal);

        boolean landable = radsAwayFromStraightUp <= com.bitdecay.helm.system.collision.LandingSystem.MAX_LANDING_ANGLE;

        Vector2 platCenter = new Vector2(widthOfPlatform / 2, heightOfPlatform / 2).rotateRad(platformTransform.angle).add(platformTransform.position);

        if (withinNormal && withinTangent) {
            if (landable) {
                renderer.setColor(pilot.getHelm().palette.get(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM));
                float[] checkPoints = Geom.rotatePoints(checkMark, platformTransform.angle);
                checkPoints = Geom.translatePoints(checkPoints, platCenter);
                renderer.polyline(checkPoints);
            } else {
                renderer.setColor(pilot.getHelm().palette.get(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT));
                float[] xPoints = Geom.rotatePoints(xMark, platformTransform.angle);
                xPoints = Geom.translatePoints(xPoints, platCenter);
                renderer.polyline(xPoints);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        player = null;
        platform = null;
    }
}
