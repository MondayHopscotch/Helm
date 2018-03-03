package com.bitdecay.helm.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.bitdecay.helm.persist.AssetUtils;
import com.bitdecay.helm.sound.MusicLibrary;
import com.bitdecay.helm.sound.SFXLibrary;

/**
 * Created by Monday on 3/3/2018.
 */

public class Loader {
    public static void loadAssets(AssetManager assets) {
        assets.clear();
        SFXLibrary.loadAllAsync(assets);
        MusicLibrary.loadAllAsync(assets);
        AssetUtils.loadSkinSync(assets);
        AssetUtils.loadImageAtlases(assets);
    }
}
