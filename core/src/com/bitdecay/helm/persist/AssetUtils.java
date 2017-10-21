package com.bitdecay.helm.persist;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Monday on 10/21/2017.
 */

public class AssetUtils {

    public static void loadImageAtlases(AssetManager assets) {
        assets.load("img/icons.atlas", TextureAtlas.class);
    }

    public static void loadSkinSync(AssetManager assets) {
        assets.load("skin/ui.atlas", TextureAtlas.class);
        assets.finishLoadingAsset("skin/ui.atlas");
    }
}
