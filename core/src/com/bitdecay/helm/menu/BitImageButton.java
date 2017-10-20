package com.bitdecay.helm.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class BitImageButton extends Button {
    private final Image image;
    private TextureRegionDrawable imgUp;
    private TextureRegionDrawable imgDown;
    private TextButton.TextButtonStyle style;

    public BitImageButton(TextureRegionDrawable imageUp, TextureRegionDrawable imgDown, float scale, Skin skin) {
        this(imageUp, imgDown, scale, skin.get(TextButton.TextButtonStyle.class));
        setSkin(skin);
    }

    public BitImageButton(TextureRegionDrawable imgUp, TextureRegionDrawable imgDown, float scale, TextButton.TextButtonStyle style) {
        super();
        this.imgUp = imgUp;
        this.imgDown = imgDown;
        this.style = style;
        setStyle(style);

        image = new Image();
        image.setScaling(Scaling.fit);
        image.setOrigin(Align.center);
        image.setAlign(Align.center);
        image.setDrawable(imgUp);
        add(image).size(imgUp.getRegion().getRegionHeight() * scale);
    }

    public TextButton.TextButtonStyle getStyle() {
        return style;
    }

    public void draw(Batch batch, float parentAlpha) {
        if (isDisabled() && style.disabledFontColor != null) {
            image.setColor(style.disabledFontColor);
        }

        super.draw(batch, parentAlpha);
    }

    public void setImage(TextureRegionDrawable image) {
        this.image.setDrawable(image);
    }

    public void down() {
        setImage(imgDown);
    }

    public void up() {
        setImage(imgUp);
    }
}
