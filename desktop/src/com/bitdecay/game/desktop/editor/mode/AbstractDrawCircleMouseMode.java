package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 2/18/2017.
 */

public abstract class AbstractDrawCircleMouseMode extends BaseMouseMode {
    public AbstractDrawCircleMouseMode(LevelBuilder builder) {
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
            float radius = getRadius();
            if (radius > 10) {
                objectDrawn(startPoint, getRadius());
            }
        }
    }

    protected abstract void objectDrawn(Vector2 startPoint, float radius);

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
