package com.bitdecay.helm.collision;

import com.bitdecay.helm.component.collide.CollisionGeometryComponent;
import com.bitdecay.helm.math.Geom;

/**
 * Created by Monday on 2/18/2017.
 */

public abstract class Collider {

    protected CollisionGeometryComponent geom1;
    protected CollisionGeometryComponent geom2;

    protected float[] geom1WorkingSet;
    protected float[] geom2WorkingSet;

    protected Collider() {

    }

    protected Collider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        this.geom1 = geom1;
        this.geom2 = geom2;

        geom1WorkingSet = getWorkingGeom(geom1);
        geom2WorkingSet = getWorkingGeom(geom2);
    }

    public abstract boolean collisionFound();

    public void setGeom1(CollisionGeometryComponent geom1) {
        this.geom1 = geom1;
        geom1WorkingSet = getWorkingGeom(geom1);
    }

    public void setGeom2(CollisionGeometryComponent geom2) {
        this.geom2 = geom2;
        geom2WorkingSet = getWorkingGeom(geom2);
    }

    public float[] getGeom1WorkingSet() {
        return geom1WorkingSet;
    }

    public float[] getGeom2WorkingSet() {
        return geom2WorkingSet;
    }

    private float[] getWorkingGeom(CollisionGeometryComponent geomComponent) {
        float[] workingGeom = new float[geomComponent.originalGeom.length];
        System.arraycopy(geomComponent.originalGeom, 0, workingGeom, 0, geomComponent.originalGeom.length);

        // only modify non-circle geometries
        if (workingGeom.length > 1) {
            // apply rotation first
            workingGeom = Geom.rotatePoints(workingGeom, geomComponent.rotation);

            // then apply position
            for (int i = 0; i < workingGeom.length; i += 2) {
                workingGeom[i] = workingGeom[i] + geomComponent.posX;
                workingGeom[i+1] = workingGeom[i+1] + geomComponent.posY;
            }
        }


        return workingGeom;
    }
}
