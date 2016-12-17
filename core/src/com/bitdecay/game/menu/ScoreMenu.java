package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;

/**
 * Created by Monday on 12/17/2016.
 */
public class ScoreMenu {

    public final Stage stage;
    private final Skin skin;
    public boolean visible = false;
    private GamePilot pilot;

    public ScoreMenu(final GamePilot pilot) {
        this.pilot = pilot;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/skin.json"));

        Table scoreTable = new Table();
        scoreTable.setFillParent(true);
        scoreTable.align(Align.center);
        scoreTable.setOrigin(Align.center);

        TextButton nextLevelButton = new TextButton("Next Level", skin);
        nextLevelButton.setSize(500, 500);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.nextLevel();
            }
        });

        scoreTable.add(nextLevelButton).size(500, 500);

        stage.addActor(scoreTable);
    }

    public void updateAndDraw() {
        stage.act();
        if (visible) {
            stage.draw();
        }
    }
}
