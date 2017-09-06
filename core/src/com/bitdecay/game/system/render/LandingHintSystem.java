package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
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
    public static final float MAX_GUIDE_WIDTH = 100;
    public static final float LINE_SIZER = 0.7f;

    private ShapeRenderer renderer;

    private GameEntity player;
    private GameEntity platform;

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
            if (Helm.debug) {
//                System.out.println("Objects not found for Landing Hint System");
            }
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

        if (landable && withinNormal && withinTangent) {
            System.out.println("Distance from surface: " + normalDistance);

            float percentage = 1 - (normalDistance / HINT_RANGE);
            float guideHalfWidth = MAX_GUIDE_WIDTH * percentage  / 2 ;

            System.out.println("PERCENTAGE: " + percentage);
            System.out.println("HALF WIDTH:      " +  guideHalfWidth);

            // these are centered under the player
            Vector2 start = new Vector2(tangentDistance, heightOfPlatform);

            Vector2 leftStart = new Vector2(start).sub(guideHalfWidth, 0);
            Vector2 rightStart = new Vector2(start).add(guideHalfWidth, 0);

            leftStart.x = Math.min(widthOfPlatform, Math.max(0, leftStart.x));
            rightStart.x = Math.min(widthOfPlatform, Math.max(0, rightStart.x));

            Vector2 leftEnd = new Vector2(leftStart).add(0, normalDistance * LINE_SIZER);
            leftEnd.y = Math.max(leftEnd.y, leftStart.y);
            Vector2 rightEnd = new Vector2(rightStart).add(0, normalDistance * LINE_SIZER);
            rightEnd.y = Math.max(rightEnd.y, rightStart.y);


            leftStart.rotateRad(platformTransform.angle).add(platformTransform.position);
            rightStart.rotateRad(platformTransform.angle).add(platformTransform.position);
            leftEnd.rotateRad(platformTransform.angle).add(platformTransform.position);
            rightEnd.rotateRad(platformTransform.angle).add(platformTransform.position);


            renderer.setColor(pilot.getHelm().palette.get(GameColors.LANDING_PLATFORM));
            renderer.line(leftStart.x, leftStart.y, leftEnd.x, leftEnd.y);
            renderer.line(rightStart.x, rightStart.y, rightEnd.x, rightEnd.y);
        }

        player = null;
        platform = null;
    }
}
