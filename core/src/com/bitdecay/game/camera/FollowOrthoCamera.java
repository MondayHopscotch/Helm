package com.bitdecay.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

    private final float aspectRatio;
    private float originalWidth = 0;

    private List<Vector2> pointsToFollow;

    private float targetZoom = 0.1f;
    private Vector2 targetPosition = new Vector2(0, 0);

    private boolean shake = false;
    private float shakeStrength = 0;
    private float shakeDuration = 0;

    // These are here so we can reuse instances
    private final Rectangle perfectFitRect = new Rectangle();
    private final Rectangle aspectFitRect = new Rectangle();
    private final Rectangle bufferedFitRect = new Rectangle();

    public FollowOrthoCamera(float width, float height){
        super(width, height);
        originalWidth = width;
        aspectRatio = width / height;
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
        if (pointsToFollow == null) {
            pointsToFollow = new ArrayList<>();
        }

        if (pointsToFollow.size() > 0) {
            updateFit();

            targetPosition.x = bufferedFitRect.width / 2f + bufferedFitRect.x;
            targetPosition.y = bufferedFitRect.height / 2f + bufferedFitRect.y;
            targetZoom = bufferedFitRect.width / originalWidth;
            goToTargets();
            pointsToFollow.clear();
        }
        if (shake){
            translate(MathUtils.random(-shakeStrength, shakeStrength), MathUtils.random(-shakeStrength, shakeStrength));

            shakeDuration -= delta;
            if (shakeDuration <= 0){
                shake = false;
            }
        }
    }

    private void goToTargets(){
        float transX = (targetPosition.x - position.x) * snapSpeed;
        float transY = (targetPosition.y - position.y) * snapSpeed;
        translate(transX, transY);
        float transZoom = ((targetZoom < maxZoom ? maxZoom : (targetZoom > minZoom ? minZoom : targetZoom)) - zoom) * snapSpeed;
        zoom += transZoom;
    }

    private void updateFit(){
        setPerfectFit();

        setAspectFit();

        setBufferedFit();
    }

    private void setPerfectFit() {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float perfectFitWidth;
        float perfectFitHeight;
        if (pointsToFollow.size() <= 0) {
            minX = 0;
            minY = 0;
            maxX = 0;
            maxY = 0;
        } else {
            for (Vector2 point : pointsToFollow) {
                if (point.x < minX) {
                    minX = point.x;
                }
                if (point.x > maxX) {
                    maxX = point.x;
                }
                if (point.y < minY) {
                    minY = point.y;
                }
                if (point.y > maxY) {
                    maxY = point.y;
                }
            }
        }

        perfectFitWidth = maxX - minX;
        perfectFitHeight = maxY - minY;

        perfectFitRect.x = minX;
        perfectFitRect.y = minY;
        perfectFitRect.width = perfectFitWidth;
        perfectFitRect.height = perfectFitHeight;
    }

    private void setAspectFit() {
        aspectFitRect.set(perfectFitRect);
        float currentAspect = perfectFitRect.width / perfectFitRect.height;
        if (currentAspect < aspectRatio) {
            float newWidth = perfectFitRect.height * aspectRatio;
            aspectFitRect.x -= (newWidth - perfectFitRect.width) / 2;
            aspectFitRect.width = newWidth;
        } else if (currentAspect > aspectRatio) {
            float newHeight = perfectFitRect.width / aspectRatio;
            aspectFitRect.y -= (newHeight - perfectFitRect.height) / 2;
            aspectFitRect.height = newHeight;
        }
    }

    private void setBufferedFit() {
        bufferedFitRect.set(aspectFitRect);
        bufferedFitRect.x -= buffer;
        bufferedFitRect.width += 2 * buffer;

        bufferedFitRect.y -= buffer;
        bufferedFitRect.height += 2 * buffer;
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
}