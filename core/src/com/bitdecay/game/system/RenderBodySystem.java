package com.bitdecay.game.system;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.ShipBodyComponent;

/**
 * Created by Monday on 12/12/2016.
 */
public class RenderBodySystem extends AbstractIteratingGameSystem {

    private ShapeRenderer renderer;

    public RenderBodySystem(ShapeRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ShipBodyComponent body = entity.getComponent(ShipBodyComponent.class);
        PositionComponent position = entity.getComponent(PositionComponent.class);

        float[] asFloats = new float[body.bodyPoints.length*2];
        for (int i = 0; i < body.bodyPoints.length; i++) {
            asFloats[i * 2] = body.bodyPoints[i].x + position.position.x;
            asFloats[i * 2 + 1] = body.bodyPoints[i].y + position.position.y;
        }
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.polygon(asFloats);
        renderer.end();
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ShipBodyComponent.class) &&
                entity.hasComponent(PositionComponent.class);
    }
}
