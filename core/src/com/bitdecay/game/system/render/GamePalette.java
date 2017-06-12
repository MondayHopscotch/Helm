package com.bitdecay.game.system.render;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.game.unlock.palette.GameColors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 6/5/2017.
 */

public class GamePalette {
    public Map<GameColors, Color> colors = new HashMap<>();

    public GamePalette() {
        // Here for JSON
    }

    public Color get(GameColors selector) {
        if (colors.containsKey(selector)) {
            return colors.get(selector);
        } else {
            return Color.WHITE;
        }
    }
}
