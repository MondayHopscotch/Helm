package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Monday on 10/26/2017.
 */

public class DrawUtils {
    private static int gap = 25;
    private static int lineLength = 50;
    private static int lineThickness = 10;
    private static int border = 25;

    public static void drawDottedRect(ShapeRenderer shaper, Rectangle rect) {

        int end;
        int segmentLength;

        Rectangle inner = new Rectangle(rect);
        inner.x += border;
        inner.y += border;
        inner.width -= 2 * border;
        inner.height -= 2 * border;

        for (int x = (int) inner.x; x < inner.x + inner.width; x += gap + lineLength) {
            segmentLength = lineLength;
            end = x + lineLength;

            if (end > inner.x + inner.width) {
                // cut our line short
                segmentLength = (int) (end - (inner.x + inner.width));
            } else if (end + gap >= inner.x + inner.width) {
                // draw out the rest of the line
                segmentLength += inner.x + inner.width - end;
            }


            shaper.rect(x, inner.y, segmentLength, lineThickness);
            shaper.rect(x, inner.y + inner.getHeight() - lineThickness, segmentLength, lineThickness);
        }

        for (int y = (int) inner.y; y < inner.y + inner.height; y += gap + lineLength) {
            segmentLength = lineLength;
            end = y + lineLength;
            if (end > inner.y + inner.height) {
                segmentLength = (int) (end - (inner.y + inner.height));
            }
            shaper.rect(inner.x, y, lineThickness, segmentLength);
            shaper.rect(inner.x + inner.getWidth() - lineThickness, y, lineThickness, segmentLength);
        }
    }
}
