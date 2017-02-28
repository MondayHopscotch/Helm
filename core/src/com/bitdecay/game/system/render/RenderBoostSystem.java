package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;
import com.bitdecay.game.world.GameColors;

/**
 * Created by Monday on 12/17/2016.
 */
public class RenderBoostSystem extends AbstractIteratingGameSystem {
    ShapeRenderer renderer;

    float[] boostJetPoints = new float[]{-100, -25, -200, 0, -100, 25};

    public RenderBoostSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoosterComponent boost = entity.getComponent(BoosterComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        if (boost.engaged) {
            float[] translated = Geom.rotatePoints(boostJetPoints, transform.angle);
            translated = Geom.translatePoints(translated, transform.position);

            renderer.setColor(GameColors.BOOST);
            renderer.polygon(translated);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(TransformComponent.class);
    }
}
