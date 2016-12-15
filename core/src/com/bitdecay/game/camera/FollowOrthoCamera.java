package com.bitdecay.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * The FollowOrthoCamera will try and follow a list of points.  As the points spread out, the camera will zoom out.  As they get closer together, the camera will zoom in.  You have to give the camera a new list of points to follow every update step.
 */
public class FollowOrthoCamera extends OrthographicCamera {
    public float maxZoom = 0.05f;
    public float minZoom = 0.1f;
    public float buffer = 10;
    public float snapSpeed = 0.05f;
    public boolean limitWindow = false;
    public float limitWindowMinX = 0;
    public float limitWindowMinY = 0;
    public float limitWindowMaxX = 0;
    public float limitWindowMaxY = 0;


    private List<Vector2> pointsToFollow = new ArrayList<>();

    private float targetZoom = 0.1f;
    private Vector2 targetPosition = new Vector2(0, 0);

    private RectangleExt window;
    private float originalWidth = 0;
    private float widthRatio = 0;
    private float heightRatio = 0;

    private float minX = 0;
    private float minY = 0;
    private float maxX = 0;
    private float maxY = 0;
    private float midX = 0;
    private float midY = 0;
    private float maxW = 0;
    private float maxH = 0;

    private boolean shake = false;
    private float shakeStrength = 0;
    private float shakeDuration = 0;

    public FollowOrthoCamera(float width, float height){
        super(width, height);
        originalWidth = width;
        widthRatio = height / width;
        heightRatio = width / height;
        window = new RectangleExt(0, 0, width, height);
    }

    public void addFollowPoint(Vector2 point){
        pointsToFollow.add(point);
    }

    @Override
    public void update(){
        update(1f / 60f);
    }

    public void update(float delta){
        super.update();
        if (pointsToFollow != null && pointsToFollow.size() > 0) {
            getWorldMaxWindow();

            window.x = midX;
            window.y = midY;
            targetPosition.x = window.x;
            targetPosition.y = window.y;
            // try max width first
            window.width = maxW;
            window.height = maxW * widthRatio;
            window.setOriginAtCenter();
            if (!testAllPoints()) {
                // then try max height
                window.height = maxH;
                window.width = maxH * heightRatio;
                window.setOriginAtCenter();
            }
            targetZoom = window.width / originalWidth;
            //
            goToTargets();
            pointsToFollow = new ArrayList<>();
        }
        if (shake){
            translate(MathUtils.random(-shakeStrength, shakeStrength), MathUtils.random(-shakeStrength, shakeStrength));

            shakeDuration -= delta;
            if (shakeDuration <= 0){
                shake = false;
            }
        }
    }

    public void shake(float duration){
        this.shake = true;
        this.shakeStrength = 5;
        this.shakeDuration = duration;
    }

    public void shake(float duration, float strength){
        this.shake = true;
        this.shakeStrength = strength;
        this.shakeDuration = duration;
    }

    public void rumble(){
        this.shake = true;
        this.shakeStrength = 1;
        this.shakeDuration = 0.0001f;
    }

    private boolean testAllPoints(){
        for (Vector2 point : pointsToFollow){
            if (!window.contains(point)){
                return false;
            }
        }
        return true;
    }

    private void goToTargets(){
        float transX = (targetPosition.x - position.x) * snapSpeed;
        float transY = (targetPosition.y - position.y) * snapSpeed;
        translate(transX, transY);
        float transZoom = ((targetZoom < maxZoom ? maxZoom : (targetZoom > minZoom ? minZoom : targetZoom)) - zoom) * snapSpeed;
        zoom += transZoom;
    }

    private void getWorldMaxWindow(){
        minX = 0;
        minY = 0;
        maxX = 0;
        maxY = 0;
        maxW = 0;
        maxH = 0;
        midX = 0;
        midY = 0;
        if (pointsToFollow.size() > 0){
            Vector2 firstPoint = pointsToFollow.get(0);
            minX = firstPoint.x;
            minY = firstPoint.y;
            maxX = minX;
            maxY = minY;
        }

        for (Vector2 point : pointsToFollow){
            if (point.x < minX){
                minX = point.x;
            }
            if (point.x > maxX){
                maxX = point.x;
            }
            if (point.y < minY){
                minY = point.y;
            }
            if (point.y > maxY){
                maxY = point.y;
            }
        }

        if (limitWindow){
            minX = (minX < limitWindowMinX ? limitWindowMinX : (minX > limitWindowMaxX ? limitWindowMaxX : minX));
            maxX = (maxX > limitWindowMaxX ? limitWindowMaxX : (maxX < limitWindowMinX ? limitWindowMinX : maxX));

            minY = (minY < limitWindowMinY ? limitWindowMinY : (minY > limitWindowMaxY ? limitWindowMaxY : minY));
            maxY = (maxY > limitWindowMaxY ? limitWindowMaxY : (maxY < limitWindowMinY ? limitWindowMinY : maxY));

        }


        maxW = (maxX - minX) + (buffer * 2);
        maxH = (maxY - minY) + (buffer * 2);
        midX = maxW / 2f + (minX - buffer);
        midY = maxH / 2f + (minY - buffer);
    }

    private static class CamWindow {

    }
}