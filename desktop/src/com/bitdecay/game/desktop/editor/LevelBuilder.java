package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

import java.util.ArrayList;

/**
 * Created by Monday on 1/2/2017.
 */
public class LevelBuilder {

    public ArrayList<LineSegment> lines = new ArrayList<>();
    public Rectangle landingPlat;
    public Vector2 startPoint;

    public void addLineSegment(Vector2 start, Vector2 end) {
        for (LineSegment line : lines) {
            if (line.startPoint.equals(start) && line.endPoint.equals(end)) {
                // same line
                return;
            } else if (line.startPoint.equals(end) && line.endPoint.equals(start)) {
                // same line
                return;
            }
        }

        lines.add(new LineSegment(start, end));
    }

    public LevelDefinition build() {
        LevelDefinition level = new LevelDefinition();
        level.levelLines = new Array<>(lines.size());
        for (LineSegment line : lines) {
            level.levelLines.add(line);
        }

        level.startPosition = new Vector2(startPoint);
        level.finishPlatform = new Rectangle(landingPlat);

        return level;
    }

    public void setLandingPlatform(Vector2 startPoint, Vector2 endPoint) {
        landingPlat = new Rectangle(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        if (landingPlat.width < 0) {
            landingPlat.x += landingPlat.width;
            landingPlat.width *= -1;
        }

        if (landingPlat.height < 0) {
            landingPlat.y += landingPlat.height;
            landingPlat.height *= -1;
        }
    }

    public void setStartPoint(Vector2 point) {
        startPoint = new Vector2(point);
    }

    public void setLevel(LevelDefinition level) {
        lines.clear();
        for (LineSegment levelLine : level.levelLines) {
            lines.add(levelLine);
        }

        startPoint = level.startPosition;

        landingPlat = level.finishPlatform;
    }

    public void removeLineSegment(LineSegment line) {
        lines.remove(line);
    }
}
