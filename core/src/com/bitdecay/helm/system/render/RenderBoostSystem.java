package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/17/2016.
 */
public class RenderBoostSystem extends AbstractIteratingGameSystem {
    ShapeRenderer renderer;

    float[] boostJetPoints = new float[]{-100, -25, -200, 0, -100, 25};

    public RenderBoostSystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        BoosterComponent boost = entity.getComponent(BoosterComponent.class);
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

        if (boost.engaged) {
            float[] translated = com.bitdecay.helm.math.Geom.rotatePoints(boostJetPoints, transform.angle);
            translated = com.bitdecay.helm.math.Geom.translatePoints(translated, transform.position);

            renderer.setColor(pilot.getHelm().palette.get(com.bitdecay.helm.unlock.palette.GameColors.BOOST));
            renderer.polygon(translated);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class);
    }
}
