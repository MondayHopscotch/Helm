package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.credits.CreditsData;
import com.bitdecay.helm.menu.BitImageButton;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;

public class CreditsScreen extends AbstractScrollingItemScreen {
    private static TextureRegion gotoTexture;

    // Some hackery happening here. Goal is to start at the bottom and then scroll up
    // to force the player to realize there is a bunch of stuff in the credits.
    private boolean requestStartAtBottom = true;
    private boolean scrollBackToTop = false;

    public CreditsScreen(final Helm game) {
        super(game);
        initIcons();
        build(false);
    }

    @Override
    public void populateRows(Table mainTable) {
        FileHandle creditsFile = Gdx.files.internal("credits.json");

        CreditsData[] loadedCredits = JsonUtils.unmarshal(CreditsData[].class, creditsFile);

        Label creditTitle = new Label("Credits", skin);
        creditTitle.setColor(Color.GRAY);
        creditTitle.setFontScale(game.fontScale * 1.5f);

        for (CreditsData loadedCredit : loadedCredits) {
            addCreditSection(mainTable, loadedCredit);
        }
    }

    @Override
    public String getTitle() {
        return "Credits";
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

    private void initIcons() {
        gotoTexture = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("next_icon");
    }

    private void addCreditSection(Table creditsTable, CreditsData credit) {
        Label sectionHeader = new Label(credit.sectionTitle, skin);
        sectionHeader.setFontScale(game.fontScale * 1.2f);
        sectionHeader.setAlignment(Align.left);
        sectionHeader.setOrigin(Align.left);

        creditsTable.add(sectionHeader).align(Align.left).padTop(game.fontScale * 10);
        creditsTable.row();

        for (final CreditsData.CreditLine creditLine : credit.creditLines) {
            Label lineLabel = new Label(creditLine.text, skin);
            lineLabel.setFontScale(game.fontScale);
            lineLabel.setAlignment(Align.right);
            lineLabel.setOrigin(Align.right);

            creditsTable.add(lineLabel).align(Align.right).width(Gdx.graphics.getWidth() * .75f);

            if (creditLine.url != null && creditLine.url.length() > 0) {
                ClickListener linkListener = new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Helm.urlOpener.open(creditLine.url);
                    }
                };

                TextureRegionDrawable gotoDrawable = new TextureRegionDrawable(gotoTexture);
                BitImageButton linkButton = new BitImageButton(gotoDrawable, gotoDrawable, game.fontScale * 0.3f, game.skin);

                linkButton.addListener(linkListener);
                lineLabel.addListener(linkListener);

                creditsTable.add(linkButton).padLeft(game.fontScale * 5);
            } else {
                creditsTable.add(new Image());
            }
            creditsTable.row();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (requestStartAtBottom) {
            scroll.setSmoothScrolling(false);
            scroll.setScrollY(1000);
            scroll.setScrollPercentY(1);
            requestStartAtBottom = false;
            scrollBackToTop = true;
        } else if (scrollBackToTop) {
            scroll.setSmoothScrolling(true);
            scroll.setScrollPercentY(0);
            scrollBackToTop = false;
        }
    }
}
