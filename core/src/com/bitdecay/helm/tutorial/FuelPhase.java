package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/8/2017.
 */

public class FuelPhase extends PagedPhase {
    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;


    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        init(stage);

        ship = TutorialUtils.getShip(player.allEntities);

        makePages();
    }

    private void makePages() {
        final Vector2 playerLocation = ship.getComponent(TransformComponent.class).position;
        RotatingLabel fuelLabel1 = new RotatingLabel("Boosting uses fuel", game.fontScale, game.skin, stageClickListener);
        fuelLabel1.setOrigin(Align.center);
        RotatingLabel fuelLabel2 = new RotatingLabel("So pay attention to the", game.fontScale, game.skin, stageClickListener);
        fuelLabel2.setOrigin(Align.center);
        RotatingLabel fuelLabel3 = new RotatingLabel("fuel line on your ship!", game.fontScale, game.skin, stageClickListener);
        fuelLabel3.setOrigin(Align.center);

        Table fuelTable = new Table();
        fuelTable.setTouchable(Touchable.disabled);
        fuelTable.align(Align.left);
        fuelTable.add(fuelLabel1).center();
        fuelTable.row();
        fuelTable.add(fuelLabel2).center();
        fuelTable.row();
        fuelTable.add(fuelLabel3).center();

        final UpdatingContainer page1 = new UpdatingContainer(fuelTable);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                page1.setPosition(project.x - page1.getPrefWidth()/2, project.y);
            }
        };
        pages.add(page1);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        return currentPage >= pages.size;
    }
}
