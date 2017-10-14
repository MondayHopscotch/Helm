package com.bitdecay.helm.desktop.editor;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.world.WormholePair;

import java.util.ArrayList;

/**
 * Created by Monday on 1/2/2017.
 */
public class LevelBuilder {

    public ArrayList<com.bitdecay.helm.world.LineSegment> lines = new ArrayList<>();
    public ArrayList<Circle> gravityWells = new ArrayList<>();
    public ArrayList<WormholePair> wormholes = new ArrayList<>();
    public ArrayList<Circle> repulsionFields = new ArrayList<>();
    public ArrayList<Circle> focusPoints = new ArrayList<>();
    public Rectangle landingPlat;
    public float landingPlatRotationInDegrees;
    public Vector2 startPoint;
    public int startingFuel = 300;
    public String name = "";

    public int devScore = Integer.MAX_VALUE;
    public int goldScore = 0;
    public int silverScore = 0;
    public int bronzeScore = 0;

    public float devTime = 0f;
    public float goldTime = 0f;
    public float silverTime = 0f;
    public float bronzeTime = 0f;

    public LevelBuilder() {
        setLevel(new com.bitdecay.helm.world.LevelDefinition());
    }

    public void setStartPoint(Vector2 point) {
        startPoint = new Vector2(point);
    }

    public void setLandingPlatform(Rectangle rectangle, float rotation) {
        landingPlat = new Rectangle(rectangle);
        landingPlatRotationInDegrees = rotation;
        if (landingPlat.width < 0) {
            landingPlat.x += landingPlat.width;
            landingPlat.width *= -1;
        }

        if (landingPlat.height < 0) {
            landingPlat.y += landingPlat.height;
            landingPlat.height *= -1;
        }
    }

    public void addLineSegment(Vector2 start, Vector2 end) {
        for (com.bitdecay.helm.world.LineSegment line : lines) {
            if (line.startPoint.equals(start) && line.endPoint.equals(end)) {
                // same line
                return;
            } else if (line.startPoint.equals(end) && line.endPoint.equals(start)) {
                // same line
                return;
            }
        }

        lines.add(new com.bitdecay.helm.world.LineSegment(start, end));
    }

    public void removeLineSegment(com.bitdecay.helm.world.LineSegment line) {
        lines.remove(line);
    }

    public void addGravityWell(Vector2 position, float size) {
        gravityWells.add(new Circle(position, size));
    }

    public void removeGravityWell(Circle well) {
        gravityWells.remove(well);
    }

    public void addWormhole(Circle entrance, Circle exit) {
        wormholes.add(new WormholePair(entrance, exit));
    }

    public void removeWormhole(WormholePair pair) {
        wormholes.remove(pair);
    }

    public void addRepulsionField(Vector2 position, float size) {
        repulsionFields.add(new Circle(position, size));
    }

    public void removeRepulsionField(Circle field) {
        repulsionFields.remove(field);
    }

    public void addFocusPoint(Vector2 startPoint, float radius) {
        focusPoints.add(new Circle(startPoint, radius));
    }

    public void removeFocusPoint(Circle focusPoint) {
        focusPoints.remove(focusPoint);
    }

    public void setLevel(com.bitdecay.helm.world.LevelDefinition level) {
        lines.clear();
        for (com.bitdecay.helm.world.LineSegment levelLine : level.levelLines) {
            lines.add(levelLine);
        }

        gravityWells.clear();
        for (Circle gravityWell : level.gravityWells) {
            gravityWells.add(gravityWell);
        }

        wormholes.clear();
        for (WormholePair wormhole : level.wormholes) {
            wormholes.add(wormhole);
        }

        repulsionFields.clear();
        for (Circle repulsionField : level.repulsionFields) {
            repulsionFields.add(repulsionField);
        }

        focusPoints.clear();
        for (Circle circle : level.focusPoints) {
            focusPoints.add(circle);
        }

        startPoint = level.startPosition;

        startingFuel = level.startingFuel;

        landingPlat = level.finishPlatform;
        landingPlatRotationInDegrees = level.finishPlatformRotation;

        name = level.name;

        devScore = level.devScore;
        goldScore = level.goldScore;
        silverScore = level.silverScore;
        bronzeScore = level.bronzeScore;

        devTime = level.devTime;
        goldTime = level.goldTime;
        silverTime = level.silverTime;
        bronzeTime = level.bronzeTime;
    }

    public boolean isLevelValid() {
        return landingPlat != null && startPoint != null && name != null && !name.equals("");
    }

    public com.bitdecay.helm.world.LevelDefinition build() {
        com.bitdecay.helm.world.LevelDefinition level = new com.bitdecay.helm.world.LevelDefinition();
        level.name = name;

        level.devScore = devScore;
        level.goldScore = goldScore;
        level.silverScore = silverScore;
        level.bronzeScore = bronzeScore;

        level.devTime = devTime;
        level.goldTime = goldTime;
        level.silverTime = silverTime;
        level.bronzeTime = bronzeTime;

        level.levelLines = new Array<>(lines.size());
        for (com.bitdecay.helm.world.LineSegment line : lines) {
            level.levelLines.add(line);
        }

        level.gravityWells = new Array<>(gravityWells.size());
        for (Circle gravityWell : gravityWells) {
            level.gravityWells.add(gravityWell);
        }

        level.repulsionFields = new Array<>(repulsionFields.size());
        for (Circle repulsionField : repulsionFields) {
            level.repulsionFields.add(repulsionField);
        }

        level.wormholes = new Array<>(wormholes.size());
        for (WormholePair wormhole : wormholes) {
            level.wormholes.add(wormhole);
        }

        for (Circle focalPoint : focusPoints) {
            level.focusPoints.add(focalPoint);
        }

        level.startPosition = new Vector2(startPoint);
        level.finishPlatform = new Rectangle(landingPlat);
        level.finishPlatformRotation = landingPlatRotationInDegrees;

        level.startingFuel = startingFuel;

        return level;
    }
}
