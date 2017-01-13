package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/12/2017.
 */
public class DebugFocusPointSystem extends AbstractIteratingGameSystem {

    private ShapeRenderer renderer;

    public DebugFocusPointSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent tranform = entity.getComponent(TransformComponent.class);
        ProximityComponent proximity = entity.getComponent(ProximityComponent.class);

        renderer.setColor(Color.PINK);
        if (proximity.radius > 0) {
            renderer.circle(tranform.position.x, tranform.position.y, proximity.radius);
        } else {
            renderer.circle(tranform.position.x, tranform.position.y, 5);
            renderer.circle(tranform.position.x, tranform.position.y, 10);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                ProximityComponent.class
        );
    }
}
