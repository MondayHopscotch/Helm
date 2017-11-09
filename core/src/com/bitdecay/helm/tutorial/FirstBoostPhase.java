package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BoostCountComponent;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;


/**
 * Created by Monday on 10/26/2017.
 */

public class FirstBoostPhase extends PagedPhase {
    private LevelPlayer player;

    private FuelComponent fuelComponent;
    private BoostControlComponent controls;
    private BoosterComponent boost;
    private float boostTime;
    private GameEntity ship;

    @Override
    public void start(Helm game, LevelPlayer player, final Stage stage) {
        this.player = player;
        init(stage);

        ship = TutorialUtils.getShip(player.allEntities);
        setupShip();

        boostTime = 0;

        final Vector2 boostCenter = controls.activeArea.getCenter(new Vector2());
        RotatingLabel steeringLabel1 = new RotatingLabel("Touch this area", game.fontScale, game.skin, new ClickListener());
        steeringLabel1.setOrigin(Align.center);
        RotatingLabel steeringLabel2 = new RotatingLabel("to use your booster", game.fontScale, game.skin, new ClickListener());
        steeringLabel2.setOrigin(Align.center);

        Table boostTable = new Table();
        boostTable.align(Align.left);
        boostTable.add(steeringLabel1).center();
        boostTable.row();
        boostTable.add(steeringLabel2).center();

        final UpdatingContainer page1 = new UpdatingContainer(boostTable);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                page1.setPosition(boostCenter.x, boostCenter.y);
            }
        };
        page1.updater.run();
        pages.add(page1);

        nextPage();
    }

    public void setupShip() {
        fuelComponent = ship.getComponent(FuelComponent.class);
        controls = ship.getComponent(BoostControlComponent.class);
        boost = ship.getComponent(BoosterComponent.class);
        boost.strength = 0;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {


        if (boost.engaged) {
            boostTime += Gdx.graphics.getDeltaTime();
        }

        Rectangle rect = controls.activeArea;
        shaper.setColor(Color.WHITE);

        DrawUtils.drawDottedRect(shaper, rect);

        return boostTime >= 0.75f;
    }
}
