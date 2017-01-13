package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 1/2/2017.
 */
public class StartPointMouseMode extends com.bitdecay.game.desktop.editor.mode.BaseMouseMode {

    public StartPointMouseMode(LevelBuilder builder) {
        super(builder);
    }


    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        endPoint = Geom.snap(point, 25);
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
            builder.setStartPoint(endPoint);
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (currentLocation != null) {
            shaper.setColor(Color.WHITE);
            shaper.circle(currentLocation.x, currentLocation.y, 100);
        }
    }
}
