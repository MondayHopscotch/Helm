package com.bitdecay.game.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.game.system.render.GamePalette;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 6/19/2017.
 */

public class CharcoalPalette extends GamePalette {
    public CharcoalPalette() {
        colors.put(GameColors.LEVEL_SEGMENT, Color.GRAY);
        colors.put(GameColors.EXPLOSION, Color.LIGHT_GRAY);
        colors.put(GameColors.SHIP_BODY, Color.GRAY);
        colors.put(GameColors.LAUNCH_SMOKE, Color.GRAY);
        colors.put(GameColors.FUEL_METER, Color.LIGHT_GRAY);
        colors.put(GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(GameColors.GRAVITY_WELL, Color.GRAY);
        colors.put(GameColors.WORMHOLE, Color.GRAY);
        colors.put(GameColors.BOOST, Color.WHITE);
        colors.put(GameColors.LANDING_PLATFORM, Color.WHITE);
        colors.put(GameColors.BACKGROUND, Color.DARK_GRAY);
    }
}
