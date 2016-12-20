package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.scoring.LandingScore;

/**
 * Created by Monday on 12/17/2016.
 */
public class ScoreMenu {

    public final Stage stage;
    private final Skin skin;
    public boolean visible = false;
    private GamePilot pilot;

    private Label landingSpeedLabel;
    private Label landingSpeedScore;

    private Label landingAngleLabel;
    private Label landingAngleScore;

    private Label fuelLeftLabel;
    private Label fuelLeftPercent;

    public ScoreMenu(final GamePilot pilot) {
        this.pilot = pilot;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/skin.json"));

        Table scoreTable = new Table();
        scoreTable.setFillParent(true);
        scoreTable.align(Align.center);
        scoreTable.setOrigin(Align.center);

        Table landingSpeedTable = new Table();
        landingSpeedLabel = new Label("Landing Speed:", skin);
        landingSpeedLabel.setFontScale(6);
        landingSpeedLabel.setAlignment(Align.left);


        landingSpeedScore = new Label("----", skin);
        landingSpeedScore.setFontScale(6);
        landingSpeedScore.setAlignment(Align.right);


        landingSpeedTable.add(landingSpeedLabel).padRight(100);
        landingSpeedTable.add(landingSpeedScore);

        scoreTable.add(landingSpeedTable);
        scoreTable.row();

        Table landingAngleTable = new Table();
        landingAngleLabel = new Label("Landing Angle:", skin);
        landingAngleLabel.setFontScale(6);
        landingAngleLabel.setAlignment(Align.left);

        landingAngleScore = new Label("----", skin);
        landingAngleScore.setFontScale(6);
        landingAngleScore.setAlignment(Align.right);

        landingAngleTable.add(landingAngleLabel).padRight(100);
        landingAngleTable.add(landingAngleScore);

        scoreTable.add(landingAngleTable);
        scoreTable.row();

        Table fuelLeftTable = new Table();
        fuelLeftLabel = new Label("Fuel Remaining:", skin);
        fuelLeftLabel.setFontScale(6);
        fuelLeftLabel.setAlignment(Align.left);

        fuelLeftPercent = new Label("----", skin);
        fuelLeftPercent.setFontScale(6);
        fuelLeftPercent.setAlignment(Align.right);

        fuelLeftTable.add(fuelLeftLabel).padRight(100);
        fuelLeftTable.add(fuelLeftPercent);

        scoreTable.add(fuelLeftTable);
        scoreTable.row();

        TextButton nextLevelButton = new TextButton("Next Level", skin);
        nextLevelButton.getLabel().setFontScale(5);
//        nextLevelButton.setSize(500, 300);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.nextLevel();
            }
        });

        scoreTable.add(nextLevelButton).padTop(200);

        stage.addActor(scoreTable);
    }

    public void updateAndDraw() {
        stage.act();
        if (visible) {
            stage.draw();
        }
    }

    public void setScore(LandingScore score) {
        landingSpeedScore.setText(Integer.toString(score.speedScore));
        landingAngleScore.setText(Integer.toString(score.angleScore));
        fuelLeftPercent.setText(String.format("%.2f%%", score.fuelLeft * 100));
    }
}
