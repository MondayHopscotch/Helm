package com.bitdecay.helm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.input.ReplayNameInputListener;
import com.bitdecay.helm.scoring.LandingScore;
import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.time.TimerUtils;
import com.bitdecay.helm.unlock.palette.PaletteList;

/**
 * Created by Monday on 12/17/2016.
 */
public class ScoreMenu {

    private static TextureRegion levelSelectTexture;
    private static TextureRegion retryLevelTexture;
    private static TextureRegion saveReplayTexture;
    private static TextureRegion saveCompleteTexture;
    private static TextureRegion nextLevelTexture;

    public final Stage stage;
    private final Skin skin;

    public boolean visible = false;

    private com.bitdecay.helm.GamePilot pilot;

    private Table achieveTable;

    private Label landingSpeedLabel;
    private Label landingSpeedScore;
    private Image landingSpeedIcon;

    private Label landingAngleLabel;
    private Label landingAngleScore;
    private Image landingAngleIcon;

    private Label landingAccuracyLabel;
    private Label landingAccuracyScore;
    private Image landingAccuracyIcon;

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

    private final Table buttonTable;
    private final BitImageButton levelSelectButton;
    private final BitImageButton saveReplayButton;
    private final BitImageButton playAgainButton;
    private final BitImageButton nextLevelButton;

