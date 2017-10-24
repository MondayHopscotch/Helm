package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Sort;
import com.bitdecay.helm.input.InputReplay;
import com.bitdecay.helm.persist.JsonUtils;

import java.util.Comparator;

/**
 * Created by Monday on 2/26/2017.
 */
public class ReplaySelectScreen extends AbstractScrollingItemScreen {
    public ReplaySelectScreen(com.bitdecay.helm.Helm game) {
        super(game);
        build(false);
    }

    @Override
    public String getTitle() {
        return "Replays";
    }

    @Override
    public void populateRows(Table table) {
        FileHandle replayDir = Gdx.files.local(com.bitdecay.helm.persist.ReplayUtils.REPLAY_DIR);
        FileHandle[] fileList = replayDir.list();

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
                    com.bitdecay.helm.persist.ReplayUtils.deleteReplay(replayFile);
                    game.setScreen(new ReplaySelectScreen(game));
                }
            };

            ClickListener firstDeleteListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
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
