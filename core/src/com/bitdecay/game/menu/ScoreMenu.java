package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.world.LevelInstance;

/**
 * Created by Monday on 12/17/2016.
 */
public class ScoreMenu {

    private static final String RETURN_TO_LEVEL_SELECT = "Level Select";
    private static final String RETURN_TO_REPLAY = "Done";
    private static final String SAVE_REPLAY_TEXT = "Save Replay";
    private static final String PLAY_AGAIN = "Play Again";

    public final Stage stage;
    private final Skin skin;

    public boolean visible = false;

    private GamePilot pilot;

    private Label landingSpeedLabel;
    private Label landingSpeedScore;

    private Label landingAngleLabel;
    private Label landingAngleScore;

    private Label landingAccuracyLabel;
    private Label landingAccuracyScore;

    private Label fuelLeftLabel;

    private Label fuelScoreLabel;

    private Label fuelScoreScore;
    private Label fuelLeftPercent;

    private Label totalTimeLabel;
    private Label totalTimeValue;
    private Image totalTimeMedal;

    private Label totalScoreLabel;
    private Label totalScoreScore;
    private Image totalScoreMedal;

    private final Table nextTable;
    private final TextButton nextButton;
    private final TextButton saveReplayButton;
    private final TextButton playAgainButton;

    public ScoreMenu(final GamePilot pilot) {
        this.pilot = pilot;
        stage = new Stage();
        skin = pilot.getHelm().skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
        mainTable.setOrigin(Align.center);

        Table scoreTable = new Table();
        scoreTable.align(Align.center);
        scoreTable.setOrigin(Align.center);
        scoreTable.columnDefaults(2).width(MedalUtils.imageSize);

        landingSpeedLabel = new Label(getLeftPaddedString("Landing Speed:"), skin);
        landingSpeedLabel.setFontScale(pilot.getHelm().fontScale);
        landingSpeedLabel.setAlignment(Align.right);

        landingSpeedScore = new Label("----", skin);
        landingSpeedScore.setFontScale(pilot.getHelm().fontScale);
        landingSpeedScore.setAlignment(Align.right);
        landingSpeedScore.setOrigin(Align.right);

        scoreTable.add(landingSpeedLabel).padRight(100);
        scoreTable.add(landingSpeedScore).growX();
        scoreTable.row();

        landingAngleLabel = new Label(getLeftPaddedString("Landing Angle:"), skin);
        landingAngleLabel.setFontScale(pilot.getHelm().fontScale);
        landingAngleLabel.setAlignment(Align.right);

        landingAngleScore = new Label("----", skin);
        landingAngleScore.setFontScale(pilot.getHelm().fontScale);
        landingAngleScore.setAlignment(Align.right);
        landingAngleScore.setOrigin(Align.right);

        scoreTable.add(landingAngleLabel).padRight(100);
        scoreTable.add(landingAngleScore).growX();
        scoreTable.row();

        landingAccuracyLabel = new Label(getLeftPaddedString("Landing Accuracy:"), skin);
        landingAccuracyLabel.setFontScale(pilot.getHelm().fontScale);
        landingAccuracyLabel.setAlignment(Align.right);

        landingAccuracyScore = new Label("----", skin);
        landingAccuracyScore.setFontScale(pilot.getHelm().fontScale);
        landingAccuracyScore.setAlignment(Align.right);
        landingAccuracyScore.setOrigin(Align.right);

        scoreTable.add(landingAccuracyLabel).padRight(100);
        scoreTable.add(landingAccuracyScore).growX();
        scoreTable.row();

        fuelLeftLabel = new Label(getLeftPaddedString("Fuel Remaining:"), skin);
        fuelLeftLabel.setFontScale(pilot.getHelm().fontScale);
        fuelLeftLabel.setAlignment(Align.right);

        fuelLeftPercent = new Label("----", skin);
        fuelLeftPercent.setFontScale(pilot.getHelm().fontScale);
        fuelLeftPercent.setAlignment(Align.right);
        fuelLeftPercent.setOrigin(Align.right);

        fuelScoreLabel = new Label(getLeftPaddedString("Fuel Score:"), skin);
        fuelScoreLabel.setFontScale(pilot.getHelm().fontScale);
        fuelScoreLabel.setAlignment(Align.right);

        fuelScoreScore = new Label("----", skin);
        fuelScoreScore.setFontScale(pilot.getHelm().fontScale);
        fuelScoreScore.setAlignment(Align.right);
        fuelScoreScore.setOrigin(Align.right);

        scoreTable.add(fuelLeftLabel).padRight(100);
        scoreTable.add(fuelLeftPercent).growX();
        scoreTable.row();

        scoreTable.add(fuelScoreLabel).padRight(100);
        scoreTable.add(fuelScoreScore).growX();
        scoreTable.row();

        totalScoreLabel = new Label(getLeftPaddedString("Total Score:"), skin);
        totalScoreLabel.setFontScale(pilot.getHelm().fontScale);
        totalScoreLabel.setAlignment(Align.right);

        totalScoreScore = new Label("----", skin);
        totalScoreScore.setFontScale(pilot.getHelm().fontScale);
        totalScoreScore.setAlignment(Align.right);
        totalScoreScore.setOrigin(Align.right);

        totalScoreMedal = MedalUtils.getRankImage(MedalUtils.LevelRating.UNRANKED);

        scoreTable.add(totalScoreLabel).padRight(100);
        scoreTable.add(totalScoreScore).growX();
        scoreTable.add(totalScoreMedal).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        totalTimeLabel = new Label(getLeftPaddedString("Total Time:"), skin);
        totalTimeLabel.setFontScale(pilot.getHelm().fontScale);
        totalTimeLabel.setAlignment(Align.right);

        totalTimeValue = new Label("----", skin);
        totalTimeValue.setFontScale(pilot.getHelm().fontScale);
        totalTimeValue.setAlignment(Align.right);
        totalTimeValue.setOrigin(Align.right);

        totalTimeMedal = MedalUtils.getRankImage(MedalUtils.LevelRating.UNRANKED);

        scoreTable.add(totalTimeLabel).padRight(100);
        scoreTable.add(totalTimeValue).growX();
        Image medalImage = MedalUtils.getRankImage(MedalUtils.LevelRating.UNRANKED);
        scoreTable.add(totalTimeMedal).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        nextTable = new Table();
        nextTable.align(Align.center);
        nextTable.setOrigin(Align.center);

        nextButton = new TextButton(RETURN_TO_LEVEL_SELECT, skin);
        nextButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        nextButton.align(Align.center);
        nextButton.setOrigin(Align.center);

        saveReplayButton = new TextButton(SAVE_REPLAY_TEXT, skin);
        saveReplayButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        saveReplayButton.align(Align.center);
        saveReplayButton.setOrigin(Align.center);
        saveReplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.saveLastReplay();
                saveReplayButton.getLabel().setText("Replay Saved");
                saveReplayButton.removeListener(this);
                saveReplayButton.setDisabled(true);
            }
        });

        playAgainButton = new TextButton(PLAY_AGAIN, skin);
        playAgainButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        playAgainButton.align(Align.center);
        playAgainButton.setOrigin(Align.center);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.requestRestartLevel();
            }
        });

        rebuildNextTable(false);

        mainTable.add(scoreTable);
        mainTable.row();
        mainTable.add(nextTable).padTop(Gdx.graphics.getHeight() / 5);

        stage.addActor(mainTable);

    }

    public void rebuildNextTable(final boolean isReplay) {
        nextTable.clear();
        nextButton.clearListeners();
        if (isReplay) {
            nextButton.getLabel().setText(RETURN_TO_REPLAY);
        } else {
            nextButton.getLabel().setText(RETURN_TO_LEVEL_SELECT);
        }
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.returnToMenus(false);
            }
        });

        nextTable.add(nextButton);

        if (!isReplay) {
            // if we aren't watching a replay, let them save it
            nextTable.add(playAgainButton).padLeft(pilot.getHelm().fontScale * 5);
            nextTable.add(saveReplayButton).padLeft(pilot.getHelm().fontScale * 5);
        }
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

    public void setScore(LevelInstance currentLevel, LandingScore score) {
        SequenceAction baseScoreSequence = Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setAllInvisible();
                    }
                }),
                Actions.delay(.2f),
                Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.LABEL_DISPLAY, landingSpeedLabel)),
                Actions.delay(.1f),
                Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.LABEL_DISPLAY, landingAngleLabel)),
                Actions.delay(.1f),
                Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.LABEL_DISPLAY, landingAccuracyLabel)),
                Actions.delay(.1f),
                Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.LABEL_DISPLAY, fuelLeftLabel)),
                Actions.delay(.1f),
                Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.LABEL_DISPLAY, fuelScoreLabel)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(landingSpeedScore)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(landingAngleScore)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(landingAccuracyScore)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(fuelLeftPercent)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(fuelScoreScore)),
                Actions.delay(1f)
        );

        if (score.newHighScore) {
            totalScoreScore.setColor(Color.GOLD);
        } else {
            totalScoreScore.setColor(Color.WHITE);
        }

        if (score.newBestTime) {
            totalTimeValue.setColor(Color.GOLD);
        } else {
            totalTimeValue.setColor(Color.WHITE);
        }

        if (score.newHighScore || score.newBestTime) {
            baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.HIGH_SCORE_BEAT,
                    totalScoreLabel,
                    totalScoreScore,
                    totalScoreMedal,
                    totalTimeLabel,
                    totalTimeValue,
                    totalTimeMedal
            )));
            baseScoreSequence.addAction(Actions.delay(1f));
        } else {
            baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.NEXT_LEVEL,
                    totalScoreLabel,
                    totalScoreScore,
                    totalScoreMedal,
                    totalTimeLabel,
                    totalTimeValue,
                    totalTimeMedal
            )));
            baseScoreSequence.addAction(Actions.delay(1f));

        }

        baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.NEXT_LEVEL, nextButton, playAgainButton, saveReplayButton)));

        stage.addAction(baseScoreSequence);

        landingSpeedScore.setText(getLeftPaddedString(Integer.toString(score.speedScore)));
        landingAngleScore.setText(getLeftPaddedString(Integer.toString(score.angleScore)));
        landingAccuracyScore.setText(getLeftPaddedString(Integer.toString(score.accuracyScore)));
        fuelLeftPercent.setText(getLeftPaddedString(String.format("%.2f%%", score.fuelLeft * 100)));
        fuelScoreScore.setText(getLeftPaddedString(Integer.toString(score.fuelScore)));

        totalTimeValue.setText(TimerUtils.getFormattedTime(score.timeTaken));
        totalTimeMedal.setDrawable(MedalUtils.getIconForBestTime(currentLevel, score.timeTaken).getDrawable());
        totalScoreScore.setText(getLeftPaddedString(Integer.toString(score.total())));
        totalScoreMedal.setDrawable(MedalUtils.getIconForScore(currentLevel, score.total()).getDrawable());
    }

    private Runnable getShowActorsRunnable(final Actor... actors) {
        return getShowActorsRunnableWithSFX(SFXLibrary.SCORE_POP, actors);

    }

    private Runnable getShowActorsRunnableWithSFX(final String sfxName, final Actor... actors) {
        return new Runnable() {
            @Override
            public void run() {
                if (sfxName != null) {
                    pilot.doSound(SoundMode.PLAY, sfxName);
                }
                for (Actor actor : actors) {
                    actor.setVisible(true);
                }
            }
        };
    }

    private void setAllInvisible() {
        landingSpeedLabel.setVisible(false);
        landingSpeedScore.setVisible(false);
        landingAngleLabel.setVisible(false);
        landingAngleScore.setVisible(false);
        landingAccuracyLabel.setVisible(false);
        landingAccuracyScore.setVisible(false);
        fuelLeftLabel.setVisible(false);
        fuelLeftPercent.setVisible(false);
        fuelScoreLabel.setVisible(false);
        fuelScoreScore.setVisible(false);
        totalScoreLabel.setVisible(false);
        totalScoreScore.setVisible(false);
        totalScoreMedal.setVisible(false);
        totalTimeLabel.setVisible(false);
        totalTimeMedal.setVisible(false);
        totalTimeValue.setVisible(false);
        nextButton.setVisible(false);
        playAgainButton.setVisible(false);
        saveReplayButton.setVisible(false);
    }
}
