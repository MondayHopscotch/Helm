package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.MouseButton;

/**
 * Created by Monday on 11/8/2015.
 */
public class NoOpMouseMode implements MouseMode {
    @Override
    public void mouseDown(Vector2 point, MouseButton button) {

    }

    @Override
    public void mouseDragged(Vector2 point) {

    }

    @Override
    public void mouseUp(Vector2 point, MouseButton button) {

    }

    @Override
    public void mouseMoved(Vector2 point) {

    }

    @Override
    public void render(ShapeRenderer shaper) {

    }
}
