package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;
import com.bitdecay.helm.desktop.editor.MouseButton;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.unlock.palette.PaletteList;

public class LineSegmentMouseMode extends BaseMouseMode {
    public LineSegmentMouseMode(LevelBuilder builder) {
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
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        endPoint = Geom.snap(point, 25);
        if ( !(startPoint.x == endPoint.x && startPoint.y == endPoint.y)) {
            builder.addLineSegment(startPoint, endPoint);
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(PaletteList.STANDARD.palette.get(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT));
            shaper.line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
    }
}
