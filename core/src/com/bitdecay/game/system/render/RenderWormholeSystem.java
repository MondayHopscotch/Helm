package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.TransformComponent;
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
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        SecondLocationComponent exitLocationComponent = entity.getComponent(SecondLocationComponent.class);

        CollisionGeometryComponent collisionGeom = entity.getComponent(CollisionGeometryComponent.class);
        float collisionRadius = collisionGeom.originalGeom[0];

        renderer.setColor(Color.TAN);

        renderer.circle(transformComponent.position.x, transformComponent.position.y, collisionRadius);
        renderer.circle(transformComponent.position.x, transformComponent.position.y, collisionRadius+50);
        renderer.circle(exitLocationComponent.position.x, exitLocationComponent.position.y, collisionRadius);
        renderer.circle(exitLocationComponent.position.x, exitLocationComponent.position.y, collisionRadius+50);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                CollisionGeometryComponent.class,
                SecondLocationComponent.class
        );
    }
}
