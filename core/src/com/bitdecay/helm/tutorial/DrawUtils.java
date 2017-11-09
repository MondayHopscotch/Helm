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

        segmentLength = lineLength;

        int x = (int) inner.x;
        int y = (int) inner.y;
        for (; x < inner.x + inner.width; x += lineLength + gap) {
            end = x + segmentLength;
            if (end > inner.x + inner.width) {
                segmentLength -= (int) (end - (inner.x + inner.width));
            }
            shaper.rect(x, y, segmentLength, lineThickness);
        }
        segmentLength = lineLength;
        x = (int) (inner.x + inner.width);

        for (; y < inner.y + inner.height; y += lineLength + gap) {
            end = y + segmentLength;
            if (end > inner.y + inner.height) {
                segmentLength -= (int) (end - (inner.y + inner.height));
            }
            shaper.rect(x, y, -lineThickness, segmentLength);
        }
        segmentLength = lineLength;
        y = (int) (inner.y + inner.height);

        for (; x > inner.x; x -= lineLength + gap) {
            end = x - segmentLength;
            if (end < inner.x) {
                segmentLength -= (int) (inner.x - end);
            }
            shaper.rect(x, y, -segmentLength, -lineThickness);
        }
        segmentLength = lineLength;
        x = (int) inner.x;

        for (; y > inner.y; y -= lineLength + gap) {
            end = y - segmentLength;
            if (end < inner.y) {
                segmentLength -= (int) (inner.y - end);
            }
            shaper.rect(x, y, lineThickness, -segmentLength);
        }
    }
}
