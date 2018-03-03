package com.bitdecay.helm.persist;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Monday on 10/21/2017.
 */

public class AssetUtils {

    private static String skinFileName = "skin/ui.atlas";
    private static String iconFileName = "img/icons.atlas";

    public static void loadImageAtlases(AssetManager assets) {
        if (!assets.isLoaded(iconFileName, TextureAtlas.class)) {
            assets.load(iconFileName, TextureAtlas.class);
            assets.finishLoadingAsset(iconFileName);
        }
    }

    public static void loadSkinSync(AssetManager assets) {
        if (!assets.isLoaded(skinFileName, TextureAtlas.class)) {
            assets.load(skinFileName, TextureAtlas.class);
            assets.finishLoadingAsset(skinFileName);
        }
    }
}
