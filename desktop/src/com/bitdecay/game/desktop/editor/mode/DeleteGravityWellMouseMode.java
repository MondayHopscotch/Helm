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
public class DeleteGravityWellMouseMode extends BaseMouseMode {

    private Circle selectedWell;

    public DeleteGravityWellMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseMoved(Vector2 point) {
        for (Circle well : builder.gravityWells) {
            if (point.sub(well.x, well.y).len() < well.radius + 10) {
                selectedWell = well;
                return;
            }
        }

        selectedWell = null;
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        if (selectedWell != null) {
            builder.removeGravityWell(selectedWell);
            selectedWell = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (selectedWell != null) {
            shaper.setColor(Color.CHARTREUSE);
            shaper.circle(selectedWell.x, selectedWell.y, selectedWell.radius);
            shaper.circle(selectedWell.x, selectedWell.y, selectedWell.radius + 10);
            shaper.circle(selectedWell.x, selectedWell.y, selectedWell.radius - 10);

        }
    }
}