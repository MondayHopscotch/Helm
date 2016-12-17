package com.bitdecay.game.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;

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
            float[] rotated = Geom.rotatePoints(boostJetPoints, transform.angle);
            for (int i = 0; i < rotated.length; i += 2) {
                rotated[i] += transform.position.x;
                rotated[i+1] += transform.position.y;
            }

            renderer.setColor(Color.FIREBRICK);
            renderer.polygon(rotated);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(TransformComponent.class);
    }
}
