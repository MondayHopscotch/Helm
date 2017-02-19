package com.bitdecay.game.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.unlock.StatName;

/**
 * Created by Monday on 2/19/2017.
 */

public class StatsScreen extends AbstractScrollingItemScreen {

    public StatsScreen(Helm game) {
        super(game);
        build();
    }

    @Override
    public void populateRows(Table table) {
        itemTable.columnDefaults(0).expandX();
        itemTable.columnDefaults(1).width(game.fontScale * 50);
        for (StatName statName : StatName.values()) {
            buildStatRow(statName, table);
            table.row().padTop(game.fontScale * 10);
        }
    }

    private void buildStatRow(StatName statName, Table table) {
        Label statNameLabel = new Label(statName.displayName, skin);
        statNameLabel.setAlignment(Align.center);
        statNameLabel.setFontScale(game.fontScale);

        Label statValueLabel = new Label(Integer.toString(Helm.stats.getCount(statName)), skin);
        statValueLabel.setAlignment(Align.center);
        statValueLabel.setFontScale(game.fontScale);

        table.add(statNameLabel);
        table.add(statValueLabel);
    }

    @Override
    public Actor getReturnButton() {
        TextButton returnButton = new TextButton("Return to Title Screen", skin);
        returnButton.getLabel().setFontScale(game.fontScale);
        returnButton.align(Align.bottomRight);
        returnButton.setOrigin(Align.bottomRight);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });
        return returnButton;
    }
}
