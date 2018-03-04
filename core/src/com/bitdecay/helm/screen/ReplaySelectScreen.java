package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Sort;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.input.InputReplay;
import com.bitdecay.helm.menu.BitImageButton;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;

import java.util.Comparator;

/**
 * Created by Monday on 2/26/2017.
 */
public class ReplaySelectScreen extends AbstractScrollingItemScreen {

    private static ReplaySelectScreen instance;

    public static ReplaySelectScreen get(Helm game) {
        if (instance == null) {
            instance = new ReplaySelectScreen(game);
        }
        return instance;
    }

    public ReplaySelectScreen(com.bitdecay.helm.Helm game) {
        super(game);
    }

    @Override
    public String getTitle() {
        return "Replays";
    }

    @Override
    public void populateRows(Table table) {
        FileHandle replayDir = Gdx.files.local(com.bitdecay.helm.persist.ReplayUtils.REPLAY_DIR);
        FileHandle[] fileList = replayDir.list();

        if (fileList.length > 0) {
            buildReplayRows(table, fileList);
        } else {
            buildInfoPage(table);
        }

    }

    private void buildInfoPage(Table table) {
        Label noReplayLabel = new Label("No replays found", skin);
        noReplayLabel.setAlignment(Align.center);
        noReplayLabel.setFontScale(game.fontScale * 1.5f);

        TextureRegion saveReplayTexture = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("save_icon");
        TextureRegionDrawable imageUp = new TextureRegionDrawable(saveReplayTexture);

        BitImageButton saveReplayButton = new BitImageButton(imageUp, imageUp, game.fontScale * 0.4f, skin);
        saveReplayButton.align(Align.bottomLeft);
        saveReplayButton.setOrigin(Align.bottomLeft);
        saveReplayButton.setTouchable(Touchable.disabled);

        Table iconHintTable = new Table();
        Label clickLabel = new Label("Click the", skin);
        clickLabel.setAlignment(Align.center);
        clickLabel.setFontScale(game.fontScale);

        Label iconLabel = new Label("button after a successful landing", skin);
        iconLabel.setAlignment(Align.center);
        iconLabel.setFontScale(game.fontScale);

        iconHintTable.add(clickLabel);
        iconHintTable.add(saveReplayButton).padLeft(game.fontScale * 5).padRight(game.fontScale * 5);
        iconHintTable.add(iconLabel);

        Label saveLabel = new Label("to save a replay", skin);
        saveLabel.setAlignment(Align.center);
        saveLabel.setFontScale(game.fontScale);

        table.add(noReplayLabel).padBottom(game.fontScale * 30);
        table.row();
        table.add(iconHintTable);
        table.row();
        table.add(saveLabel);
    }

    private void buildReplayRows(Table table, FileHandle[] fileList) {
        Sort sorter = Sort.instance();
        sorter.sort(fileList, new Comparator<FileHandle>() {
            @Override
            public int compare(FileHandle o1, FileHandle o2) {
                if (o1.lastModified() < o2.lastModified()) {
                    // older files to the back
                    return 1;
                } else {
                    // newer files to the front
                    return -1;
                }
            }
        });

        for (final FileHandle replayFile : fileList) {
            final InputReplay replay = JsonUtils.unmarshal(InputReplay.class, replayFile);

            TextButton watchButton = new TextButton("Watch", skin);
            watchButton.getLabel().setFontScale(game.fontScale);

            Label nameLabel = new Label(replayFile.name(), skin);
            nameLabel.setAlignment(Align.center);
            nameLabel.setFontScale(game.fontScale);


            final TextButton deleteButton = new TextButton(" Delete ", skin);
            deleteButton.getLabel().setFontScale(game.fontScale);

            ClickListener watchListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                    Gdx.input.vibrate(10);
                    stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(new GameScreen(game, replay));
                        }
                    }));
                }
            };

            final ClickListener confirmListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                    Gdx.input.vibrate(10);
                    com.bitdecay.helm.persist.ReplayUtils.deleteReplay(replayFile);
                    game.setScreen(new ReplaySelectScreen(game));
                }
            };

            ClickListener firstDeleteListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                    Gdx.input.vibrate(10);
                    deleteButton.clearListeners();
                    deleteButton.getLabel().setText("Confirm");
                    event.getTarget().addListener(confirmListener);
                }
            };
            deleteButton.addListener(firstDeleteListener);

            watchButton.addListener(watchListener);
            nameLabel.addListener(watchListener);

            table.add(watchButton).expand(false, false).padRight(game.fontScale * 10);
            table.add(nameLabel);
            table.add(deleteButton).expand(false, false).padLeft(game.fontScale * 10);
            table.row().padTop(game.fontScale * 10);
        }
    }

    @Override
    public ClickListener getReturnButtonAction() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(TitleScreen.get(game));
                    }
                }));
            }
        };
    }
}
