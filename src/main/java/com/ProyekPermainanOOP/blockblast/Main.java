package com.ProyekPermainanOOP.blockblast;

import com.ProyekPermainanOOP.blockblast.audio.BackgroundMusicPlayer;
import com.ProyekPermainanOOP.blockblast.audio.SoundEffectManager;
import com.ProyekPermainanOOP.blockblast.command.PlaceBlockCommand;
import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.graphics.GameAssets;
import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.model.ClearResult;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.ProyekPermainanOOP.blockblast.model.PusakaCompendium;
import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.ProyekPermainanOOP.blockblast.scoring.ScoreManager;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Main extends ApplicationAdapter {

    private static final float GAME_WIDTH = 720f;
    private static final float GAME_HEIGHT = 1000f;
    private static final int STARTING_LIVES = 3;
    private static final int MAX_UNDO_USES = 2;

    private ExtendViewport viewport;
    private BoardRenderer boardRenderer;
    private BlockTray blockTray;
    private Board board;
    private DragDropManager dragDropManager;
    private ScoreManager scoreManager;
    private BackgroundMusicPlayer musicPlayer;
    private SoundEffectManager soundManager;
    private LocalizationManager localization;
    private PusakaCompendium compendium;

    private PlaceBlockCommand lastCommand;
    private GameMode currentMode;

    private int lives;
    private float gameTimerSeconds;
    private boolean gameOver;
    private boolean compendiumOpen;
    private int undoRemaining;

    private int questTargetTileId;
    private int questClearedCount;
    private int questGoalCount;

    @Override
    public void create() {
        viewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT);
        boardRenderer = new BoardRenderer();
        board = new Board();
        blockTray = new BlockTray(board);
        dragDropManager = new DragDropManager();
        scoreManager = ScoreManager.getInstance();
        musicPlayer = new BackgroundMusicPlayer();
        soundManager = SoundEffectManager.getInstance();
        localization = LocalizationManager.getInstance();
        compendium = PusakaCompendium.getInstance();

        currentMode = GameMode.CLASSIC;
        compendiumOpen = false;
        initSessionState();

        try {
            GameAssets.load();
        } catch (Exception e) {
            System.out.println("Peringatan aset grafis: " + e.getMessage());
        }

        triggerAllTraySpawnAnimations();
        centerViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setupInputProcessor();
        checkNoMovesCondition();
    }

    private void initSessionState() {
        lives = STARTING_LIVES;
        gameOver = false;
        lastCommand = null;
        undoRemaining = MAX_UNDO_USES;

        if (currentMode == GameMode.BLITZ) {
            gameTimerSeconds = 60f;
        } else if (currentMode == GameMode.QUEST) {
            gameTimerSeconds = 90f;
            questTargetTileId = MathUtils.random(1, 10);
            questClearedCount = 0;
            questGoalCount = 12;
        } else {
            gameTimerSeconds = 0f;
        }
    }

    private void cycleGameMode() {
        GameMode[] modes = GameMode.values();
        currentMode = modes[(currentMode.ordinal() + 1) % modes.length];
        restartGame();
        boardRenderer.spawnFloatingText("MODE: " + localization.getModeName(currentMode), BoardRenderer.BOARD_X + 180f,
                BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f + 40f, Color.GOLD);
    }

    private void performUndo() {
        if (lastCommand == null || undoRemaining <= 0 || gameOver) {
            return;
        }
        dragDropManager.cancelDrag();
        lastCommand.undo(board, blockTray);
        scoreManager.restoreState(lastCommand.getPreviousScore(), lastCommand.getPreviousCombo());
        questClearedCount = lastCommand.getPreviousQuestCount();
        lastCommand = null;
        undoRemaining--;
        soundManager.playUndoSound();
        boardRenderer.spawnFloatingText("BATAL!", BoardRenderer.BOARD_X + 260f,
                BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f, Color.CYAN);
    }

    private void centerViewport(int width, int height) {
        viewport.update(width, height, false);
        viewport.getCamera().position.set(GAME_WIDTH / 2f, GAME_HEIGHT / 2f, 0f);
        viewport.getCamera().update();
    }

    private void triggerAllTraySpawnAnimations() {
        for (int i = 0; i < blockTray.getSlotCount(); i++) {
            if (blockTray.hasBlockAt(i)) {
                boardRenderer.notifySlotSpawned(i);
            }
        }
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));

                if (localization.isLanguageMenuOpen()) {
                    handleLanguageMenuClick(worldPos.x, worldPos.y);
                    return true;
                }

                if (compendiumOpen) {
                    handleCompendiumClick(worldPos.x, worldPos.y);
                    return true;
                }

                if (handleRightInfoCardClick(worldPos.x, worldPos.y)) {
                    return true;
                }

                if (isCollectionBottomRightClicked(worldPos.x, worldPos.y)) {
                    dragDropManager.cancelDrag();
                    compendiumOpen = true;
                    return true;
                }

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
                if (localization.isLanguageMenuOpen() || compendiumOpen || !dragDropManager.isDragging()) {
                    return false;
                }
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
                dragDropManager.updateDragPosition(worldPos.x, worldPos.y, board);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (localization.isLanguageMenuOpen() || compendiumOpen || !dragDropManager.isDragging()) {
                    return false;
                }
                Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
                Block placedBlock = dragDropManager.getActiveBlock();
                int usedSlot = dragDropManager.getSelectedSlotIndex();




                dragDropManager.updateDragPosition(worldPos.x, worldPos.y, board);
                if (placedBlock != null && dragDropManager.isPreviewValid()) {
                    PlaceBlockCommand pendingCommand = new PlaceBlockCommand(
                            board,
                            blockTray,
                            scoreManager.getCurrentScore(),
                            scoreManager.getComboMultiplier(),
                            questClearedCount
                    );

                    if (board.placeBlock(placedBlock, dragDropManager.getPreviewRow(), dragDropManager.getPreviewCol())) {
                        dragDropManager.cancelDrag();
                        lastCommand = pendingCommand;

                        ClearResult clearResult = board.clearLines();
                        int basePoints = placedBlock.getCellCount() * 10 + clearResult.getLinesCleared() * 150;
                        int prevScore = scoreManager.getCurrentScore();
                        scoreManager.addScore(basePoints, clearResult.hasClearedLines());
                        int gained = scoreManager.getCurrentScore() - prevScore;

                        if (clearResult.hasClearedLines()) {
                            boardRenderer.triggerLineClearAnimation(clearResult.getClearedCells(), gained);
                            soundManager.playClearSound(scoreManager.getComboMultiplier());
                            if (clearResult.isSpecialTriggered()) {
                                soundManager.playPowerUpSound();
                            }

                            if (clearResult.hasSynergy()) {
                                if (clearResult.getSynergyBonusScore() > 0) {
                                    scoreManager.addScore(clearResult.getSynergyBonusScore(), false);
                                }
                                if (clearResult.getSynergyBonusLives() > 0) {
                                    lives = Math.min(STARTING_LIVES, lives + clearResult.getSynergyBonusLives());
                                }
                                boardRenderer.spawnFloatingText(
                                        clearResult.getSynergyName(),
                                        BoardRenderer.BOARD_X + 110f,
                                        BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f + 60f,
                                        Color.GOLD
                                );
                            }

                            int currentScoreNow = scoreManager.getCurrentScore();
                            if ((currentMode == GameMode.CLASSIC || currentMode == GameMode.QUEST)
                                    && (currentScoreNow / 1500) > (prevScore / 1500)) {
                                if (board.sealRandomOccupiedTile()) {
                                    boardRenderer.spawnFloatingText(
                                            "UBIN TERSEGEL!",
                                            BoardRenderer.BOARD_X + 200f,
                                            BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f - 40f,
                                            Color.ORANGE
                                    );
                                }
                            }

                            int[] clearedVals = clearResult.getClearedTileValues();
                            compendium.recordClearedTiles(clearedVals);

                            if (currentMode == GameMode.BLITZ) {
                                gameTimerSeconds += 5f;
                                boardRenderer.spawnFloatingText("+5d!", worldPos.x + 20f, worldPos.y + 70f, Color.CYAN);
                            } else if (currentMode == GameMode.QUEST) {
                                for (int encoded : clearedVals) {
                                    if (SpecialTileType.extractBaseTileId(encoded) == questTargetTileId) {
                                        questClearedCount++;
                                    }
                                }
                                if (questClearedCount >= questGoalCount) {
                                    scoreManager.addScore(1000, true);
                                    questTargetTileId = MathUtils.random(1, 10);
                                    questClearedCount = 0;
                                    questGoalCount += 4;
                                    gameTimerSeconds += 25f;
                                    boardRenderer.spawnFloatingText("MISI SELESAI +1000!", BoardRenderer.BOARD_X + 120f,
                                            BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f + 20f, Color.GOLD);
                                }
                            }
                        } else {
                            boardRenderer.spawnFloatingText("+" + gained, worldPos.x - 24f, worldPos.y + 40f, Color.WHITE);
                            soundManager.playPlaceSound();
                        }

                        blockTray.takeBlock(usedSlot);
                        if (blockTray.isTrayEmpty()) {
                            blockTray.refillTray(board);
                            triggerAllTraySpawnAnimations();
                        }

                        checkNoMovesCondition();
                    } else {
                        dragDropManager.cancelDrag();
                    }
                } else {
                    dragDropManager.cancelDrag();
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.L) {
                    dragDropManager.cancelDrag();
                    compendiumOpen = false;
                    localization.toggleLanguageMenu();
                    return true;
                }
                if (keycode == Input.Keys.K) {
                    dragDropManager.cancelDrag();
                    localization.setLanguageMenuOpen(false);
                    compendiumOpen = !compendiumOpen;
                    return true;
                }
                if (localization.isLanguageMenuOpen()) {
                    if (keycode == Input.Keys.UP) {
                        localization.moveSelection(-1);
                        return true;
                    }
                    if (keycode == Input.Keys.DOWN) {
                        localization.moveSelection(1);
                        return true;
                    }
                    if (keycode == Input.Keys.LEFT) {
                        localization.moveSelection(-BoardRenderer.LANG_ROWS);
                        return true;
                    }
                    if (keycode == Input.Keys.RIGHT) {
                        localization.moveSelection(BoardRenderer.LANG_ROWS);
                        return true;
                    }
                    if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
                        localization.setLanguageByIndex(keycode - Input.Keys.NUM_1);
                        localization.setLanguageMenuOpen(false);
                        return true;
                    }
                    if (keycode == Input.Keys.NUM_0) {
                        localization.setLanguageByIndex(9);
                        localization.setLanguageMenuOpen(false);
                        return true;
                    }
                    if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.ENTER) {
                        localization.setLanguageMenuOpen(false);
                        return true;
                    }
                    return true;
                }
                if (compendiumOpen) {
                    if (keycode == Input.Keys.LEFT) {
                        compendium.previousPage();
                        return true;
                    }
                    if (keycode == Input.Keys.RIGHT) {
                        compendium.nextPage();
                        return true;
                    }
                    if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.ENTER) {
                        compendiumOpen = false;
                    }
                    return true;
                }
                if (keycode == Input.Keys.G) {
                    cycleGameMode();
                    return true;
                }
                if (keycode == Input.Keys.Z) {
                    performUndo();
                    return true;
                }
                if (keycode == Input.Keys.R) {
                    restartGame();
                    return true;
                }
                if (keycode == Input.Keys.M && musicPlayer != null) {
                    musicPlayer.toggleMute();
                    soundManager.setEnabled(!musicPlayer.isMuted());
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

    private boolean handleRightInfoCardClick(float x, float y) {
        float rightX = BoardRenderer.getRightPanelX(viewport.getWorldWidth());
        float infoY = BoardRenderer.getInfoRightCardY();
        if (x >= rightX && x <= rightX + BoardRenderer.SIDE_PANEL_W
                && y >= infoY && y <= infoY + BoardRenderer.INFO_CARD_H) {
            float relY = y - infoY;
            if (relY < 64f) {
                dragDropManager.cancelDrag();
                localization.setLanguageMenuOpen(true);
            } else if (relY < 114f) {
                performUndo();
            } else if (relY < 164f) {
                cycleGameMode();
            }
            return true;
        }
        return false;
    }

    private void handleCompendiumClick(float x, float y) {
        if (y >= BoardRenderer.COMP_NAV_BTN_Y && y <= BoardRenderer.COMP_NAV_BTN_Y + BoardRenderer.COMP_NAV_BTN_H) {
            if (x >= BoardRenderer.COMP_PREV_BTN_X && x <= BoardRenderer.COMP_PREV_BTN_X + BoardRenderer.COMP_NAV_BTN_W) {
                compendium.previousPage();
                return;
            }
            if (x >= BoardRenderer.COMP_NEXT_BTN_X && x <= BoardRenderer.COMP_NEXT_BTN_X + BoardRenderer.COMP_NAV_BTN_W) {
                compendium.nextPage();
                return;
            }
        }
        if (x < BoardRenderer.COMPENDIUM_X || x > BoardRenderer.COMPENDIUM_X + BoardRenderer.COMPENDIUM_W
                || y < BoardRenderer.COMPENDIUM_Y || y > BoardRenderer.COMPENDIUM_Y + BoardRenderer.COMPENDIUM_H) {
            compendiumOpen = false;
        }
    }

    private boolean isCollectionBottomRightClicked(float x, float y) {
        float screenRightX = (GAME_WIDTH + viewport.getWorldWidth()) * 0.5f;
        return y >= 0f && y <= 78f && x >= screenRightX - 340f && x <= screenRightX;
    }

    private void handleLanguageMenuClick(float x, float y) {
        int count = LocalizationManager.Language.values().length;
        for (int i = 0; i < count; i++) {
            float btnX = BoardRenderer.getLanguageButtonX(i);
            float btnY = BoardRenderer.getLanguageButtonY(i);
            if (x >= btnX && x <= btnX + BoardRenderer.LANG_BTN_W
                    && y >= btnY && y <= btnY + BoardRenderer.LANG_BTN_H) {
                localization.setLanguageByIndex(i);
                localization.setLanguageMenuOpen(false);
                return;
            }
        }
        localization.setLanguageMenuOpen(false);
    }

    private int findTraySlotAt(float worldX, float worldY) {
        int slotCount = blockTray.getSlotCount();
        float slotWidth = BoardRenderer.BOARD_PX / slotCount;
        float hitHeight = 164f;
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
                soundManager.playLifeLostSound();
                blockTray.resetTray(board);
                triggerAllTraySpawnAnimations();
                boardRenderer.spawnFloatingText(localization.getNewTrayAlert(), BoardRenderer.BOARD_X + 140f,
                        BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f, Color.SALMON);
                if (!blockTray.hasAnyValidPlacement(board)) {
                    gameOver = true;
                }
            } else {
                lives = 0;
                soundManager.playLifeLostSound();
                gameOver = true;
            }
        }
    }

    private void restartGame() {
        dragDropManager.cancelDrag();
        board.reset();
        blockTray.resetTray(board);
        triggerAllTraySpawnAnimations();
        scoreManager.resetScore();
        initSessionState();
        checkNoMovesCondition();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        boolean paused = gameOver || localization.isLanguageMenuOpen() || compendiumOpen;
        if (!paused) {
            if (currentMode == GameMode.CLASSIC) {
                gameTimerSeconds += delta;
            } else {
                gameTimerSeconds -= delta;
                if (gameTimerSeconds <= 0f) {
                    gameTimerSeconds = 0f;
                    gameOver = true;
                }
            }
        }
        boardRenderer.updateAnimations(delta);

        viewport.apply();
        viewport.getCamera().position.set(GAME_WIDTH / 2f, GAME_HEIGHT / 2f, 0f);
        viewport.getCamera().update();

        Gdx.gl.glClearColor(0.05f, 0.22f, 0.17f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int selectedSlot = dragDropManager.isDragging() ? dragDropManager.getSelectedSlotIndex() : -1;
        int[][] gridSnapshot = board.getGridSnapshot();
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        boardRenderer.begin(viewport.getCamera().combined);
        boardRenderer.drawBackground(worldW, worldH);
        boardRenderer.end();

        boardRenderer.drawBackgroundFlyingTiles(
                viewport.getCamera().combined,
                worldW,
                worldH,
                scoreManager.getComboMultiplier(),
                scoreManager.getCurrentScore()
        );

        boardRenderer.begin(viewport.getCamera().combined);
        boardRenderer.drawHudPanels(worldW);
        boardRenderer.drawBoard(gridSnapshot);
        boardRenderer.drawPlacementGuidance(gridSnapshot, dragDropManager, localization.isLanguageMenuOpen() || compendiumOpen);
        boardRenderer.drawTray(blockTray, selectedSlot);
        boardRenderer.drawDraggedBlock(dragDropManager);
        boardRenderer.drawClearFlashes();
        boardRenderer.drawModalShapes(compendiumOpen);
        boardRenderer.end();

        boardRenderer.drawOverlay(
                viewport.getCamera().combined,
                worldW,
                worldH,
                gridSnapshot,
                blockTray,
                dragDropManager,
                scoreManager.getCurrentScore(),
                scoreManager.getHighScore(),
                scoreManager.getComboMultiplier(),
                lives,
                gameTimerSeconds,
                musicPlayer != null && musicPlayer.isMuted(),
                gameOver,
                currentMode,
                questTargetTileId,
                questClearedCount,
                questGoalCount,
                undoRemaining,
                compendiumOpen
        );
    }

    @Override
    public void resize(int width, int height) {
        centerViewport(width, height);
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