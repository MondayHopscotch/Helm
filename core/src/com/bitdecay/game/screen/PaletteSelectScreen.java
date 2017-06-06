package com.bitdecay.game.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.unlock.palette.PaletteList;

/**
 * Created by Monday on 6/5/2017.
 */

public class PaletteSelectScreen extends AbstractScrollingItemScreen {
    public PaletteSelectScreen(Helm game) {
        super(game);
        build(false);
    }

    @Override
    public void populateRows(Table mainTable) {
        for (PaletteList palette : PaletteList.values()) {
            buildPaletteRow(palette, mainTable);
            mainTable.row().padTop(game.fontScale * 10);
        }

    }

    private void buildPaletteRow(final PaletteList paletteInfo, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.palette = paletteInfo.palette;
            }
        };

        TextButton selectButton = new TextButton("Select", skin);
        selectButton.getLabel().setFontScale(game.fontScale);
        selectButton.addListener(listener);

        Label paletteNameLabel = new Label(paletteInfo.name, skin);
        paletteNameLabel.setAlignment(Align.center);
        paletteNameLabel.setFontScale(game.fontScale);
        paletteNameLabel.addListener(listener);

        table.add(selectButton).expand(false, false);
        table.add(paletteNameLabel);
    }

    @Override
    public String getTitle() {
        return "Palette Select";
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
