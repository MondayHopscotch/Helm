package com.bitdecay.helm.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Monday on 6/4/2017.
 */

public class IconUtils {
    private static TextureRegion perfectIcon;
    private static TextureRegion checkMarkIcon;


    public static void init(com.bitdecay.helm.Helm helm) {
        perfectIcon = helm.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("perfect_icon");
        checkMarkIcon = helm.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("checkmark_icon");
    }

    public static Image getPerfectIcon() {
        return new Image(perfectIcon);
    }

    public static Image getCheckMarkIcon() {
        return new Image(checkMarkIcon);
    }

    public static Image getEmptyIcon() {
        return new Image();
    }
}
