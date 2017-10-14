package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;

/**
 * Created by Monday on 6/7/2017.
 */

public class WhiteoutPalette extends GamePalette {
    public WhiteoutPalette() {
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION, Color.RED);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER, Color.SLATE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.WORMHOLE, Color.DARK_GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BOOST, Color.FIREBRICK);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM, Color.FOREST);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND, Color.LIGHT_GRAY);
    }
}
