package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.world.LevelWorld;
import com.bitdecay.game.world.World1;
import com.bitdecay.game.world.World2;
import com.bitdecay.game.world.World3;

/**
 * Created by Monday on 1/10/2017.
 */
public class WorldSelectScreen implements Screen {

    private final Helm game;
    Stage stage;
    Skin skin;

    public WorldSelectScreen(final Helm game) {
        this.game = game;

        stage = new Stage();
        skin = game.skin;

        LevelWorld[] worlds = new LevelWorld[] {
                new World1(),
                new World2(),
                new World3()
        };

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        int totalHighScore = 0;

        Table worldTable = new Table();
        for (LevelWorld world : worlds) {
            totalHighScore += buildWorldRow(world, worldTable);
        }

        Label totalHighScoreLabel = new Label("Total Score: ", skin);
        totalHighScoreLabel.setFontScale(game.fontScale);
        totalHighScoreLabel.setAlignment(Align.right);

        Label totalHighScoreValue = new Label(Integer.toString(totalHighScore), skin);
        totalHighScoreValue.setFontScale(game.fontScale);
        totalHighScoreValue.setAlignment(Align.right);

        worldTable.add(totalHighScoreLabel).colspan(2);
        worldTable.add(totalHighScoreValue);

        mainTable.add(worldTable).width(Gdx.graphics.getWidth() * 0.5f);

        Table returnTable = new Table();
        returnTable.setFillParent(true);
        returnTable.align(Align.bottomRight);
        returnTable.setOrigin(Align.bottomRight);

        Label returnToTitleLabel = new Label("Return to Title Screen", skin);
        returnToTitleLabel.setFontScale(game.fontScale);
        returnToTitleLabel.setAlignment(Align.bottomRight);
        returnToTitleLabel.setOrigin(Align.bottomRight);
        returnToTitleLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        returnTable.add(returnToTitleLabel);

        stage.addActor(mainTable);
        stage.addActor(returnTable);
    }

    private int buildWorldRow(final LevelWorld world, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, world));
            }
        };

        TextButton goButton = new TextButton("Go!", skin);
        goButton.getLabel().setFontScale(game.fontScale);
        goButton.addListener(listener);

        Label worldNameLabel = new Label(world.getWorldName(), skin);
        worldNameLabel.setAlignment(Align.left);
        worldNameLabel.setFontScale(game.fontScale);

//        TextButton worldNameLabel = new TextButton(world.getWorldName(), skin);
//        worldNameLabel.getLabel().setFontScale(game.fontScale);
//        worldNameLabel.align(Align.left);
        worldNameLabel.addListener(listener);

        int worldHighScore = world.getHighScore();

        Label worldScoreLabel = new Label(Integer.toString(worldHighScore), skin);
        worldScoreLabel.setAlignment(Align.right);
        worldScoreLabel.setFontScale(game.fontScale);

        table.add(goButton).expand(false, false);
        table.add(worldNameLabel).expandX();
        table.add(worldScoreLabel);
        table.row().padTop(game.fontScale * 10);
        return worldHighScore;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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
