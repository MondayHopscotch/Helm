package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.component.ProximityComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/12/2017.
 */
public class DebugFocusPointSystem extends AbstractIteratingGameSystem {

    private ShapeRenderer renderer;

    public DebugFocusPointSystem(com.bitdecay.helm.GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        ProximityComponent proximity = entity.getComponent(ProximityComponent.class);
        CameraFollowComponent follow = entity.getComponent(CameraFollowComponent.class);

        renderer.setColor(Color.PINK);
        if (proximity.radius > 0) {
            renderer.circle(transform.position.x, transform.position.y, proximity.radius);
        } else {
            renderer.circle(transform.position.x, transform.position.y, 5);
            renderer.circle(transform.position.x, transform.position.y, 10);
        }

        renderer.circle(transform.position.x + follow.offset.x, transform.position.y + follow.offset.y, 30);
        renderer.circle(transform.position.x + follow.offset.x, transform.position.y + follow.offset.y, 25);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                ProximityComponent.class,
                CameraFollowComponent.class

        );
    }
}
