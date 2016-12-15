package com.bitdecay.game.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.RotationComponent;
import com.bitdecay.game.component.ShipBodyComponent;
import com.bitdecay.game.math.Geom;

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
        RotationComponent rotation = entity.getComponent(RotationComponent.class);
        PositionComponent position = entity.getComponent(PositionComponent.class);

        float[] rotated = Geom.rotatePoints(body.bodyPoints, rotation.angle);
        for (int i = 0; i < rotated.length; i += 2) {
            rotated[i] += position.position.x;
            rotated[i+1] += position.position.y;
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        if (rotated.length > Geom.DATA_POINTS_FOR_LINE) {
            renderer.polygon(rotated);
        } else {
            renderer.polyline(rotated);
        }
        renderer.circle(position.position.x,position.position.y, 50);
        renderer.end();
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ShipBodyComponent.class) &&
                entity.hasComponent(PositionComponent.class) &&
                entity.hasComponent(RotationComponent.class);
    }
}
