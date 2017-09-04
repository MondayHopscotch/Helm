package com.bitdecay.game.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.game.system.render.GamePalette;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 6/6/2017.
 */

public class GuacamolePalette extends GamePalette {
    public GuacamolePalette() {
        colors.put(GameColors.LEVEL_SEGMENT, Color.GREEN);
        colors.put(GameColors.EXPLOSION, Color.ORANGE);
        colors.put(GameColors.SHIP_BODY, Color.FOREST);
        colors.put(GameColors.FUEL_METER, Color.YELLOW);
        colors.put(GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(GameColors.GRAVITY_WELL, Color.RED);
        colors.put(GameColors.WORMHOLE, Color.DARK_GRAY);
        colors.put(GameColors.BOOST, Color.GOLD);
        colors.put(GameColors.LANDING_PLATFORM, Color.OLIVE);
        colors.put(GameColors.BACKGROUND, Color.BROWN.mul(0.25f));
    }
}