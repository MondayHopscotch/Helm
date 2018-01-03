package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;
import com.bitdecay.helm.unlock.palette.GameColors;

/**
 * Created by Monday on 6/7/2017.
 */

public class WhiteoutPalette extends GamePalette {
    public WhiteoutPalette() {
        colors.put(GameColors.LEVEL_SEGMENT, Color.GRAY);
        colors.put(GameColors.EXPLOSION, Color.RED);
        colors.put(GameColors.SHIP_BODY, Color.GRAY);
        colors.put(GameColors.FUEL_METER, Color.SLATE);
        colors.put(GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(GameColors.GRAVITY_WELL, Color.GRAY);
        colors.put(GameColors.WORMHOLE, Color.DARK_GRAY);
        colors.put(GameColors.BOOST, Color.FIREBRICK);
        colors.put(GameColors.LANDING_PLATFORM, Color.FOREST);
        colors.put(GameColors.BACKGROUND, Color.LIGHT_GRAY);
    }
}
