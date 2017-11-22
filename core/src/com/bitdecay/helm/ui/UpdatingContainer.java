package com.bitdecay.helm.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

/**
 * Created by Monday on 10/31/2017.
 */

public class UpdatingContainer extends Container {
    public Runnable updater;
    public Runnable action;

    public UpdatingContainer(Actor wrap) {
        super(wrap);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (updater != null) {
            updater.run();
        }
    }
}
