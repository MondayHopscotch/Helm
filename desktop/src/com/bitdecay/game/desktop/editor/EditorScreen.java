package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 1/1/2017.
 */
public class EditorScreen extends InputAdapter implements Screen {

    public static final int cellSize = 25;

    private static final int CAM_MOVE_SPEED = 5;
    private static final float CAM_MAX_ZOOM = 20;
    private static final float CAM_MIN_ZOOM= .2f;
    private static final float CAM_ZOOM_SPEED_SLOW = .05f;
    private static final float CAM_ZOOM_SPEED_FAST = .2f;
    private static final int ZOOM_THRESHOLD = 5;

    OrthographicCamera camera;
    ShapeRenderer shaper;

    private Map<OptionsMode, MouseMode> mouseModes;
    private MouseMode mouseMode;
    private final NoOpMouseMode noOpMouseMode = new NoOpMouseMode();

    private LevelBuilder builder = new LevelBuilder();

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaper = new ShapeRenderer();

        mouseMode = noOpMouseMode;
        mouseModes = new HashMap<>();
        mouseModes.put(OptionsMode.DRAW_LINE, new LineSegmentMouseMode(builder));
        mouseModes.put(OptionsMode.DRAW_LANDING, new LandingPlatMouseMode(builder));
        mouseModes.put(OptionsMode.PLACE_START, new StartPointMouseMode(builder));

        Gdx.input.setInputProcessor(this);
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
            shaper.circle(builder.startPoint.x, builder.startPoint.y, 100);
        }

        shaper.setColor(Color.GREEN);
        if (builder.landingPlat != null) {
            shaper.rect(builder.landingPlat.x, builder.landingPlat.y, builder.landingPlat.width, builder.landingPlat.height);
        }
    }

    private void handleInput() {
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

        shaper.setColor(Color.NAVY);
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
//            LevelDefinition savedLevel = LevelUtilities.saveLevel(curLevelBuilder, true);
//            if (savedLevel != null) {
//                currentFile = FileUtils.lastTouchedFileName;
//                setLevelBuilder(savedLevel);
//            }
            Json json = new Json();
            json.setElementType(LevelDefinition.class, "levelLines", LineSegment.class);
            String out = json.toJson(builder.build());

            FileHandle level1File = Gdx.files.local("level/level999.json");
            System.out.println(level1File.file().getAbsolutePath());
            level1File.writeBytes(out.getBytes(), false);
        } else if (OptionsMode.LOAD_LEVEL.equals(mode)) {
//            LevelDefinition loadLevel = LevelUtilities.loadLevel();
//            if (loadLevel != null) {
//                currentFile = FileUtils.lastTouchedFileName;
//                setLevelBuilder(loadLevel);
//                setCamToOrigin();
//            }
        } else {
            mouseMode = noOpMouseMode;
        }
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
