package com.bitdecay.helm;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.menu.BitImageButton;
import com.bitdecay.helm.prefs.GamePrefs;

/**
 * Created by Monday on 12/21/2016.
 */
public class Version {
    public static String CURRENT_VERSION = "0.9.4";
    public static String INITIAL_RELEASE_VERSION = "1.0.0";

    private static String[] versionChanges = new String[]{
            "Added this change dialog",
            "Level time medals tuned",
            "Level start fuel tuned",
            "Expand palette selection"
    };

    public static boolean isNewVersionSinceLastOpen(Helm helm) {
        if (Helm.debug) {
            return true;
        }

        String lastVersion = helm.prefs.getString(GamePrefs.LAST_OPENED_VERSION, "");
        if ("" == lastVersion) {
            updateLastUsedVersion(helm);
            lastVersion = Version.INITIAL_RELEASE_VERSION;
        }

        if (!Version.CURRENT_VERSION.equals(lastVersion)) {
            return true;
        } else {
            return false;
        }
    }

    public static void updateLastUsedVersion(Helm helm) {
        helm.prefs.putString(GamePrefs.LAST_OPENED_VERSION, Version.CURRENT_VERSION);
    }

    public static Dialog getChangeDialog(final Helm game) {
        Dialog dialog = new Dialog("What's New in " + Version.CURRENT_VERSION, game.skin) {
            @Override
            protected void result(Object object) {
                Version.updateLastUsedVersion(game);
            }
        };
        dialog.getTitleLabel().setFontScale(game.fontScale);
        dialog.getTitleLabel().setAlignment(Align.center);
        dialog.padTop(game.fontScale * 6);
        dialog.setMovable(false);

        Table whatsNewTable = new Table(game.skin);
        whatsNewTable.align(Align.left);

        for (String changeText : versionChanges) {
            Label textLabel = new Label("- " + changeText, game.skin);
            textLabel.setAlignment(Align.left);
            textLabel.setFontScale(game.fontScale * 0.8f);
            whatsNewTable.add(textLabel).align(Align.left);
            whatsNewTable.row();
        }
        dialog.getContentTable().add(whatsNewTable);
        TextureAtlas.AtlasRegion nextIconTexture = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("next_icon");
        TextureRegionDrawable nextLevelIcon = new TextureRegionDrawable(nextIconTexture);
        BitImageButton nextButton = new BitImageButton(nextLevelIcon, nextLevelIcon, game.fontScale * 0.4f, game.skin);
        dialog.getButtonTable().align(Align.right);
        dialog.button(nextButton);
        return dialog;
    }
}
