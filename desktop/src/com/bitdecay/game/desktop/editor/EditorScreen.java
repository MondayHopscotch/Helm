package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.desktop.editor.file.FileUtils;
import com.bitdecay.game.desktop.editor.mode.DeleteFocusMouseMode;
import com.bitdecay.game.desktop.editor.mode.DeleteSegmentMouseMode;
import com.bitdecay.game.desktop.editor.mode.FocusPointMouseMode;
import com.bitdecay.game.desktop.editor.mode.LandingPlatMouseMode;
import com.bitdecay.game.desktop.editor.mode.LineSegmentMouseMode;
import com.bitdecay.game.desktop.editor.mode.MouseMode;
import com.bitdecay.game.desktop.editor.mode.NoOpMouseMode;
import com.bitdecay.game.desktop.editor.mode.StartPointMouseMode;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * Created by Monday on 1/1/2017.
 */
public class EditorScreen extends InputAdapter implements Screen {

    public static final int cellSize = 25;

    private static final int CAM_MOVE_SPEED = 5;
    private static final float CAM_MAX_ZOOM = 20;
    private static final float CAM_MIN_ZOOM = .2f;
    private static final float CAM_ZOOM_SPEED_SLOW = .05f;
    private static final float CAM_ZOOM_SPEED_FAST = .2f;
    private static final int ZOOM_THRESHOLD = 5;

    Stage stage;
    Skin skin;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font/bit.fnt"));

    OrthographicCamera camera;
    ShapeRenderer shaper;
    SpriteBatch batch;

    private Map<OptionsMode, MouseMode> mouseModes;
    private Map<MouseMode, OptionsMode> reverseMap;
    private MouseMode mouseMode;
    private final NoOpMouseMode noOpMouseMode = new NoOpMouseMode();

    private LevelBuilder builder = new LevelBuilder();
    private HelmEditor editor;
    private Label levelNameLabel;

    public EditorScreen(HelmEditor editor) {
        this.editor = editor;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/skin.json"), new TextureAtlas(Gdx.files.internal("skin/ui.atlas")));

        buildOverlay();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaper = new ShapeRenderer();
        batch = new SpriteBatch();

        mouseMode = noOpMouseMode;
        mouseModes = new HashMap<>();
        reverseMap = new HashMap<>();

        addTool(OptionsMode.DRAW_LINE, new LineSegmentMouseMode(builder));
        addTool(OptionsMode.DELETE_LINE, new DeleteSegmentMouseMode(builder));
        addTool(OptionsMode.DRAW_LANDING, new LandingPlatMouseMode(builder));
        addTool(OptionsMode.PLACE_START, new StartPointMouseMode(builder));
        addTool(OptionsMode.ADD_FOCUS, new FocusPointMouseMode(builder));
        addTool(OptionsMode.REMOVE_FOCUS, new DeleteFocusMouseMode(builder));
    }

    private void addTool(OptionsMode option, MouseMode mode) {
        mouseModes.put(option, mode);
        reverseMap.put(mode, option);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        updateOverlay();
        refitCamera();
    }

    private void buildOverlay() {
        Table levelNameTable = new Table();
        levelNameTable.setFillParent(true);
        levelNameTable.align(Align.topRight);
        levelNameTable.setOrigin(Align.topRight);

        levelNameLabel = new Label("---", skin);
        levelNameLabel.setOrigin(Align.topRight);
        levelNameLabel.setAlignment(Align.topRight);

        levelNameTable.add(levelNameLabel).padTop(10).padRight(10).padBottom(30);
        levelNameTable.row();

        for (EditorKeys hotKey : EditorKeys.values()) {
            Label hotKeyLabel = new Label(hotKey.getHelp(), skin);
            hotKeyLabel.setColor(Color.GREEN);
            hotKeyLabel.setOrigin(Align.right);
            hotKeyLabel.setAlignment(Align.right);

            levelNameTable.add(hotKeyLabel).padRight(10);
            levelNameTable.row();
        }


        stage.addActor(levelNameTable);
    }

