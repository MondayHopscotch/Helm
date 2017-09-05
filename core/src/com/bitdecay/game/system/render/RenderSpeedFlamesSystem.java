package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 9/4/2017.
 */

public class RenderSpeedFlamesSystem extends AbstractIteratingGameSystem {
    private final ShapeRenderer renderer;

    public RenderSpeedFlamesSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                VelocityComponent.class,
                BodyDefComponent.class
        );
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
        BodyDefComponent body = entity.getComponent(BodyDefComponent.class);

        float[] transformed = Geom.transformPoints(body.bodyPoints, transform);

        float biggestDot = Float.NEGATIVE_INFINITY;
        int winner = 0;
        for (int i = 1; i < transformed.length; i+=2) {
            float dot = velocityComponent.currentVelocity.dot(transformed[i - 1], transformed[i]);
            if (dot > biggestDot) {
                biggestDot = dot;
                winner = i / 2;
            }
        }

        renderer.setColor(Color.WHITE);

        renderer.circle(transformed[winner*2], transformed[winner*2+1], 5);

        Vector2 cpy = transform.position.cpy();
        cpy.add(velocityComponent.currentVelocity.cpy().scl(10));
        renderer.line(transform.position.x, transform.position.y, cpy.x, cpy.y);
    }
}
