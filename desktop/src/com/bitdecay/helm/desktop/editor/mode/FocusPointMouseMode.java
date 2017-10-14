package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;

/**
 * Created by Monday on 1/13/2017.
 */
public class FocusPointMouseMode extends AbstractDrawCircleMouseMode {
    public FocusPointMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void objectDrawn(Vector2 startPoint, float radius) {
        builder.addFocusPoint(startPoint, radius);
    }
}
