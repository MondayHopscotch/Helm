package com.bitdecay.helm.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.helm.Helm;

/**
 * Created by Monday on 2/20/2018.
 */

public class TextureCache {
    public static TextureRegion getIcon(Helm helm, String name) {
        return helm.assets.get("img/icons.atlas", TextureAtlas.class).findRegion(name);
    }
}
