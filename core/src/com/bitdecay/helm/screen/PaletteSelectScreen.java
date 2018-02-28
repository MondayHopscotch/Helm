package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.IconUtils;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 6/5/2017.
 */

public class PaletteSelectScreen extends AbstractScrollingItemScreen {
    private Map<Color, Pixmap> pixmapCache = new HashMap<>();
    private Image selectedRowImage;

    private static PaletteSelectScreen instance;

    public static PaletteSelectScreen get(Helm game) {
        if (instance == null) {
            instance = new PaletteSelectScreen(game);
        }
        return instance;
    }

    public PaletteSelectScreen(Helm game) {
        super(game);
    }

    private Pixmap getPixmap(Color color) {
        if (!pixmapCache.containsKey(color)) {
            Pixmap pixmap = new Pixmap(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();
            pixmapCache.put(color, pixmap);
        }
        return pixmapCache.get(color);
    }

    @Override
    public void populateRows(Table mainTable) {
        int totalHighScore = com.bitdecay.helm.scoring.ScoreUtils.getTotalHighScore();

        for (com.bitdecay.helm.unlock.palette.PaletteList palette : com.bitdecay.helm.unlock.palette.PaletteList.values()) {
            if (totalHighScore >= palette.pointsForUnlock) {
                buildUnlockedPalette(palette, mainTable, game.palette == palette.palette);
            } else {
                buildLockedPalette(palette, mainTable, totalHighScore);
                return;
            }
            mainTable.row().padTop(game.fontScale * 10);
        }

    }

    private void buildUnlockedPalette(final com.bitdecay.helm.unlock.palette.PaletteList paletteInfo, Table table, boolean currentPalette) {
        final Image checkMark = IconUtils.getCheckMarkIcon();

        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.palette = paletteInfo.palette;
                Helm.prefs.putString(com.bitdecay.helm.prefs.GamePrefs.CHOSEN_PALETTE, paletteInfo.name());
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
        Pixmap pixmap = getPixmap(paletteInfo.palette.get(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND));
        Image backgroundColorImage = new Image(new Texture(pixmap));
        backgroundColorImage.setSize(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(backgroundColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT));
        Image levelColorImage = new Image(new Texture(pixmap));
        levelColorImage.setSize(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(levelColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM));
        Image landingColorImage = new Image(new Texture(pixmap));
        landingColorImage.setSize(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(landingColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER));
        Image fuelColorImage = new Image(new Texture(pixmap));
        fuelColorImage.setSize(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(fuelColorImage);

        pixmap = getPixmap(paletteInfo.palette.get(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY));
        Image shipColorImage = new Image(new Texture(pixmap));
        shipColorImage.setSize(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize); // just stealing this out of convenience
        colorSampleTable.add(shipColorImage);

        table.add(colorSampleTable).expandX();

        addBlankCell(table);
        table.add(checkMark).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize);
        if (currentPalette) {
            selectedRowImage = checkMark;
            checkMark.setVisible(true);
        } else {
            checkMark.setVisible(false);
        }
    }

    private void buildLockedPalette(com.bitdecay.helm.unlock.palette.PaletteList paletteInfo, Table table, int currentPoints) {
        addBlankCell(table);
        Label paletteNameLabel = getNameLabel(paletteInfo);
        paletteNameLabel.setColor(Color.GRAY);
        table.add(paletteNameLabel).expand();
        Label unlockHintLabel = new Label("Score " + Integer.toString(paletteInfo.pointsForUnlock - currentPoints) + " more points", skin);
        unlockHintLabel.setColor(Color.GRAY);
        unlockHintLabel.setAlignment(Align.center);
        unlockHintLabel.setFontScale(game.fontScale);
        table.add(unlockHintLabel).colspan(4); // have this take up the rest of the table
    }

    private Label getNameLabel(com.bitdecay.helm.unlock.palette.PaletteList paletteInfo) {
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

    @Override
    public void dispose() {
        super.dispose();
        for (Pixmap pixmap : pixmapCache.values()) {
            pixmap.dispose();
        }
    }
}
