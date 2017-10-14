package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.EditorScreen;
import com.bitdecay.helm.desktop.editor.LevelBuilder;
import com.bitdecay.helm.desktop.editor.MouseButton;
import com.bitdecay.helm.math.Geom;

/**
 * Created by Monday on 1/2/2017.
 */
public class LandingPlatMouseMode extends BaseMouseMode {

    float platformThickness = EditorScreen.cellSize * 2;

    Rectangle workingRect = new Rectangle();
    float rotation = Geom.NO_ROTATION;

    public LandingPlatMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseDown(Vector2 point, MouseButton button) {
        startPoint = Geom.snap(point, EditorScreen.cellSize);
    }

    @Override
    public void mouseDragged(Vector2 point) {
        super.mouseDragged(point);
        endPoint = Geom.snap(point, EditorScreen.cellSize);
        calculateRectangle(startPoint, endPoint);
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        endPoint = Geom.snap(point, EditorScreen.cellSize);
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
