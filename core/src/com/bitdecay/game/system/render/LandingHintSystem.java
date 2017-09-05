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
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 9/4/2017.
 */

public class LandingHintSystem extends AbstractIteratingGameSystem {
    public static final float HINT_RANGE = 350;
    public static final float MAX_GUIDE_WIDTH = 200;


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

        // get platform normal
        // our relative 'up' from the perspective of the landing platform
        float landingPlatformNormal = platformTransform.angle + Geom.ROTATION_UP;
        float landingPlatformTangent = platformTransform.angle;
        Vector2 normalSearchVector = new Vector2(1, 0).rotateRad(landingPlatformNormal);
        Vector2 tangentSearchVector = new Vector2(1, 0).rotateRad(landingPlatformTangent);
        Vector2 relativePlayerPosition = new Vector2(playerTransform.position).sub(platformTransform.position);

//        float[] transPlayerPoints = Geom.transformPoints(playerBodyDef.bodyPoints, playerTransform);
//        float playerMinY = Geom.getMinY(transPlayerPoints);// this is our lowest point on the player ship

        // check if player transform is within range of platform

        float normalDistance = normalSearchVector.dot(relativePlayerPosition);
        float tangentDistance = tangentSearchVector.dot(relativePlayerPosition);

        boolean withinNormal = normalDistance <= HINT_RANGE;
        boolean withinTangent = tangentDistance > 0 && tangentDistance <= widthOfPlatform;
        if (withinNormal && withinTangent) {
            System.out.println("Distance from surface: " + normalDistance);

            float trueDistanceToPlatformTop = normalDistance - heightOfPlatform;
            float trueHintRange = HINT_RANGE - heightOfPlatform;

            float percentage = 1 - (trueDistanceToPlatformTop / trueHintRange);
            float guideHalfWidth = MAX_GUIDE_WIDTH * percentage  / 2 ;

            System.out.println("PERCENTAGE: " + percentage);
            System.out.println("HALF WIDTH:      " +  guideHalfWidth);

            // these are centered
            Vector2 start = new Vector2(tangentDistance, heightOfPlatform).add(platformTransform.position);
            Vector2 end = new Vector2(tangentDistance, normalDistance).add(platformTransform.position);

            Vector2 leftStart = new Vector2(start).sub(guideHalfWidth, 0);
            Vector2 rightStart = new Vector2(start).add(guideHalfWidth, 0);

            Vector2 leftEnd = new Vector2(end).sub(guideHalfWidth, 0);
            leftEnd.y = Math.max(leftEnd.y, leftStart.y);
            Vector2 rightEnd = new Vector2(end).add(guideHalfWidth, 0);
            rightEnd.y = Math.max(rightEnd.y, rightStart.y);


            leftStart.rotateRad(platformTransform.angle);
            rightStart.rotateRad(platformTransform.angle);
            leftEnd.rotateRad(platformTransform.angle);
            rightEnd.rotateRad(platformTransform.angle);


            renderer.setColor(pilot.getHelm().palette.get(GameColors.LANDING_PLATFORM));
            renderer.line(leftStart.x, leftStart.y, leftEnd.x, leftEnd.y);
            renderer.line(rightStart.x, rightStart.y, rightEnd.x, rightEnd.y);
        }

        player = null;
        platform = null;
    }
}
