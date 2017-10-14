package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;
import com.bitdecay.helm.desktop.editor.MouseButton;
import com.bitdecay.helm.math.Geom;

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
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    Vector2 midPoint = new Vector2(startPoint).add(endPoint).scl(.5f);
                    radius = startPoint.cpy().sub(endPoint).len() / 2;
                    objectDrawn(midPoint, radius);
                } else {
                    objectDrawn(startPoint, getRadius());
                }
            }
        }
    }

    protected abstract void objectDrawn(Vector2 startPoint, float radius);

    @Override
    public void render(ShapeRenderer shaper) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(Color.PINK);
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                Vector2 midPoint = new Vector2(startPoint).add(endPoint).scl(.5f);
                float radius = startPoint.cpy().sub(endPoint).len() / 2;
                shaper.circle(midPoint.x, midPoint.y, radius);
            } else {
                shaper.circle(startPoint.x, startPoint.y, getRadius());
            }
        }
    }

    private float getRadius() {
        return startPoint.cpy().sub(endPoint).len();
    }
}
