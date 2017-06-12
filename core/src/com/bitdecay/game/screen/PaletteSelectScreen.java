package com.bitdecay.game.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.IconUtils;
import com.bitdecay.game.menu.MedalUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.unlock.palette.GameColors;
import com.bitdecay.game.unlock.palette.PaletteList;
import com.bitdecay.game.world.WorldInstance;
import com.bitdecay.game.world.WorldUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 6/5/2017.
 */

public class PaletteSelectScreen extends AbstractScrollingItemScreen {
    private Map<Color, Pixmap> pixmapCache = new HashMap<>();
    private Image selectedRowImage;

    public PaletteSelectScreen(Helm game) {
        super(game);
        build(false);
    }

    private Pixmap getPixmap(Color color) {
        if (!pixmapCache.containsKey(color)) {
            Pixmap pixmap = new Pixmap(MedalUtils.imageSize, MedalUtils.imageSize, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();
            pixmapCache.put(color, pixmap);
        }
        return pixmapCache.get(color);
    }

    @Override
    public void populateRows(Table mainTable) {
        Array<WorldInstance> worlds = WorldUtils.getWorlds();
        int totalHighScore = 0;
        for (WorldInstance world : worlds) {
            totalHighScore += world.getHighScore();
        }

        for (PaletteList palette : PaletteList.values()) {
            if (totalHighScore >= palette.pointsForUnlock) {
                buildUnlockedPalette(palette, mainTable, game.palette == palette.palette);
            } else {
                buildLockedPalette(palette, mainTable, totalHighScore);
                return;
            }
            mainTable.row().padTop(game.fontScale * 10);
        }

    }

    private void buildUnlockedPalette(final PaletteList paletteInfo, Table table, boolean currentPalette) {
        final Image checkMark = IconUtils.getCheckMarkIcon();

        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.palette = paletteInfo.palette;
                Helm.prefs.putString(GamePrefs.CHOSEN_PALETTE, paletteInfo.name());
                selectedRowImage.setVisible(false);
                checkMark.setVisible(true);
                selectedRowImage = checkMark;
            }
        };
        Label paletteNameLabel = getNameLabel(paletteInfo);
        TextButton selectButton = getTextButton(listener);
        table.add(selectButton).expand(false, false);
        paletteNameLabel.addListener(listener);
        table.add(paletteNameLabel).expand();
        addBlankCell(table);

        Table colorSampleTable = new Table();
        Pixmap pixmap = getPixmap(paletteInfo.palette.get(GameColors.BACKGROUND));
        Image backgroundColorImage = new Image(new Texture(pixmap));
        backgroundColorImage.setSize(MedalUtils.imageSize, MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(backgroundColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(GameColors.LEVEL_SEGMENT));
        Image levelColorImage = new Image(new Texture(pixmap));
        levelColorImage.setSize(MedalUtils.imageSize, MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(levelColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(GameColors.LANDING_PLATFORM));
        Image landingColorImage = new Image(new Texture(pixmap));
        landingColorImage.setSize(MedalUtils.imageSize, MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(landingColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(GameColors.FUEL_METER));
        Image fuelColorImage = new Image(new Texture(pixmap));
        fuelColorImage.setSize(MedalUtils.imageSize, MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(fuelColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(GameColors.SHIP_BODY));
        Image shipColorImage = new Image(new Texture(pixmap));
        shipColorImage.setSize(MedalUtils.imageSize, MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(shipColorImage);

        table.add(colorSampleTable);

        addBlankCell(table);
        table.add(checkMark).size(MedalUtils.imageSize, MedalUtils.imageSize);
        if (currentPalette) {
            selectedRowImage = checkMark;
            checkMark.setVisible(true);
        } else {
            checkMark.setVisible(false);
        }
    }

    private void buildLockedPalette(PaletteList paletteInfo, Table table, int currentPoints) {
        addBlankCell(table);
        Label paletteNameLabel = getNameLabel(paletteInfo);
        paletteNameLabel.setColor(Color.GRAY);
        table.add(paletteNameLabel).expand();
        Label unlockHintLabel = new Label(Integer.toString(currentPoints) + " points to go", skin);
        unlockHintLabel.setColor(Color.GRAY);
        unlockHintLabel.setAlignment(Align.center);
        unlockHintLabel.setFontScale(game.fontScale);
        table.add(unlockHintLabel).colspan(4); // have this take up the rest of the table
    }

    private Label getNameLabel(PaletteList paletteInfo) {
        Label paletteNameLabel = new Label(paletteInfo.name, skin);
        paletteNameLabel.setAlignment(Align.center);
        paletteNameLabel.setFontScale(game.fontScale);
        return paletteNameLabel;
    }

    private TextButton getTextButton(ClickListener listener) {
        TextButton selectButton = new TextButton("Select", skin);
        selectButton.getLabel().setFontScale(game.fontScale);
        selectButton.addListener(listener);
        return selectButton;
    }

    private void addBlankCell(Table table) {
        table.add(new Image()).width(32);
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

    @Override
    public void dispose() {
        super.dispose();
        for (Pixmap pixmap : pixmapCache.values()) {
            pixmap.dispose();
        }
    }
}
