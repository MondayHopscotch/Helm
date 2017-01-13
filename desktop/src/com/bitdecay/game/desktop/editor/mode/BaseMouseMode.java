package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;

public abstract class BaseMouseMode implements MouseMode {

    public Vector2 startPoint;
    public Vector2 endPoint;
    public Vector2 currentLocation;

    protected LevelBuilder builder;

    public BaseMouseMode(LevelBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void mouseDown(Vector2 point, MouseButton button) {
        startPoint = point;
    }

    @Override
    public void mouseDragged(Vector2 point) {
        currentLocation = point;
        endPoint = point;
    }

    @Override
    public void mouseUp(Vector2 point, MouseButton button) {
        if (startPoint == null) {
            return;
        }
        endPoint = point;
        mouseUpLogic(point, button);
        startPoint = null;
        endPoint = null;
    }

    protected abstract void mouseUpLogic(Vector2 point, MouseButton button);

    @Override
    public void mouseMoved(Vector2 point) {
        currentLocation = point;
    }

    @Override
    public void render(ShapeRenderer shaper) {
    }
}
