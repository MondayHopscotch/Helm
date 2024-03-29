package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.RenderColorComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class RenderBodySystem extends AbstractIteratingGameSystem {

    private ShapeRenderer renderer;

    public RenderBodySystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        BodyDefComponent body = entity.getComponent(BodyDefComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        RenderColorComponent color = entity.getComponent(RenderColorComponent.class);

        float[] transformed = Geom.transformPoints(body.bodyPoints, transform);

        renderer.setColor(pilot.getHelm().palette.get(color.color));
        if (transformed.length > com.bitdecay.helm.math.Geom.DATA_POINTS_FOR_LINE) {
            renderer.polygon(transformed);
        } else {
            renderer.polyline(transformed);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.BodyDefComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class) &&
                entity.hasComponent(RenderColorComponent.class);
    }
}
