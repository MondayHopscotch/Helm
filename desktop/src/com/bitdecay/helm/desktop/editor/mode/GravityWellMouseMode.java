package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 2/18/2017.
 */

public class GravityWellMouseMode extends AbstractDrawCircleMouseMode {
    public GravityWellMouseMode(com.bitdecay.helm.desktop.editor.LevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void objectDrawn(Vector2 startPoint, float radius) {
        builder.addGravityWell(startPoint, radius);
    }
}
