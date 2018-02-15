package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.unlock.LiveStat;
import com.bitdecay.helm.unlock.StatDisplayType;
import com.bitdecay.helm.unlock.StatName;

/**
 * Created by Monday on 2/19/2017.
 */

public class StatsScreen extends AbstractScrollingItemScreen {

    public StatsScreen(Helm game) {
        super(game);
        build(false);
    }

    @Override
    public String getTitle() {
        return "Pilot Stats";
    }

    @Override
    public void populateRows(Table table) {
        itemTable.columnDefaults(0).expandX();
        itemTable.columnDefaults(1).width(game.fontScale * 50);
        for (StatName statName : StatName.values()) {
            buildStatRow(statName, table);
        }
    }

    private void buildStatRow(StatName statName, Table table) {
        LiveStat liveStat = Helm.stats.getLiveStat(statName);
        if (statName.displayType.equals(StatDisplayType.HIDE)) {
            return;
        }
        if (statName.displayType.equals(StatDisplayType.ONLY_SHOW_WHEN_PRESENT)) {
            if (!liveStat.hasSetValue(game.prefs)) {
                return;
            }
        }
        Label statNameLabel = new Label(statName.displayName, skin);
        statNameLabel.setAlignment(Align.center);
        statNameLabel.setFontScale(game.fontScale);

        Label statValueLabel = new Label(liveStat.format(), skin);
        statValueLabel.setAlignment(Align.center);
        statValueLabel.setFontScale(game.fontScale);

        table.add(statNameLabel);
        table.add(statValueLabel);
        table.row().padTop(game.fontScale * 10);
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