    public ScoreMenu(final com.bitdecay.helm.GamePilot pilot) {
        this.pilot = pilot;


        float iconSize = pilot.getHelm().fontScale * 0.4f;
        initIcons();

        stage = new Stage();
        stage.setDebugAll(Helm.debug);

        skin = pilot.getHelm().skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
        mainTable.setOrigin(Align.center);

        Table scoreTable = new Table();
        scoreTable.align(Align.center);
        scoreTable.setOrigin(Align.center);
        scoreTable.columnDefaults(2).width(com.bitdecay.helm.menu.MedalUtils.imageSize);

        landingSpeedLabel = new Label(getLeftPaddedString("Landing Speed:"), skin);
        landingSpeedLabel.setFontScale(pilot.getHelm().fontScale);
        landingSpeedLabel.setAlignment(Align.right);

        landingSpeedScore = new Label("----", skin);
        landingSpeedScore.setFontScale(pilot.getHelm().fontScale);
        landingSpeedScore.setAlignment(Align.right);
        landingSpeedScore.setOrigin(Align.right);

        landingSpeedIcon = IconUtils.getEmptyIcon();

        scoreTable.add(landingSpeedLabel).padRight(100);
        scoreTable.add(landingSpeedScore).growX();
        // these icons should be the same size as the medals
        scoreTable.add(landingSpeedIcon).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        landingAngleLabel = new Label(getLeftPaddedString("Landing Angle:"), skin);
        landingAngleLabel.setFontScale(pilot.getHelm().fontScale);
        landingAngleLabel.setAlignment(Align.right);

        landingAngleScore = new Label("----", skin);
        landingAngleScore.setFontScale(pilot.getHelm().fontScale);
        landingAngleScore.setAlignment(Align.right);
        landingAngleScore.setOrigin(Align.right);

        landingAngleIcon = IconUtils.getEmptyIcon();

        scoreTable.add(landingAngleLabel).padRight(100);
        scoreTable.add(landingAngleScore).growX();
        // these icons should be the same size as the medals
        scoreTable.add(landingAngleIcon).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        landingAccuracyLabel = new Label(getLeftPaddedString("Landing Accuracy:"), skin);
        landingAccuracyLabel.setFontScale(pilot.getHelm().fontScale);
        landingAccuracyLabel.setAlignment(Align.right);

        landingAccuracyScore = new Label("----", skin);
        landingAccuracyScore.setFontScale(pilot.getHelm().fontScale);
        landingAccuracyScore.setAlignment(Align.right);
        landingAccuracyScore.setOrigin(Align.right);

        landingAccuracyIcon = IconUtils.getEmptyIcon();

        scoreTable.add(landingAccuracyLabel).padRight(100);
        scoreTable.add(landingAccuracyScore).growX();
        // these icons should be the same size as the medals
        scoreTable.add(landingAccuracyIcon).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
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

        totalScoreMedal = com.bitdecay.helm.menu.MedalUtils.getRankImage(com.bitdecay.helm.menu.MedalUtils.LevelRating.UNRANKED);

        scoreTable.add(totalScoreLabel).padRight(100);
        scoreTable.add(totalScoreScore).growX();
        scoreTable.add(totalScoreMedal).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        totalTimeLabel = new Label(getLeftPaddedString("Total Time:"), skin);
        totalTimeLabel.setFontScale(pilot.getHelm().fontScale);
        totalTimeLabel.setAlignment(Align.right);

        totalTimeValue = new Label("----", skin);
        totalTimeValue.setFontScale(pilot.getHelm().fontScale);
        totalTimeValue.setAlignment(Align.right);
        totalTimeValue.setOrigin(Align.right);

        totalTimeMedal = com.bitdecay.helm.menu.MedalUtils.getRankImage(com.bitdecay.helm.menu.MedalUtils.LevelRating.UNRANKED);

        scoreTable.add(totalTimeLabel).padRight(100);
        scoreTable.add(totalTimeValue).growX();
        scoreTable.add(totalTimeMedal).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.row();

        buttonTable = new Table();
        buttonTable.align(Align.center);
        buttonTable.setOrigin(Align.center);

        TextureRegionDrawable levelSelectDrawable = new TextureRegionDrawable(levelSelectTexture);
        levelSelectButton = new BitImageButton(levelSelectDrawable, levelSelectDrawable, iconSize, skin);
        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.returnToMenus(false);
            }
        });

        TextureRegionDrawable imageUp = new TextureRegionDrawable(saveReplayTexture);
        final TextureRegionDrawable imageDown = new TextureRegionDrawable(saveCompleteTexture);
        saveReplayButton = new BitImageButton(imageUp, imageDown, iconSize, skin);
        saveReplayButton.align(Align.bottomLeft);
        saveReplayButton.setOrigin(Align.bottomLeft);

        saveReplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String defaultName = TimerUtils.getDateAsString() + " - " + pilot.getLevelName();
                Gdx.input.getTextInput(new ReplayNameInputListener(pilot, saveReplayButton), "Replay Name", defaultName, "");
            }
        });

        TextureRegionDrawable retryImage = new TextureRegionDrawable(retryLevelTexture);
        playAgainButton = new BitImageButton(retryImage, retryImage, iconSize, skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.requestRestartLevel();
            }
        });


        TextureRegionDrawable nextLevelIcon = new TextureRegionDrawable(nextLevelTexture);
        nextLevelButton = new BitImageButton(nextLevelIcon, nextLevelIcon, iconSize, skin);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.goToNextLevel();
            }
        });

        rebuildButtonTable(false);

        mainTable.add(scoreTable);
        mainTable.row();
        mainTable.add(buttonTable).padTop(Gdx.graphics.getHeight() / 5);

        stage.addActor(mainTable);

        achieveTable = new Table();
        achieveTable.setHeight(stage.getHeight());
        achieveTable.setWidth(stage.getWidth() / 2);
        achieveTable.setPosition(stage.getWidth() / 2, 0);
        achieveTable.align(Align.right);
        achieveTable.setOrigin(Align.center);

        stage.addActor(achieveTable);
    }

    private void initIcons() {
        levelSelectTexture = pilot.getHelm().assets.get("img/icons.atlas", TextureAtlas.class).findRegion("exit_icon");
        retryLevelTexture = pilot.getHelm().assets.get("img/icons.atlas", TextureAtlas.class).findRegion("retry_icon");
        saveReplayTexture = pilot.getHelm().assets.get("img/icons.atlas", TextureAtlas.class).findRegion("save_icon");
        saveCompleteTexture = pilot.getHelm().assets.get("img/icons.atlas", TextureAtlas.class).findRegion("saved_icon");
        nextLevelTexture = pilot.getHelm().assets.get("img/icons.atlas", TextureAtlas.class).findRegion("next_icon");
    }

    private Actor addUnlockLabel(String text) {
        Label newThingLabel = new Label(text, skin);
        newThingLabel.setAlignment(Align.center);
        newThingLabel.setFontScale(pilot.getHelm().fontScale * 0.5f);
        newThingLabel.setOrigin(Align.center);

        Table labelParent = new Table();
        labelParent.setTransform(true);
        labelParent.setFillParent(false);
        labelParent.setRotation(MathUtils.random(-12, 12));
        labelParent.add(newThingLabel);
        // width / height haven't been calculated yet, so do it manually
        labelParent.setOrigin(labelParent.getMinWidth() / 2, labelParent.getMinHeight() / 2);

        boolean padTop = achieveTable.hasChildren();
        Cell parentCell = achieveTable.add(labelParent).align(Align.center);
        if (padTop) {
            parentCell.padTop(pilot.getHelm().fontScale * 10); // arbitrary spacing that feels pretty good
        }
        achieveTable.row();
        return labelParent;
    }

    public void rebuildButtonTable(final boolean isReplay) {
        buttonTable.clear();

        buttonTable.add(levelSelectButton);

        if (!isReplay) {
            // if we aren't watching a replay, let them save it
            buttonTable.add(playAgainButton).padLeft(pilot.getHelm().fontScale * 15);
            buttonTable.add(saveReplayButton).padLeft(pilot.getHelm().fontScale * 15);
            buttonTable.add(nextLevelButton).padLeft(pilot.getHelm().fontScale * 15);
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

    public void setScore(com.bitdecay.helm.world.LevelInstance currentLevel, LandingScore score) {
        achieveTable.clear();

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
                Actions.run(getShowActorsRunnable(landingSpeedScore, landingSpeedIcon)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(landingAngleScore, landingAngleIcon)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(landingAccuracyScore, landingAccuracyIcon)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(fuelLeftPercent)),
                Actions.delay(.5f),
                Actions.run(getShowActorsRunnable(fuelScoreScore)),
                Actions.delay(1f)
        );

        boolean newHighScore = score.pointsImprovement > 0;
        boolean newBestTime = score.secondsImprovement < 0;

        if (newHighScore) {
            totalScoreScore.setColor(Color.GOLD);
        } else {
            totalScoreScore.setColor(Color.WHITE);
        }

        if (newBestTime) {
            totalTimeValue.setColor(Color.GOLD);
        } else {
            totalTimeValue.setColor(Color.WHITE);
        }

        if (newHighScore || newBestTime) {
            baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.HIGH_SCORE_BEAT,
                    totalScoreLabel,
                    totalScoreScore,
                    totalScoreMedal,
                    totalTimeLabel,
                    totalTimeValue,
                    totalTimeMedal
            )));
        } else {
            baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.NEXT_LEVEL,
                    totalScoreLabel,
                    totalScoreScore,
                    totalScoreMedal,
                    totalTimeLabel,
                    totalTimeValue,
                    totalTimeMedal
            )));

        }

        // check for palette unlock
        PaletteList.checkUnlocks(com.bitdecay.helm.scoring.ScoreUtils.getTotalHighScore(), score.pointsImprovement);

        for (String message : ScoreStamps.getPendingStamps()) {
            Actor stamp = addUnlockLabel(message);
            stamp.setVisible(false);
            baseScoreSequence.addAction(
                    Actions.sequence(
                            Actions.delay(.2f),
                            Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.STAMP, stamp))
                    )
            );
        }

        ScoreStamps.clearPendingStamps();

        baseScoreSequence.addAction(Actions.delay(1f));

        baseScoreSequence.addAction(Actions.run(getShowActorsRunnableWithSFX(SFXLibrary.NEXT_LEVEL, levelSelectButton, playAgainButton, saveReplayButton, nextLevelButton)));

        stage.addAction(baseScoreSequence);

        landingSpeedScore.setText(getLeftPaddedString(Integer.toString(score.speedScore)));
        landingSpeedIcon.setDrawable((score.speedScore == LandingScore.MAX_SPEED_SCORE ? IconUtils.getPerfectIcon() : IconUtils.getEmptyIcon()).getDrawable());

        landingAngleScore.setText(getLeftPaddedString(Integer.toString(score.angleScore)));
        landingAngleIcon.setDrawable((score.angleScore == LandingScore.MAX_ANGLE_SCORE ? IconUtils.getPerfectIcon() : IconUtils.getEmptyIcon()).getDrawable());

        landingAccuracyScore.setText(getLeftPaddedString(Integer.toString(score.accuracyScore)));
        landingAccuracyIcon.setDrawable((score.accuracyScore == LandingScore.MAX_ANGLE_SCORE ? IconUtils.getPerfectIcon() : IconUtils.getEmptyIcon()).getDrawable());

        fuelLeftPercent.setText(getLeftPaddedString(String.format("%.2f%%", score.fuelLeft * 100)));
        fuelScoreScore.setText(getLeftPaddedString(Integer.toString(score.fuelScore)));

        totalTimeValue.setText(TimerUtils.getFormattedTime(score.timeTaken));
        totalTimeMedal.setDrawable(com.bitdecay.helm.menu.MedalUtils.getIconForBestTime(currentLevel, score.timeTaken).getDrawable());
        totalScoreScore.setText(getLeftPaddedString(Integer.toString(score.total())));
        totalScoreMedal.setDrawable(com.bitdecay.helm.menu.MedalUtils.getIconForScore(currentLevel, score.total()).getDrawable());
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
        landingSpeedIcon.setVisible(false);
        landingAngleLabel.setVisible(false);
        landingAngleScore.setVisible(false);
        landingAngleIcon.setVisible(false);
        landingAccuracyLabel.setVisible(false);
        landingAccuracyScore.setVisible(false);
        landingAccuracyIcon.setVisible(false);
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
        levelSelectButton.setVisible(false);
        playAgainButton.setVisible(false);
        saveReplayButton.setVisible(false);
        nextLevelButton.setVisible(false);
    }
}
