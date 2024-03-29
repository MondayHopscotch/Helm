package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.ExplosionComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 2/16/2017.
 */

public class RenderExplosionSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderExplosionSystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        float angleStep = MathUtils.PI2 / explosion.spreadCount;
        Vector2 singleBoomPos = new Vector2(explosion.distance, 0);

        singleBoomPos.set(Geom.rotateSinglePoint(singleBoomPos, explosion.rotationalOffset));

        renderer.setColor(pilot.getHelm().palette.get(explosion.color));
        for (int i = 0; i < explosion.spreadCount; i++) {
            singleBoomPos.set(Geom.rotateSinglePoint(singleBoomPos, angleStep));
            renderer.circle(transform.position.x + singleBoomPos.x, transform.position.y + singleBoomPos.y, explosion.particleSize);
        }

        explosion.distance += explosion.speed;
        explosion.particleSize -= explosion.decay;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                ExplosionComponent.class,
                TransformComponent.class
        );
    }
}
