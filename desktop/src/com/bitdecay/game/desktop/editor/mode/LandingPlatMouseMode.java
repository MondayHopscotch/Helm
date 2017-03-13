package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 1/2/2017.
 */
public class LandingPlatMouseMode extends com.bitdecay.game.desktop.editor.mode.BaseMouseMode {

    float platformThickness = 32;

    Rectangle workingRect = new Rectangle();
    float rotation = Geom.NO_ROTATION;

    public LandingPlatMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseDown(Vector2 point, MouseButton button) {
        startPoint = Geom.snap(point, 25);
    }

    @Override
    public void mouseDragged(Vector2 point) {
        super.mouseDragged(point);
        endPoint = Geom.snap(point, 25);
        calculateRectangle(startPoint, endPoint);
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        endPoint = Geom.snap(point, 25);
        if (startPoint.x != endPoint.x || startPoint.y != endPoint.y) {
            builder.setLandingPlatform(workingRect, rotation);
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(Color.GREEN);
            shaper.rect(workingRect.x, workingRect.y, 0, 0, workingRect.width, workingRect.height, 1, 1, rotation);

            Vector2 normal = endPoint.cpy().sub(startPoint).nor().rotate(90);
            Vector2 lineCenter = startPoint.cpy().add(endPoint).scl(.5f);

            shaper.line(lineCenter, lineCenter.cpy().add(normal.cpy().scl(50)));
        }
    }

    private void calculateRectangle(Vector2 startPoint, Vector2 endPoint) {
        Vector2 normal = endPoint.cpy().sub(startPoint).nor().rotate(90);
        Vector2 rectStartPoint = startPoint.cpy().sub(normal.cpy().scl(platformThickness));
        float lineLength = endPoint.cpy().sub(startPoint).len();

        workingRect.set(rectStartPoint.x, rectStartPoint.y, lineLength, platformThickness);
        rotation = normal.angle() - 90;
    }
}