    private void updateOverlay() {
        levelNameLabel.setText(builder.name + " (File: " + FileUtils.lastTouchedFileName + ")");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        camera.update();
        shaper.setProjectionMatrix(camera.combined);

        shaper.begin(ShapeRenderer.ShapeType.Line);
        drawGrid();

        drawCurrentBuilder(shaper);

        mouseMode.render(shaper);

        shaper.end();

        batch.begin();
        renderMisc();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void renderMisc() {
        OptionsMode selectedOption = reverseMap.get(mouseMode);
        if (selectedOption != null) {
            font.draw(batch, selectedOption.label, Gdx.input.getX(), (Gdx.graphics.getHeight() - (Gdx.input.getY() + 20)));
        }
    }

    private void drawCurrentBuilder(ShapeRenderer shaper) {
        shaper.setColor(Color.RED);
        if (builder.lines != null) {
            for (LineSegment line : builder.lines) {
                shaper.line(line.startPoint.x, line.startPoint.y, line.endPoint.x, line.endPoint.y);
            }
        }

        shaper.setColor(Color.WHITE);
        if (builder.startPoint != null) {
            shaper.polygon(new float[]{builder.startPoint.x - 50, builder.startPoint.y - 100, builder.startPoint.x, builder.startPoint.y + 100, builder.startPoint.x + 50, builder.startPoint.y - 100});
        }

        shaper.setColor(Color.GREEN);
        if (builder.landingPlat != null) {
            shaper.rect(builder.landingPlat.x, builder.landingPlat.y, builder.landingPlat.width, builder.landingPlat.height);
        }

        shaper.setColor(Color.PINK);
        if (builder.focusPoints != null) {
            for (Circle focalPoint : builder.focusPoints) {
                shaper.circle(focalPoint.x, focalPoint.y, focalPoint.radius);
            }

        }
    }

    private void handleInput() {

        if (EditorKeys.TEST_LEVEL.isJustPressed()) {
            if (builder.isLevelValid()) {
                editor.testLevel(builder.build());
            }
        }

        if (EditorKeys.PAN_LEFT.isPressed()) {
            camera.translate(-CAM_MOVE_SPEED * camera.zoom, 0);
        } else if (EditorKeys.PAN_RIGHT.isPressed()) {
            camera.translate(CAM_MOVE_SPEED * camera.zoom, 0);
        }
        if (EditorKeys.PAN_UP.isPressed()) {
            camera.translate(0, CAM_MOVE_SPEED * camera.zoom);
        } else if (EditorKeys.PAN_DOWN.isPressed()) {
            camera.translate(0, -CAM_MOVE_SPEED * camera.zoom);
        }

        if (EditorKeys.ZOOM_IN.isPressed()) {
            if (camera.zoom >= ZOOM_THRESHOLD) {
                adjustCamZoom(-CAM_ZOOM_SPEED_FAST);
            } else if (camera.zoom > CAM_MIN_ZOOM) {
                adjustCamZoom(-CAM_ZOOM_SPEED_SLOW);
            }
        } else if (EditorKeys.ZOOM_OUT.isPressed()) {
            if (camera.zoom < ZOOM_THRESHOLD) {
                adjustCamZoom(CAM_ZOOM_SPEED_SLOW);
            } else if (camera.zoom < CAM_MAX_ZOOM) {
                adjustCamZoom(CAM_ZOOM_SPEED_FAST);
            }
        }

        // check mode hotkeys
        for (OptionsMode mode : OptionsMode.values()) {
            if (mode.hotkey != null && mode.hotkey.isJustPressed()) {
                setMode(mode);
            }
        }
    }

    private void adjustCamZoom(float change) {
        Vector2 mouseCoordinates = getMouseCoords();
        camera.zoom += change;
        camera.update();
        mouseCoordinates = mouseCoordinates.sub(getMouseCoords());
        camera.translate(mouseCoordinates.x, mouseCoordinates.y);
    }

    private Vector2 getMouseCoords() {
        return unproject(Gdx.input.getX(), Gdx.input.getY());
    }

    public Vector2 unproject(int x, int y) {
        Vector3 unproj = camera.unproject(new Vector3(x, y, 0));
        return new Vector2((int) unproj.x, (int) unproj.y);
    }

    private void drawGrid() {
        if (camera.zoom >= ZOOM_THRESHOLD) {
            return;
        }

        shaper.setColor(new Color(0, 0, .2f, 1));
        Vector2 topLeft = unproject(-cellSize, -cellSize);
        Vector2 snapTopLeft = Geom.snap((int) topLeft.x, (int) topLeft.y, cellSize);
        Vector2 bottomRight = unproject(Gdx.graphics.getWidth() + cellSize, Gdx.graphics.getHeight() + cellSize);
        Vector2 snapBottomRight = Geom.snap((int) bottomRight.x, (int) bottomRight.y, cellSize);

        for (float x = snapTopLeft.x; x <= snapBottomRight.x; x += cellSize) {
            shaper.line(x, snapTopLeft.y, x, snapBottomRight.y);
        }
        for (float y = snapBottomRight.y; y <= snapTopLeft.y; y += cellSize) {
            shaper.line(snapTopLeft.x, y, snapBottomRight.x, y);
        }
    }

    public void setMode(OptionsMode mode) {
        if (mouseModes.containsKey(mode)) {
            System.out.println("Setting mode: " + mode);
            mouseMode = mouseModes.get(mode);
        } else if (OptionsMode.SAVE_LEVEL.equals(mode)) {
            maybePromptForSave();
        } else if (OptionsMode.LOAD_LEVEL.equals(mode)) {
            maybeLoad();
        } else if (OptionsMode.SET_FUEL.equals(mode)) {
            maybeSetFuel();
        } else if (OptionsMode.SET_NAME.equals(mode)) {
            maybeSetName();
        } else {
            mouseMode = noOpMouseMode;
        }
    }

    private void maybePromptForSave() {
        if (builder.isLevelValid()) {
            LevelDefinition levDef = builder.build();
            String fileName = FileUtils.saveLevelToFile(levDef);
            if (fileName != null) {
                updateOverlay();
            }
        } else {
            // show a warning or something
            JOptionPane.showMessageDialog(null, "Level must contain a start point, end point, and have a name");
        }
    }

    private void maybeLoad() {
        LevelDefinition loadLevel = FileUtils.loadLevelFromFile();
        if (loadLevel != null) {
            builder.setLevel(loadLevel);
            refitCamera();
            updateOverlay();
        }
    }

    private void maybeSetFuel() {
        String result = JOptionPane.showInputDialog(
                null,
                "Level Starting Fuel (Currently: " + builder.startingFuel + ")",
                "Fuel Tool",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == null || "".equals(result)) {
            // do nothing
        } else {
            try {
                int startingFuel = Integer.parseInt(result);
                builder.startingFuel = startingFuel;
            } catch (NumberFormatException e) {
                System.out.println("Cannot parse '" + result + "' as an integer");
            }
        }
    }

    private void maybeSetName() {
        String result = JOptionPane.showInputDialog(
                null,
                "Level Name (Currently: " + builder.name + ")",
                "Level Name Tool",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == null || "".equals(result)) {
            // do nothing
        } else {
            builder.name = result;
            updateOverlay();
        }
    }

    private void refitCamera() {
        float xMin = Float.POSITIVE_INFINITY;
        float xMax = Float.NEGATIVE_INFINITY;
        float yMin = Float.POSITIVE_INFINITY;
        float yMax = Float.NEGATIVE_INFINITY;
        for (LineSegment line : builder.lines) {
            xMin = Math.min(xMin, line.startPoint.x);
            xMin = Math.min(xMin, line.endPoint.x);

            xMax = Math.max(xMax, line.startPoint.x);
            xMax = Math.max(xMax, line.endPoint.x);

            yMin = Math.min(yMin, line.startPoint.y);
            yMin = Math.min(yMin, line.endPoint.y);

            yMax = Math.max(yMax, line.startPoint.y);
            yMax = Math.max(yMax, line.endPoint.y);
        }

        if (builder.startPoint != null) {
            xMin = Math.min(xMin, builder.startPoint.x);
            xMax = Math.max(xMax, builder.startPoint.x);

            yMin = Math.min(yMin, builder.startPoint.y);
            yMax = Math.max(yMax, builder.startPoint.y);
        }

        if (builder.landingPlat != null) {
            xMin = Math.min(xMin, builder.landingPlat.getCenter(new Vector2()).x);
            xMax = Math.max(xMax, builder.landingPlat.getCenter(new Vector2()).x);

            yMin = Math.min(yMin, builder.landingPlat.getCenter(new Vector2()).y);
            yMax = Math.max(yMax, builder.landingPlat.getCenter(new Vector2()).y);
        }

        if (xMin == Float.POSITIVE_INFINITY || xMax == Float.NEGATIVE_INFINITY ||
                yMin == Float.POSITIVE_INFINITY || yMax == Float.NEGATIVE_INFINITY) {
            xMin = -1000;
            xMax = 1000;
            yMin = -1000;
            yMax = 1000;
        }

        float centerX = (xMin + xMax) / 2;
        float centerY = (yMin + yMax) / 2;

        float zoom = (xMax - xMin) / camera.viewportWidth;
        zoom = Math.max(zoom, (yMax - yMin) / camera.viewportHeight);

        camera.position.set(centerX, centerY, 0);
        camera.zoom = zoom + .2f;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseMode.mouseDown(getMouseCoords(), MouseButton.getButton(button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseMode.mouseUp(getMouseCoords(), MouseButton.getButton(button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseMode.mouseDragged(getMouseCoords());
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseMode.mouseMoved(getMouseCoords());
        return super.mouseMoved(screenX, screenY);
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

    }
}
