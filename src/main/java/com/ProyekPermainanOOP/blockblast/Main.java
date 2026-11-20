package com.ProyekPermainanOOP.blockblast;

import com.ProyekPermainanOOP.blockblast.audio.BackgroundMusicPlayer;
import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.graphics.GameAssets;
import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.model.ClearResult;
import com.ProyekPermainanOOP.blockblast.scoring.ScoreManager;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {

    private static final float GAME_WIDTH = 720f;
    private static final float GAME_HEIGHT = 1000f;
    private static final int STARTING_LIVES = 3;

    private FitViewport viewport;
    private BoardRenderer boardRenderer;
    private BlockTray blockTray;
    private Board board;
    private DragDropManager dragDropManager;
    private ScoreManager scoreManager;
    private BackgroundMusicPlayer musicPlayer;

    private int lives;
    private float elapsedSeconds;
    private boolean gameOver;

    @Override
    public void create() {
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        boardRenderer = new BoardRenderer();
        blockTray = new BlockTray();
        board = new Board();
        dragDropManager = new DragDropManager();
        scoreManager = ScoreManager.getInstance();
        musicPlayer = new BackgroundMusicPlayer();

        lives = STARTING_LIVES;
        elapsedSeconds = 0f;
        gameOver = false;

        try {
            GameAssets.load();
        } catch (Exception e) {
            System.out.println("Peringatan aset grafis: " + e.getMessage());
        }

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        setupInputProcessor();
        checkNoMovesCondition();
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
                if (gameOver) {
                    restartGame();
                    return true;
                }

                int slotIndex = findTraySlotAt(worldPos.x, worldPos.y);
                if (slotIndex != -1 && blockTray.hasBlockAt(slotIndex)) {
                    Block selected = blockTray.getBlock(slotIndex);
                    dragDropManager.startDrag(selected, slotIndex, worldPos.x, worldPos.y);
                    dragDropManager.updateDragPosition(worldPos.x, worldPos.y, board);
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!dragDropManager.isDragging()) {
                    return false;
                }
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
                dragDropManager.updateDragPosition(worldPos.x, worldPos.y, board);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (!dragDropManager.isDragging()) {
                    return false;
                }
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
                Block placedBlock = dragDropManager.getActiveBlock();
                boolean placed = dragDropManager.tryDropBlock(board, blockTray, worldPos.x, worldPos.y);

                if (placed && placedBlock != null) {
                    ClearResult clearResult = board.clearLines();
                    int basePoints = placedBlock.getCellCount() * 10 + clearResult.getLinesCleared() * 100;
                    int prevScore = scoreManager.getCurrentScore();
                    scoreManager.addScore(basePoints, clearResult.hasClearedLines());
                    int gained = scoreManager.getCurrentScore() - prevScore;

                    if (clearResult.hasClearedLines()) {
                        boardRenderer.triggerLineClearAnimation(clearResult.getClearedCells(), gained);
                    } else {
                        boardRenderer.spawnFloatingText("+" + gained, worldPos.x - 24f, worldPos.y + 40f, Color.WHITE);
                    }

                    checkNoMovesCondition();
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.R) {
                    restartGame();
                    return true;
                }
                if (keycode == Input.Keys.M && musicPlayer != null) {
                    musicPlayer.toggleMute();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE && dragDropManager.isDragging()) {
                    dragDropManager.cancelDrag();
                    return true;
                }
                return false;
            }
        });
    }

    private int findTraySlotAt(float worldX, float worldY) {
        int slotCount = blockTray.getSlotCount();
        float slotWidth = BoardRenderer.BOARD_PX / slotCount;
        float hitHeight = 150f;
        float minY = BoardRenderer.TRAY_CENTER_Y - hitHeight / 2f;
        float maxY = BoardRenderer.TRAY_CENTER_Y + hitHeight / 2f;

        if (worldY < minY || worldY > maxY) {
            return -1;
        }

        for (int slot = 0; slot < slotCount; slot++) {
            float slotMinX = BoardRenderer.BOARD_X + slot * slotWidth;
            float slotMaxX = slotMinX + slotWidth;
            if (worldX >= slotMinX && worldX <= slotMaxX) {
                return slot;
            }
        }
        return -1;
    }

    private void checkNoMovesCondition() {
        if (!blockTray.hasAnyValidPlacement(board)) {
            if (lives > 1) {
                lives--;
                blockTray.resetTray();
                boardRenderer.spawnFloatingText("NYAWA -1! TRAY BARU", BoardRenderer.BOARD_X + 95f,
                        BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f, Color.SALMON);
                if (!blockTray.hasAnyValidPlacement(board)) {
                    gameOver = true;
                }
            } else {
                lives = 0;
                gameOver = true;
            }
        }
    }

    private void restartGame() {
        dragDropManager.cancelDrag();
        board.reset();
        blockTray.resetTray();
        scoreManager.resetScore();
        lives = STARTING_LIVES;
        elapsedSeconds = 0f;
        gameOver = false;
        checkNoMovesCondition();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (!gameOver) {
            elapsedSeconds += delta;
        }
        boardRenderer.updateAnimations(delta);

        viewport.apply();

        Gdx.gl.glClearColor(0.02f, 0.11f, 0.09f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int selectedSlot = dragDropManager.isDragging() ? dragDropManager.getSelectedSlotIndex() : -1;
        int[][] gridSnapshot = board.getGridSnapshot();

        boardRenderer.begin(viewport.getCamera().combined);
        boardRenderer.drawBackground();
        boardRenderer.drawHudPanels();
        boardRenderer.drawBoard(gridSnapshot);
        boardRenderer.drawDragPreview(dragDropManager);
        boardRenderer.drawTray(blockTray, selectedSlot);
        boardRenderer.drawDraggedBlock(dragDropManager);
        boardRenderer.drawClearFlashes();
        boardRenderer.end();

        boardRenderer.drawOverlay(
                viewport.getCamera().combined,
                gridSnapshot,
                blockTray,
                dragDropManager,
                scoreManager.getCurrentScore(),
                scoreManager.getHighScore(),
                scoreManager.getComboMultiplier(),
                lives,
                elapsedSeconds,
                musicPlayer != null && musicPlayer.isMuted(),
                gameOver
        );
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (musicPlayer != null) {
            musicPlayer.dispose();
        }
        if (boardRenderer != null) {
            boardRenderer.dispose();
        }
        GameAssets.dispose();
        System.out.println("Game ditutup.");
    }
}