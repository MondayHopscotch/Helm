package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.LevelBuilder;
import com.bitdecay.game.desktop.editor.MouseButton;

/**
 * Created by Monday on 1/15/2017.
 */
public class DeleteFocusMouseMode extends BaseMouseMode {

    private Circle selectedFocus;

    public DeleteFocusMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseMoved(Vector2 point) {
        for (Circle focusPoint : builder.focusPoints) {
            if (point.sub(focusPoint.x, focusPoint.y).len() < focusPoint.radius) {
                selectedFocus = focusPoint;
                return;
            }
        }

        selectedFocus = null;
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        if (selectedFocus != null) {
            builder.removeFocusPoint(selectedFocus);
            selectedFocus = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (selectedFocus != null) {
            shaper.setColor(Color.CHARTREUSE);
            shaper.circle(selectedFocus.x, selectedFocus.y, selectedFocus.radius);
            shaper.circle(selectedFocus.x, selectedFocus.y, selectedFocus.radius + 10);
            shaper.circle(selectedFocus.x, selectedFocus.y, selectedFocus.radius - 10);

        }
    }
}