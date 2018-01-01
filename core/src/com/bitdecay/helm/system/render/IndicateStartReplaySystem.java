package com.bitdecay.helm.system.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.system.AbstractBaseGameSystem;

/**
 * Created by Monday on 1/1/2018.
 */

public class IndicateStartReplaySystem extends AbstractBaseGameSystem {
    private final RotatingLabel startLabel;
    private final Vector2 labelPosition = new Vector2();
    private final Stage stage;

    public IndicateStartReplaySystem(GamePilot pilot) {
        super(pilot);
        stage = new Stage();

        startLabel = new RotatingLabel("Tap To Start Replay", pilot.getHelm().fontScale, pilot.getHelm().skin);

        stage.addActor(startLabel);
    }

    @Override
    public void act(Array<GameEntity> entities, float delta) {
        startLabel.setVisible(false);
        for (GameEntity entity : entities) {
            if (canActOn(entity)) {
                entity.getComponent(BoostControlComponent.class).activeArea.getCenter(labelPosition);
                startLabel.setPosition(labelPosition.x, labelPosition.y);
                startLabel.setVisible(true);

                stage.act(delta);
                stage.draw();
                return;
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(BoostControlComponent.class,
                ShipLaunchComponent.class);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
