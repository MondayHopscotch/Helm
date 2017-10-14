package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 2/18/2017.
 */

public class RenderGravityWellSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderGravityWellSystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.GravityProducerComponent gravity = entity.getComponent(com.bitdecay.helm.component.GravityProducerComponent.class);
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

        maybeResetInner(gravity);

        if (gravity.repels) {
            renderer.setColor(pilot.getHelm().palette.get(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD));
        } else {
            renderer.setColor(pilot.getHelm().palette.get(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL));
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

    protected void maybeResetInner(com.bitdecay.helm.component.GravityProducerComponent gravity) {
        if (gravity.inner <= gravity.size - (gravity.size / gravity.ringCount)) {
            gravity.inner = gravity.size;
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.GravityProducerComponent.class,
                com.bitdecay.helm.component.TransformComponent.class
        );
    }
}
