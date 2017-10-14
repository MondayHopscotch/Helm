package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;
import com.bitdecay.helm.desktop.editor.MouseButton;

/**
 * Created by Monday on 1/15/2017.
 */
public class DeleteGravityObjectMouseMode extends BaseMouseMode {

    private Circle selectedObject;

    public DeleteGravityObjectMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseMoved(Vector2 point) {
        for (Circle well : builder.gravityWells) {
            if (point.cpy().sub(well.x, well.y).len() < well.radius + 10) {
                selectedObject = well;
                return;
            }
        }
        for (Circle field : builder.repulsionFields) {
            if (point.cpy().sub(field.x, field.y).len() < field.radius + 10) {
                selectedObject = field;
                return;
            }
        }

        selectedObject = null;
    }

    @Override
    public void mouseUpLogic(Vector2 point, MouseButton button) {
        if (selectedObject != null) {
            builder.removeGravityWell(selectedObject);
            builder.removeRepulsionField(selectedObject);
            selectedObject = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (selectedObject != null) {
            shaper.setColor(Color.CHARTREUSE);
            shaper.circle(selectedObject.x, selectedObject.y, selectedObject.radius);
            shaper.circle(selectedObject.x, selectedObject.y, selectedObject.radius + 10);
            shaper.circle(selectedObject.x, selectedObject.y, selectedObject.radius - 10);

        }
    }
}