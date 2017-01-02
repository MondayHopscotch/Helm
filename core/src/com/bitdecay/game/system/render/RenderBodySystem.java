package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class RenderBodySystem extends AbstractIteratingGameSystem {

    private ShapeRenderer renderer;

    public RenderBodySystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BodyDefComponent body = entity.getComponent(BodyDefComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        RenderColorComponent color = entity.getComponent(RenderColorComponent.class);

        float[] rotated = Geom.rotatePoints(body.bodyPoints, transform.angle);
        for (int i = 0; i < rotated.length; i += 2) {
            rotated[i] += transform.position.x;
            rotated[i+1] += transform.position.y;
        }

        renderer.setColor(color.color);
        if (rotated.length > Geom.DATA_POINTS_FOR_LINE) {
            renderer.polygon(rotated);
        } else {
            renderer.polyline(rotated);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BodyDefComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(RenderColorComponent.class);
    }
}
