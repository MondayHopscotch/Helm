package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 1/13/2017.
 */
public class FocusPointMouseMode extends com.bitdecay.game.desktop.editor.mode.BaseMouseMode {
    public FocusPointMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseDown(Vector2 point, MouseButton button) {
        startPoint = Geom.snap(point, 25);
    }

    @Override
    public void mouseDragged(Vector2 point) {
        super.mouseDragged(point);
        endPoint = point;
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        endPoint = point;
        if (!(startPoint.x == endPoint.x && startPoint.y == endPoint.y)) {
            builder.addFocusPoint(startPoint, getRadius());
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(Color.PINK);
            shaper.circle(startPoint.x, startPoint.y, getRadius());
        }
    }

    private float getRadius() {
        return startPoint.cpy().sub(endPoint).len();
    }
}
