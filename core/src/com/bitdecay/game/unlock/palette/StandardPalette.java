package com.bitdecay.game.unlock.palette;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.game.system.render.GamePalette;

/**
 * Created by Monday on 6/6/2017.
 */

public class StandardPalette extends GamePalette {
    public StandardPalette() {
        colors.put(GameColors.LEVEL_SEGMENT, Color.RED);
        colors.put(GameColors.EXPLOSION, Color.ORANGE);
        colors.put(GameColors.SHIP_BODY, Color.WHITE);
        colors.put(GameColors.LAUNCH_SMOKE, Color.WHITE);
        colors.put(GameColors.FUEL_METER, Color.MAGENTA);
        colors.put(GameColors.REPULSION_FIELD, Color.TAN);
        colors.put(GameColors.GRAVITY_WELL, Color.PURPLE);
        colors.put(GameColors.WORMHOLE, Color.DARK_GRAY);
        colors.put(GameColors.BOOST, Color.FIREBRICK);
        colors.put(GameColors.LANDING_PLATFORM, Color.GREEN);
        colors.put(GameColors.BACKGROUND, Color.BLACK);
    }
}
