package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.WormholeComponent;
import com.bitdecay.game.component.collide.CollisionGeometryComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;
import com.bitdecay.game.system.util.SecondLocationComponent;

/**
 * Created by Monday on 3/19/2017.
 */

public class RenderWormholeSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderWormholeSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        SecondLocationComponent exitLocationComponent = entity.getComponent(SecondLocationComponent.class);
        WormholeComponent wormhole = entity.getComponent(WormholeComponent.class);

        CollisionGeometryComponent collisionGeom = entity.getComponent(CollisionGeometryComponent.class);

        maybeResetSizes(wormhole);

        float collisionRadius = collisionGeom.originalGeom[0];

        renderer.setColor(Color.TAN);

        drawWormhole(transform.position, wormhole, wormhole.inSize, wormhole.inner, true);
        drawWormhole(exitLocationComponent.position, wormhole, wormhole.outSize, wormhole.inner * (wormhole.outSize / wormhole.inSize), false);

        float change = wormhole.animateSpeed * wormhole.inSize * delta;
        wormhole.inner -= change;
        wormhole.angle += change;

    }

    protected void drawWormhole(Vector2 position, WormholeComponent wormhole, float radius, float workingRadius, boolean in) {
        float spinMode = (in ? 1 : -1);

        float size = radius;
        float half = size / 2;

        renderer.rect(position.x - half, position.y - half, half, half, size, size, 1, 1, spinMode * wormhole.angle);
        renderer.rect(position.x - half, position.y - half, half, half, size, size, 1, 1, spinMode * wormhole.angle + 30);
        renderer.rect(position.x - half, position.y - half, half, half, size, size, 1, 1, spinMode * wormhole.angle + 60);

        float renderAngle;
        for (int i = 0; i < wormhole.drawCount; i++) {
            size = workingRadius - i * (radius / wormhole.drawCount);
            if (!in) {
                size = radius - size;
            }
            half = size / 2;
            renderAngle = (wormhole.angle + wormhole.twistFactor * (radius - size));
            renderer.rect(position.x - half, position.y - half, half, half, size, size, 1, 1, renderAngle * spinMode);
        }
    }

    protected void maybeResetSizes(WormholeComponent wormhole) {
        if (wormhole.inner <= wormhole.inSize - (wormhole.inSize / wormhole.drawCount)) {
            wormhole.inner = wormhole.inSize;
        }
        if (wormhole.angle > 360) {
            wormhole.angle -= 360;
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                CollisionGeometryComponent.class,
                SecondLocationComponent.class,
                WormholeComponent.class
        );
    }
}
