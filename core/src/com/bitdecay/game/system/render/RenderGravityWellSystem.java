package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.GravityProducerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 2/18/2017.
 */

public class RenderGravityWellSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderGravityWellSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        GravityProducerComponent gravity = entity.getComponent(GravityProducerComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        maybeResetInner(gravity);

        if (gravity.repels) {
            renderer.setColor(Color.TAN);
        } else {
            renderer.setColor(Color.PURPLE);
        }
        renderer.circle(transform.position.x, transform.position.y, gravity.size);

        float animSize;
        for (int i = 0; i < gravity.ringCount; i++) {
            animSize = gravity.inner - i * (gravity.size / gravity.ringCount);
            if (gravity.repels) {
                animSize = gravity.size - animSize;
            }
            renderer.circle(transform.position.x, transform.position.y, animSize);
        }
        gravity.inner -= gravity.animateSpeed * gravity.size * delta;
    }

    protected void maybeResetInner(GravityProducerComponent gravity) {
        if (gravity.inner <= gravity.size - (gravity.size / gravity.ringCount)) {
            gravity.inner = gravity.size;
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                GravityProducerComponent.class,
                TransformComponent.class
        );
    }
}
