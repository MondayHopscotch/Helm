package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ExplosionComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;
import com.bitdecay.game.world.GameColors;

/**
 * Created by Monday on 2/16/2017.
 */

public class RenderExplosionSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderExplosionSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        float angleStep = MathUtils.PI2 / explosion.spreadCount;
        Vector2 singleBoomPos = new Vector2(explosion.distance, 0);

        renderer.setColor(GameColors.EXPLOSION);
        for (int i = 0; i < explosion.spreadCount; i++) {
            singleBoomPos.set(Geom.rotateSinglePoint(singleBoomPos, angleStep));
            renderer.circle(transform.position.x + singleBoomPos.x, transform.position.y + singleBoomPos.y, explosion.particleSize);
        }

        explosion.distance += explosion.speed;
        explosion.particleSize -= explosion.decay;

        if (explosion.particleSize <= 0) {
            pilot.requestRestartLevel();
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                ExplosionComponent.class,
                TransformComponent.class
        );
    }
}
