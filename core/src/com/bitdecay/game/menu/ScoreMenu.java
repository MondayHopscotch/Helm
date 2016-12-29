package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private static final String NEXT_LEVEL_TEXT = "Next Level";
    private static final String RETURN_TO_TITLE_TEXT = "Return To Title";

    public final Stage stage;
    private final Skin skin;
    public boolean visible = false;
    private GamePilot pilot;

    private Label landingSpeedLabel;
    private Label landingSpeedScore;

    private Label landingAngleLabel;
    private Label landingAngleScore;

    private Label totalScoreLabel;
    private Label totalScoreScore;

    private Label fuelLeftLabel;
    private Label fuelLeftPercent;
    private final TextButton nextButton;

    private ClickListener nextLevelAction;
    private ClickListener returnToTitleAction;

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

        Table totalScoreTable = new Table();
        totalScoreLabel = new Label("Total Score:", skin);
        totalScoreLabel.setFontScale(6);
        totalScoreLabel.setAlignment(Align.left);

        totalScoreScore = new Label("----", skin);
        totalScoreScore.setFontScale(7);
        totalScoreScore.setAlignment(Align.right);
        totalScoreScore.setColor(Color.LIGHT_GRAY);

        totalScoreTable.add(totalScoreLabel).padRight(100);
        totalScoreTable.add(totalScoreScore);

        scoreTable.add(totalScoreTable).padTop(20);
        scoreTable.row();

        nextLevelAction = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.nextLevel();
            }
        };

        returnToTitleAction = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.returnToTitle();
            }
        };

        nextButton = new TextButton(NEXT_LEVEL_TEXT, skin);
        nextButton.getLabel().setFontScale(5);
//        nextLevelButton.setSize(500, 300);
        nextButton.addListener(nextLevelAction);

        scoreTable.add(nextButton).padTop(200);

        stage.addActor(scoreTable);

    }

    public void updateAndDraw() {
        stage.act();
        if (visible) {
            stage.draw();
        }
    }

    public void setScore(LandingScore score, int totalScore) {
        landingSpeedScore.setText(Integer.toString(score.speedScore));
        landingAngleScore.setText(Integer.toString(score.angleScore));
        totalScoreScore.setText(Integer.toString(totalScore));
        fuelLeftPercent.setText(String.format("%.2f%%", score.fuelLeft * 100));
    }

    public void setNextLevelOption() {
        nextButton.removeListener(nextLevelAction);
        nextButton.removeListener(returnToTitleAction);

        nextButton.getLabel().setText(NEXT_LEVEL_TEXT);
        nextButton.addListener(nextLevelAction);
    }

    public void setReturnToMenuOption() {
        nextButton.removeListener(nextLevelAction);
        nextButton.removeListener(returnToTitleAction);

        nextButton.getLabel().setText(RETURN_TO_TITLE_TEXT);
        nextButton.addListener(returnToTitleAction);
    }

}
