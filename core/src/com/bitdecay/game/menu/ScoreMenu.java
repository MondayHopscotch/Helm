package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;

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

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
        mainTable.setOrigin(Align.center);

        Table scoreTable = new Table();
        scoreTable.align(Align.center);
        scoreTable.setOrigin(Align.center);

        landingSpeedLabel = new Label(getLeftPaddedString("Landing Speed:"), skin);
        landingSpeedLabel.setFontScale(6);
        landingSpeedLabel.setAlignment(Align.right);

        landingSpeedScore = new Label("----", skin);
        landingSpeedScore.setFontScale(6);
        landingSpeedScore.setAlignment(Align.right);
        landingSpeedScore.setOrigin(Align.right);

        scoreTable.add(landingSpeedLabel).padRight(100);
        scoreTable.add(landingSpeedScore).growX();
        scoreTable.row();

        landingAngleLabel = new Label(getLeftPaddedString("Landing Angle:"), skin);
        landingAngleLabel.setFontScale(6);
        landingAngleLabel.setAlignment(Align.right);

        landingAngleScore = new Label("----", skin);
        landingAngleScore.setFontScale(6);
        landingAngleScore.setAlignment(Align.right);
        landingAngleScore.setOrigin(Align.right);

        scoreTable.add(landingAngleLabel).padRight(100);
        scoreTable.add(landingAngleScore).growX();
        scoreTable.row();

        fuelLeftLabel = new Label(getLeftPaddedString("Fuel Remaining:"), skin);
        fuelLeftLabel.setFontScale(6);
        fuelLeftLabel.setAlignment(Align.right);

        fuelLeftPercent = new Label("----", skin);
        fuelLeftPercent.setFontScale(6);
        fuelLeftPercent.setAlignment(Align.right);
        fuelLeftPercent.setOrigin(Align.right);

        scoreTable.add(fuelLeftLabel).padRight(100);
        scoreTable.add(fuelLeftPercent).growX();
        scoreTable.row();

        totalScoreLabel = new Label(getLeftPaddedString("Total Score:"), skin);
        totalScoreLabel.setFontScale(6);
        totalScoreLabel.setAlignment(Align.right);

        totalScoreScore = new Label("----", skin);
        totalScoreScore.setFontScale(6);
        totalScoreScore.setAlignment(Align.right);
        totalScoreScore.setOrigin(Align.right);
        totalScoreScore.setColor(Color.LIGHT_GRAY);

        scoreTable.add(totalScoreLabel).padRight(100);
        scoreTable.add(totalScoreScore).growX();
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

        Table nextTabel = new Table();
        nextTabel.align(Align.center);
        nextTabel.setOrigin(Align.center);
        nextButton = new TextButton(NEXT_LEVEL_TEXT, skin);
        nextButton.getLabel().setFontScale(5);
        nextButton.addListener(nextLevelAction);
        nextButton.align(Align.center);
        nextButton.setOrigin(Align.center);

        nextTabel.add(nextButton);

        mainTable.add(scoreTable);
        mainTable.row();
        mainTable.add(nextTabel).padTop(200);

        stage.addActor(mainTable);

    }

    private String getLeftPaddedString(String input) {
//        return String.format("%15s", input);
        return input;
    }

    public void updateAndDraw() {
        stage.act();
        if (visible) {
            stage.draw();
        }
    }

    public void setScore(LandingScore score, int totalScore) {
        stage.addAction(
                Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                setAllInvisible();
                            }
                        }),
                        Actions.delay(.2f),
                        Actions.run(getShowActorRunnable(landingSpeedLabel)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(landingSpeedScore)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(landingAngleLabel)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(landingAngleScore)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(fuelLeftLabel)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(fuelLeftPercent)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(totalScoreLabel)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(totalScoreScore)),
                        Actions.delay(.5f),
                        Actions.run(getShowActorRunnable(nextButton))
                )
        );
        landingSpeedScore.setText(getLeftPaddedString(Integer.toString(score.speedScore)));
        landingAngleScore.setText(getLeftPaddedString(Integer.toString(score.angleScore)));
        totalScoreScore.setText(getLeftPaddedString(Integer.toString(totalScore)));
        fuelLeftPercent.setText(getLeftPaddedString(String.format("%.2f%%", score.fuelLeft * 100)));
    }

    private Runnable getShowActorRunnable(final Actor actor) {
        return new Runnable() {
            @Override
            public void run() {
                actor.setVisible(true);
                pilot.doSound(SoundMode.PLAY, SFXLibrary.SCORE_POP);
            }
        };
    }

    private void setAllInvisible() {
        landingSpeedLabel.setVisible(false);
        landingSpeedScore.setVisible(false);
        landingAngleLabel.setVisible(false);
        landingAngleScore.setVisible(false);
        fuelLeftLabel.setVisible(false);
        fuelLeftPercent.setVisible(false);
        totalScoreLabel.setVisible(false);
        totalScoreScore.setVisible(false);
        nextButton.setVisible(false);
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
