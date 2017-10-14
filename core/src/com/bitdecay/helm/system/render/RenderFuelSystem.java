package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;
import com.bitdecay.helm.unlock.palette.GameColors;

/**
 * Created by Monday on 12/19/2016.
 */
public class RenderFuelSystem extends AbstractIteratingGameSystem {
    private ShapeRenderer renderer;

    public RenderFuelSystem(GamePilot pilot, ShapeRenderer renderer) {
        super(pilot);
        this.renderer = renderer;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        BodyDefComponent body = entity.getComponent(BodyDefComponent.class);
        FuelComponent fuel = entity.getComponent(FuelComponent.class);

        float fuelPercentage = fuel.fuelRemaining / fuel.maxFuel;
        if (fuelPercentage <= 0) {
            return;
        }

        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < body.bodyPoints.length; i += 2) {
            minX = Math.min(minX, body.bodyPoints[i]);
            maxX = Math.max(maxX, body.bodyPoints[i]);
            minY = Math.min(minY, body.bodyPoints[i+1]);
            maxY = Math.max(maxY, body.bodyPoints[i+1]);
        }

        float bodyHeight = maxY - minY;

        float bodyWidth = maxX - minX;
        bodyWidth *= fuelPercentage;

        // This is hard-coded to work with the exact triangle that the ship body is defined as...
        // Figure out a way to truncate a polygon in a generic way.
        float[] fuelPoints = new float[] {
                    body.bodyPoints[0], body.bodyPoints[1],
                    body.bodyPoints[0] + bodyWidth, body.bodyPoints[1] - bodyHeight/2 * fuelPercentage,
                    body.bodyPoints[0] + bodyWidth, body.bodyPoints[5] + bodyHeight/2 * fuelPercentage,
                    body.bodyPoints[4], body.bodyPoints[5]
            };

        fuelPoints = Geom.rotatePoints(fuelPoints, transform.angle);
        fuelPoints = Geom.translatePoints(fuelPoints, transform.position);

        renderer.setColor(pilot.getHelm().palette.get(GameColors.FUEL_METER));
        renderer.polygon(fuelPoints);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BodyDefComponent.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(FuelComponent.class);
    }
}
