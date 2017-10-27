package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Monday on 10/26/2017.
 */

public class DrawUtils {
    public static void drawDottedRect(ShapeRenderer shaper, Rectangle rect) {
        int spacing = 75;
        for (int x = (int) rect.x + spacing / 4; x < rect.x + rect.getWidth() - spacing / 2; x += spacing) {
            shaper.rect(x, rect.y + spacing / 4, spacing / 2, spacing / 4);
            shaper.rect(x, rect.y + rect.getHeight() - spacing / 2, spacing / 2, spacing / 4);
        }

        for (int y = (int) rect.y + spacing / 4; y < rect.y + rect.getHeight() - spacing / 2; y += spacing) {
            shaper.rect(rect.x + spacing / 4, y, spacing / 4, spacing / 2);
            shaper.rect(rect.x + rect.getWidth() - spacing / 2, y, spacing / 4, spacing / 2);
        }
    }
}
