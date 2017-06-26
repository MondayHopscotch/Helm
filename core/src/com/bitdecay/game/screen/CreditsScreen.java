package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.credits.CreditsData;
import com.bitdecay.game.persist.JsonUtils;

public class CreditsScreen implements Screen {
    private Helm game;
    private Stage stage = new Stage();

    private final Skin skin;


    public CreditsScreen(final Helm game) {
        this.game = game;

        stage.setDebugAll(Helm.debug);

        skin = game.skin;

        FileHandle creditsFile = Gdx.files.internal("credits.json");

        CreditsData[] loadedCredits = JsonUtils.unmarshal(CreditsData[].class, creditsFile);

        Table container = new Table();
        container.setFillParent(true);

        Label creditTitle = new Label("Credits", skin);
        creditTitle.setColor(Color.GRAY);
        creditTitle.setFontScale(game.fontScale * 1.5f);

        Table creditsTable = new Table();
        creditsTable.setWidth(Gdx.graphics.getWidth() * .75f);

        for (CreditsData loadedCredit : loadedCredits) {
            addCreditSection(creditsTable, loadedCredit);
        }

        ScrollPane scroll = new ScrollPane(creditsTable, skin);

        Table returnTable = new Table();
        returnTable.align(Align.bottomRight);
        returnTable.setOrigin(Align.bottomRight);

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

        returnTable.add(returnButton).padRight(game.fontScale).padBottom(game.fontScale);

        stage.addActor(container);

        container.add(creditTitle).expandX().fillX();
        container.row();
        container.add(scroll).expand().fill();
        container.row();
        container.add(returnTable).expandX().fillX();

        Gdx.input.setInputProcessor(stage);
    }

    private void addCreditSection(Table creditsTable, CreditsData credit) {
        Label sectionHeader = new Label(credit.sectionTitle, skin);
        sectionHeader.setFontScale(game.fontScale * 1.2f);
        sectionHeader.setAlignment(Align.left);
        sectionHeader.setOrigin(Align.left);

        creditsTable.add(sectionHeader).align(Align.left);
        creditsTable.row();

        for (String creditLine : credit.creditLines) {
            Label lineLabel = new Label(creditLine, skin);
            lineLabel.setFontScale(game.fontScale);
            lineLabel.setAlignment(Align.right);
            lineLabel.setOrigin(Align.right);

            creditsTable.add(lineLabel).align(Align.right);
            creditsTable.row().width(Gdx.graphics.getWidth() * .75f);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
