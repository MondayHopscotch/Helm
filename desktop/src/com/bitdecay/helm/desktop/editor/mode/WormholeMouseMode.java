package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;

/**
 * Created by Monday on 3/21/2017.
 */

public class WormholeMouseMode extends AbstractDrawCircleMouseMode {
    Circle entrance;
    Circle exit;

    public WormholeMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void objectDrawn(Vector2 startPoint, float radius) {
        if (entrance == null) {
            entrance = new Circle(startPoint, radius);
        } else {
            exit = new Circle(startPoint, radius);
            builder.addWormhole(entrance, exit);
            entrance = null;
            exit = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        shaper.setColor(Color.TAN);
        super.render(shaper);
        if (entrance != null) {
            shaper.circle(entrance.x, entrance.y, entrance.radius);
            Vector2 lineEnd = currentLocation.cpy();
            if (startPoint != null) {
                lineEnd.set(startPoint);
            }
            shaper.line(entrance.x, entrance.y, lineEnd.x, lineEnd.y);
        }
    }
}
