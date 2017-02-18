package com.bitdecay.game.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/1/2017.
 */
public class RenderSteeringSystem extends AbstractIteratingGameSystem {
    private OrthographicCamera cam;
    private ShapeRenderer renderer;

    public RenderSteeringSystem(GamePilot pilot, OrthographicCamera cam, ShapeRenderer renderer) {
        super(pilot);
        this.cam = cam;
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
        renderer.setColor(Color.MAROON);
        if (steering.startPoint != null) {
            Vector3 startPointScreen = cam.unproject(new Vector3(workstationTransform(steering.startPoint), 0));
            renderer.circle(startPointScreen.x, startPointScreen.y, steering.sensitivity);
        }

        if (steering.startPoint != null && steering.endPoint != null) {
            Vector3 startPointScreen = cam.unproject(new Vector3(workstationTransform(steering.startPoint), 0));
            Vector3 endPointScreen = cam.unproject(new Vector3(workstationTransform(steering.endPoint), 0));
            renderer.line(startPointScreen.x, startPointScreen.y, endPointScreen.x, endPointScreen.y);
            renderer.circle(endPointScreen.x, endPointScreen.y, 10);
        }
    }

    private Vector2 workstationTransform(Vector2 in) {
        return new Vector2(in.x, Gdx.graphics.getHeight() - in.y);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(SteeringControlComponent.class);
    }
}
