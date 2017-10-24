package com.bitdecay.helm.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Monday on 10/22/2017.
 */

public class RotatingLabel extends Table {
    private boolean spinningRight = true;
    private float maxRotation = 10;
    private float rotation = maxRotation;
    private float spinSpeed = 0;
    private float spinAccel = .005f;
    private final Label innerLabel;

    public RotatingLabel(String labelText, float fontSize, Skin skin, ClickListener listener) {
        super();
        innerLabel = new Label(labelText, skin);
        innerLabel.setFontScale(fontSize);
        innerLabel.addListener(listener);

        setTransform(true);
        setFillParent(false);
        add(innerLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (spinningRight) {
            if (rotation > 0) {
                spinSpeed -= spinAccel;
            } else {
                spinSpeed += spinAccel;
            }
        } else {
            if (rotation > 0) {
                spinSpeed += spinAccel;
            } else {
                spinSpeed -= spinAccel;
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
