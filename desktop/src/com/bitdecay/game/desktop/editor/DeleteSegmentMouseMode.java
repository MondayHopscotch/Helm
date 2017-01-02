package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.world.LineSegment;

public class DeleteSegmentMouseMode extends BaseMouseMode {
    private LineSegment selectedSegment;

    public DeleteSegmentMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseMoved(Vector2 point) {
        for (LineSegment line : builder.lines) {
            boolean xInRange = Math.abs(point.x - line.startPoint.x) + Math.abs(line.endPoint.x - point.x) == Math.abs(line.endPoint.x - line.startPoint.x);
            boolean yInRange = Math.abs(point.y - line.startPoint.y) + Math.abs(line.endPoint.y - point.y) == Math.abs(line.endPoint.y - line.startPoint.y);
            if (xInRange && yInRange) {
                selectedSegment = line;
                return;
            }
        }

        selectedSegment = null;
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        if (selectedSegment != null) {
            builder.removeLineSegment(selectedSegment);
            selectedSegment = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (selectedSegment != null) {
            shaper.setColor(Color.GRAY);
            shaper.rect(selectedSegment.startPoint.x, selectedSegment.startPoint.y,
                    selectedSegment.endPoint.x - selectedSegment.startPoint.x, selectedSegment.endPoint.y - selectedSegment.startPoint.y);
        }
    }
}
