package com.bitdecay.helm.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Monday on 10/22/2017.
 */

public class RotatingLabel extends Table {
    private boolean spinningRight = true;
    private float maxRotation = 5;
    private float rotation = maxRotation;
    private float spinSpeed = 0;
    private float spinAccel = .3f;
    public final Label innerLabel;

    public RotatingLabel(String labelText, float fontSize, Skin skin) {
        this(labelText, fontSize, skin, null);
    }

    public RotatingLabel(String labelText, float fontSize, Skin skin, ClickListener listener) {
        this(labelText, fontSize, skin, listener, true);
    }

    public RotatingLabel(String labelText, float fontSize, Skin skin, ClickListener listener, boolean fill) {
        super();
        innerLabel = new Label(labelText, skin);
        innerLabel.setOrigin(Align.center);
        innerLabel.setAlignment(Align.center);
        innerLabel.setFontScale(fontSize);
        if (listener != null) {
            addListener(listener);
        }

        setTransform(true);
        setFillParent(false);
        Cell<Label> labelCell = add(innerLabel);
        if (fill) {
            labelCell.expandX().fillX();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (spinningRight) {
            if (rotation > 0) {
                spinSpeed -= spinAccel * delta;
            } else {
                spinSpeed += spinAccel * delta;
            }
        } else {
            if (rotation > 0) {
                spinSpeed += spinAccel * delta;
            } else {
                spinSpeed -= spinAccel * delta;
            }
        }

        rotation += spinSpeed;

        if (rotation > maxRotation || rotation < -maxRotation) {
            spinningRight = !spinningRight;
            spinSpeed = 0;
            rotation = Math.max(rotation, -maxRotation);
            rotation = Math.min(rotation, maxRotation);
        }

        setOrigin(getWidth() / 2, getHeight() / 2);
        setRotation(rotation);
    }
}
