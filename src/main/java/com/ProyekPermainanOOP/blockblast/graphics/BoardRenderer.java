package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.ProyekPermainanOOP.blockblast.model.PusakaCompendium;
import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;

public class BoardRenderer implements Disposable {

    public static final float VIEW_W = 720f;
    public static final float VIEW_H = 1000f;

    public static final int GRID_SIZE = 8;
    public static final float CELL = 64f;
    public static final float BOARD_PX = GRID_SIZE * CELL;
    public static final float BOARD_X = (VIEW_W - BOARD_PX) / 2f;
    public static final float BOARD_Y = 330f;

    public static final float TRAY_TILE_SIZE = 28f;
    public static final float TRAY_CENTER_Y = 145f;

    public static final int LANG_COLS = 2;
    public static final int LANG_ROWS = 10;
    public static final float LANG_MENU_W = 490f;
    public static final float LANG_MENU_H = 476f;
    public static final float LANG_MENU_X = (VIEW_W - LANG_MENU_W) / 2f;
    public static final float LANG_MENU_Y = BOARD_Y + (BOARD_PX - LANG_MENU_H) / 2f;
    public static final float LANG_BTN_W = 222f;
    public static final float LANG_BTN_H = 31f;
    public static final float LANG_COL_GAP = 14f;
    public static final float LANG_ROW_GAP = 5f;
    public static final float LANG_GRID_START_X = LANG_MENU_X + (LANG_MENU_W - (LANG_COLS * LANG_BTN_W + LANG_COL_GAP)) / 2f;
    public static final float LANG_GRID_FIRST_Y = LANG_MENU_Y + LANG_MENU_H - 96f;

    public static final float COMPENDIUM_W = 490f;
    public static final float COMPENDIUM_H = 490f;
    public static final float COMPENDIUM_X = (VIEW_W - COMPENDIUM_W) / 2f;
    public static final float COMPENDIUM_Y = BOARD_Y + (BOARD_PX - COMPENDIUM_H) / 2f;

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont hudFont = new BitmapFont();
    private final BitmapFont bigFont = new BitmapFont();
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private final SideDecorationRenderer sideDecoration = new SideDecorationRenderer();
    private final BoardEffectManager effectManager = new BoardEffectManager();
    private final LocalizationManager localization = LocalizationManager.getInstance();

    private final Color panelTop = Color.valueOf("0F4736");
    private final Color panelBottom = Color.valueOf("062119");
    private final Color emptyTop = Color.valueOf("1A634C");
    private final Color emptyBottom = Color.valueOf("0E3E2F");
    private final Color tileTop = Color.valueOf("FFFDF7");
    private final Color tileBottom = Color.valueOf("EADBB7");
    private final Color goldAccent = Color.valueOf("F3C65F");
    private final Color heartRed = Color.valueOf("FF5C6C");
    private final Color mintAccent = Color.valueOf("7CF5C4");
    private final Color dragonColor = Color.valueOf("38BDF8");
    private final Color wildcardColor = Color.valueOf("34D399");
    private final Color bombColor = Color.valueOf("FB7185");

    private float elapsedAnimTime = 0f;
    private int activeCombo = 1;
    private int latestScore = 0;

    public BoardRenderer() {
        hudFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hudFont.setUseIntegerPositions(false);
        bigFont.setUseIntegerPositions(false);
        hudFont.getData().setScale(1.18f);
        bigFont.getData().setScale(1.95f);
    }

    public static float getLanguageButtonX(int index) {
        int col = index / LANG_ROWS;
        return LANG_GRID_START_X + col * (LANG_BTN_W + LANG_COL_GAP);
    }

    public static float getLanguageButtonY(int index) {
        int row = index % LANG_ROWS;
        return LANG_GRID_FIRST_Y - row * (LANG_BTN_H + LANG_ROW_GAP);
    }

    public void notifySlotSpawned(int slotIndex) {
        effectManager.notifySlotSpawned(slotIndex);
    }

    public void triggerLineClearAnimation(int[][] clearedCells, int earnedPoints) {
        effectManager.triggerLineClearAnimation(clearedCells, earnedPoints, goldAccent);
    }

    public void spawnFloatingText(String text, float x, float y, Color color) {
        effectManager.spawnFloatingText(text, x, y, color);
    }

    public void updateAnimations(float delta) {
        elapsedAnimTime += delta;
        effectManager.update(delta);
    }

    public void begin(Matrix4 projection) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void end() {
        shape.end();
    }

    public void drawBackground(float worldW, float worldH) {
        sideDecoration.drawShapes(shape, worldW, worldH, elapsedAnimTime, activeCombo, effectManager.getClearSurge(), latestScore);
    }

    public void drawHudPanels() {
        float hudY = BOARD_Y + BOARD_PX + 28f;
        float hudH = 88f;
        float gap = 14f;
        float cardW = (BOARD_PX - gap * 2f) / 3f;

        for (int i = 0; i < 3; i++) {
            float x = BOARD_X + i * (cardW + gap);
            shape.rect(x, hudY, cardW, hudH, panelBottom, panelBottom, panelTop, panelTop);
        }

        float trayPanelY = TRAY_CENTER_Y - 66f;
        float trayPanelH = 132f;
        shape.rect(BOARD_X - 10f, trayPanelY, BOARD_PX + 20f, trayPanelH, panelBottom, panelBottom, panelTop, panelTop);
    }

    private void drawSpecialTileBadge(float x, float y, float size, SpecialTileType type) {
        if (type == SpecialTileType.NONE) {
            return;
        }
        float pulse = 0.72f + 0.28f * (float) Math.sin(elapsedAnimTime * 7f);
        Color badgeColor = dragonColor;
        if (type == SpecialTileType.JADE_WILDCARD) {
            badgeColor = wildcardColor;
        } else if (type == SpecialTileType.TALISMAN_BOMB) {
            badgeColor = bombColor;
        }
        shape.setColor(badgeColor.r, badgeColor.g, badgeColor.b, pulse);
        shape.rect(x, y, size, 4f);
        shape.rect(x, y + size - 4f, size, 4f);
        shape.rect(x, y, 4f, size);
        shape.rect(x + size - 4f, y, 4f, size);
        shape.circle(x + size - 7f, y + size - 7f, 4.5f, 20);
    }

    public void drawBoard(int[][] grid) {
        shape.rect(BOARD_X - 10f, BOARD_Y - 10f, BOARD_PX + 20f, BOARD_PX + 20f, panelBottom, panelBottom, panelTop, panelTop);

        float pulse = 0.5f + 0.5f * (float) Math.sin(elapsedAnimTime * 3.0f);
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                float x = BOARD_X + c * CELL;
                float y = BOARD_Y + (GRID_SIZE - 1 - r) * CELL;
                int val = grid[r][c];
                if (val == 0) {
                    Color top = emptyTop.cpy().lerp(panelTop, pulse * 0.25f);
                    shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f, emptyBottom, emptyBottom, top, top);
                } else {
                    shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f, tileBottom, tileBottom, tileTop, tileTop);
                    drawSpecialTileBadge(x + 2f, y + 2f, CELL - 4f, SpecialTileType.fromEncoded(val));
                }
            }
        }
    }

    public void drawPlacementGuidance(int[][] grid, DragDropManager dragDropManager, boolean modalOpen) {
        if (dragDropManager == null || !dragDropManager.isDragging() || modalOpen) {
            return;
        }
        Block block = dragDropManager.getActiveBlock();
        int startRow = dragDropManager.getPreviewRow();
        int startCol = dragDropManager.getPreviewCol();
        if (block == null) {
            return;
        }

        int[][] shapeGrid = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        boolean valid = dragDropManager.isPreviewValid();
        float pulse = 0.5f + 0.5f * (float) Math.sin(elapsedAnimTime * 9f);
        float shake = valid ? 0f : (float) Math.sin(elapsedAnimTime * 36f) * 3.5f;

        int matchingNeighbors = 0;
        if (valid && startRow >= 0 && startCol >= 0) {
            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};
            boolean[][] highlightedMatch = new boolean[GRID_SIZE][GRID_SIZE];

            for (int r = 0; r < block.getRows(); r++) {
                for (int c = 0; c < block.getCols(); c++) {
                    if (shapeGrid[r][c] == 1) {
                        int br = startRow + r;
                        int bc = startCol + c;
                        if (br >= 0 && br < GRID_SIZE && bc >= 0 && bc < GRID_SIZE) {
                            int rawTile = (tileGrid[r][c] > 0) ? tileGrid[r][c] : block.getTileId();
                            int baseTile = SpecialTileType.extractBaseTileId(rawTile);
                            boolean isWild = SpecialTileType.fromEncoded(rawTile) == SpecialTileType.JADE_WILDCARD;

                            for (int d = 0; d < 4; d++) {
                                int nr = br + dr[d];
                                int nc = bc + dc[d];
                                if (nr >= 0 && nr < GRID_SIZE && nc >= 0 && nc < GRID_SIZE && grid[nr][nc] > 0) {
                                    int neighborBase = SpecialTileType.extractBaseTileId(grid[nr][nc]);
                                    boolean neighborWild = SpecialTileType.fromEncoded(grid[nr][nc]) == SpecialTileType.JADE_WILDCARD;
                                    if (isWild || neighborWild || neighborBase == baseTile) {
                                        matchingNeighbors++;
                                        if (!highlightedMatch[nr][nc]) {
                                            highlightedMatch[nr][nc] = true;
                                            float mx = BOARD_X + nc * CELL;
                                            float my = BOARD_Y + (GRID_SIZE - 1 - nr) * CELL;
                                            shape.setColor(1f, 0.84f, 0.22f, 0.32f + 0.25f * pulse);
                                            shape.rect(mx + 1f, my + 1f, CELL - 2f, CELL - 2f);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (shapeGrid[r][c] == 1) {
                    int boardRow = startRow + r;
                    int boardCol = startCol + c;
                    if (boardRow >= 0 && boardRow < GRID_SIZE && boardCol >= 0 && boardCol < GRID_SIZE) {
                        float x = BOARD_X + boardCol * CELL + shake;
                        float y = BOARD_Y + (GRID_SIZE - 1 - boardRow) * CELL;
                        if (valid) {
                            if (matchingNeighbors > 0) {
                                shape.setColor(0.98f, 0.82f, 0.25f, 0.42f + 0.24f * pulse);
                            } else {
                                shape.setColor(0.24f, 0.94f, 0.56f, 0.34f + 0.20f * pulse);
                            }
                        } else {
                            shape.setColor(0.96f, 0.26f, 0.28f, 0.42f + 0.22f * pulse);
                        }
                        shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f);
                    }
                }
            }
        }
    }

    public void drawTray(BlockTray tray, int selectedSlotIndex) {
        if (tray == null) {
            return;
        }
        int slotCount = tray.getSlotCount();
        float slotWidth = BOARD_PX / slotCount;

        for (int slot = 0; slot < slotCount; slot++) {
            if (slot == selectedSlotIndex) {
                continue;
            }
            Block block = tray.getBlock(slot);
            if (block == null || !block.isActive()) {
                continue;
            }
            float scale = effectManager.getSlotSpawnScale(slot);
            float size = TRAY_TILE_SIZE * scale;
            int[][] cells = block.getShape();
            int[][] tileGrid = block.getTileGrid();
            float centerX = BOARD_X + (slot + 0.5f) * slotWidth;
            float startX = centerX - (block.getCols() * size) / 2f;
            float startY = TRAY_CENTER_Y + (block.getRows() * size) / 2f;
            for (int row = 0; row < cells.length; row++) {
                for (int col = 0; col < cells[row].length; col++) {
                    if (cells[row][col] != 0) {
                        float x = startX + col * size;
                        float y = startY - (row + 1) * size;
                        shape.rect(x + 1f, y + 1f, Math.max(2f, size - 2f), Math.max(2f, size - 2f), tileBottom, tileBottom, tileTop, tileTop);
                        drawSpecialTileBadge(x + 1f, y + 1f, Math.max(2f, size - 2f), SpecialTileType.fromEncoded(tileGrid[row][col]));
                    }
                }
            }
        }
    }

    public void drawDraggedBlock(DragDropManager dragDropManager) {
        if (dragDropManager == null || !dragDropManager.isDragging()) {
            return;
        }
        Block block = dragDropManager.getActiveBlock();
        int[][] cells = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        float liftPulse = 1.06f + 0.04f * (float) Math.sin(elapsedAnimTime * 14f);
        float tileW = CELL * liftPulse;
        float centerX = dragDropManager.getTouchX();
        float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY() + 8f;

        float startX = centerX - (block.getCols() * tileW) / 2f;
        float startY = centerY + (block.getRows() * tileW) / 2f;

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (cells[r][c] == 1) {
                    float x = startX + c * tileW;
                    float y = startY - (r + 1) * tileW;
                    shape.rect(x + 2f, y + 2f, tileW - 4f, tileW - 4f, tileBottom, tileBottom, tileTop, tileTop);
                    drawSpecialTileBadge(x + 2f, y + 2f, tileW - 4f, SpecialTileType.fromEncoded(tileGrid[r][c]));
                }
            }
        }
    }

    public void drawClearFlashes() {
        effectManager.drawClearFlashes(shape);
    }

    public void drawModalShapes(boolean compendiumOpen) {
        if (localization.isLanguageMenuOpen()) {
            shape.setColor(0f, 0f, 0f, 0.65f);
            shape.rect(BOARD_X - 10f, BOARD_Y - 10f, BOARD_PX + 20f, BOARD_PX + 20f);

            shape.setColor(goldAccent);
            shape.rect(LANG_MENU_X - 4f, LANG_MENU_Y - 4f, LANG_MENU_W + 8f, LANG_MENU_H + 8f);
            shape.rect(LANG_MENU_X, LANG_MENU_Y, LANG_MENU_W, LANG_MENU_H, panelBottom, panelBottom, panelTop, panelTop);

            LocalizationManager.Language[] languages = LocalizationManager.Language.values();
            LocalizationManager.Language active = localization.getCurrentLanguage();

            for (int i = 0; i < languages.length; i++) {
                float btnX = getLanguageButtonX(i);
                float btnY = getLanguageButtonY(i);
                boolean isSelected = (languages[i] == active);
                shape.setColor(isSelected ? goldAccent : emptyTop);
                shape.rect(btnX, btnY, LANG_BTN_W, LANG_BTN_H);
            }
        } else if (compendiumOpen) {
            shape.setColor(0f, 0f, 0f, 0.68f);
            shape.rect(BOARD_X - 10f, BOARD_Y - 10f, BOARD_PX + 20f, BOARD_PX + 20f);

            shape.setColor(goldAccent);
            shape.rect(COMPENDIUM_X - 4f, COMPENDIUM_Y - 4f, COMPENDIUM_W + 8f, COMPENDIUM_H + 8f);
            shape.rect(COMPENDIUM_X, COMPENDIUM_Y, COMPENDIUM_W, COMPENDIUM_H, panelBottom, panelBottom, panelTop, panelTop);

            List<PusakaCompendium.RelicEntry> entries = PusakaCompendium.getInstance().getEntries();
            for (int i = 0; i < entries.size(); i++) {
                float rowY = COMPENDIUM_Y + COMPENDIUM_H - 96f - i * 36f;
                shape.setColor(entries.get(i).isUnlocked() ? emptyTop : panelBottom);
                shape.rect(COMPENDIUM_X + 18f, rowY, COMPENDIUM_W - 36f, 31f);
            }
        }
    }

    public void drawOverlay(Matrix4 projection, float worldW, float worldH, int[][] grid, BlockTray tray,
                            DragDropManager dragDropManager, int score, int highScore, int combo, int lives,
                            float elapsedSeconds, boolean muted, boolean gameOver,
                            GameMode gameMode, int questTileId, int questCurrent, int questTarget,
                            int undoRemaining, boolean compendiumOpen) {
        this.activeCombo = Math.max(1, combo);
        this.latestScore = score;

        batch.setProjectionMatrix(projection);
        batch.begin();

        sideDecoration.drawFlyingTiles(batch, worldW, worldH, elapsedAnimTime, activeCombo, effectManager.getClearSurge());
        effectManager.drawBurstTiles(batch);
        drawBoardAndTrayTiles(grid, tray, dragDropManager);
        drawHudTexts(worldW, score, highScore, combo, lives, elapsedSeconds, muted, gameOver,
                gameMode, questTileId, questCurrent, questTarget, undoRemaining, compendiumOpen);
        effectManager.drawFloatingTexts(batch, bigFont);
        drawLanguageModalTexts();
        drawCompendiumModalTexts(compendiumOpen);

        batch.end();
    }

    private void drawBoardAndTrayTiles(int[][] grid, BlockTray tray, DragDropManager dragDropManager) {
        if (GameAssets.chineseCharTiles == null || GameAssets.chineseCharTiles.length == 0) {
            return;
        }

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                int tileId = grid[r][c];
                if (tileId > 0) {
                    TextureRegion region = getTileRegion(tileId);
                    if (region != null) {
                        float x = BOARD_X + c * CELL + 6f;
                        float y = BOARD_Y + (GRID_SIZE - 1 - r) * CELL + 6f;
                        batch.draw(region, x, y, CELL - 12f, CELL - 12f);
                    }
                }
            }
        }

        if (tray != null) {
            int slotCount = tray.getSlotCount();
            float slotWidth = BOARD_PX / slotCount;
            int selectedSlot = (dragDropManager != null && dragDropManager.isDragging())
                    ? dragDropManager.getSelectedSlotIndex()
                    : -1;

            for (int slot = 0; slot < slotCount; slot++) {
                if (slot == selectedSlot) {
                    continue;
                }
                Block block = tray.getBlock(slot);
                if (block == null || !block.isActive()) {
                    continue;
                }
                float scale = effectManager.getSlotSpawnScale(slot);
                float size = TRAY_TILE_SIZE * scale;
                int[][] tileGrid = block.getTileGrid();
                float startX = BOARD_X + (slot + 0.5f) * slotWidth - (block.getCols() * size) / 2f;
                float startY = TRAY_CENTER_Y + (block.getRows() * size) / 2f;

                for (int row = 0; row < block.getRows(); row++) {
                    for (int col = 0; col < block.getCols(); col++) {
                        int tileId = tileGrid[row][col];
                        if (tileId > 0) {
                            TextureRegion region = getTileRegion(tileId);
                            if (region != null) {
                                float pad = Math.max(1.5f, 3f * scale);
                                float x = startX + col * size + pad;
                                float y = startY - (row + 1) * size + pad;
                                batch.draw(region, x, y, Math.max(2f, size - pad * 2f), Math.max(2f, size - pad * 2f));
                            }
                        }
                    }
                }
            }
        }

        if (dragDropManager != null && dragDropManager.isDragging()) {
            Block block = dragDropManager.getActiveBlock();
            int[][] tileGrid = block.getTileGrid();
            float liftPulse = 1.06f + 0.04f * (float) Math.sin(elapsedAnimTime * 14f);
            float tileW = CELL * liftPulse;
            float centerX = dragDropManager.getTouchX();
            float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY() + 8f;
            float startX = centerX - (block.getCols() * tileW) / 2f;
            float startY = centerY + (block.getRows() * tileW) / 2f;

            for (int r = 0; r < block.getRows(); r++) {
                for (int c = 0; c < block.getCols(); c++) {
                    int tileId = tileGrid[r][c];
                    if (tileId > 0) {
                        TextureRegion region = getTileRegion(tileId);
                        if (region != null) {
                            float x = startX + c * tileW + 6f;
                            float y = startY - (r + 1) * tileW + 6f;
                            batch.draw(region, x, y, tileW - 12f, tileW - 12f);
                        }
                    }
                }
            }
        }
    }

    private void drawHudTexts(float worldW, int score, int highScore, int combo, int lives, float elapsedSeconds,
                              boolean muted, boolean gameOver, GameMode gameMode, int questTileId,
                              int questCurrent, int questTarget, int undoRemaining, boolean compendiumOpen) {
        float hudY = BOARD_Y + BOARD_PX + 28f;
        float hudH = 88f;
        float gap = 14f;
        float cardW = (BOARD_PX - gap * 2f) / 3f;

        hudFont.getData().setScale(1.18f);
        hudFont.setColor(goldAccent);
        hudFont.draw(batch, localization.getScoreLabel(), BOARD_X + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, String.valueOf(score), BOARD_X + 14f, hudY + 42f);

        float midX = BOARD_X + cardW + gap;
        hudFont.setColor(heartRed);
        hudFont.draw(batch, localization.getLivesLabel(), midX + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, lives + " / 3", midX + 14f, hudY + 42f);

        float rightX = BOARD_X + (cardW + gap) * 2f;
        int totalSec = Math.max(0, (int) elapsedSeconds);
        String timeStr = String.format("%02d:%02d", totalSec / 60, totalSec % 60);
        hudFont.setColor(mintAccent);
        hudFont.draw(batch, localization.getTimeLabel(), rightX + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, timeStr, rightX + 14f, hudY + 42f);

        hudFont.getData().setScale(0.95f);
        hudFont.setColor(Color.WHITE);
        String topCenterText = localization.formatCenterTopBar(highScore, combo, gameMode, undoRemaining);
        glyphLayout.setText(hudFont, topCenterText);
        float centerTopX = (VIEW_W - glyphLayout.width) * 0.5f;
        hudFont.draw(batch, topCenterText, centerTopX, VIEW_H - 18f);

        float screenLeftX = (VIEW_W - worldW) * 0.5f + 22f;
        float screenRightX = (VIEW_W + worldW) * 0.5f - 22f;

        hudFont.getData().setScale(1.28f);
        hudFont.setColor(goldAccent);
        String bottomLeftText = localization.formatBottomLeftControls(muted, gameMode, questCurrent, questTarget);
        hudFont.draw(batch, bottomLeftText, screenLeftX, 42f);
        if (gameMode == GameMode.QUEST) {
            glyphLayout.setText(hudFont, bottomLeftText);
            TextureRegion questReg = getTileRegion(questTileId);
            if (questReg != null) {
                batch.draw(questReg, screenLeftX + glyphLayout.width + 10f, 22f, 24f, 24f);
            }
        }

        hudFont.getData().setScale(1.35f);
        hudFont.setColor(mintAccent);
        String collectionText = localization.formatBottomRightCollection(PusakaCompendium.getInstance().getUnlockedCount(), 10);
        glyphLayout.setText(hudFont, collectionText);
        hudFont.draw(batch, collectionText, screenRightX - glyphLayout.width, 43f);

        hudFont.getData().setScale(1.18f);

        if (gameOver && !localization.isLanguageMenuOpen() && !compendiumOpen) {
            bigFont.setColor(Color.WHITE);
            bigFont.draw(batch, localization.getGameOverTitle(), BOARD_X + 95f, BOARD_Y + BOARD_PX / 2f + 24f);
            hudFont.setColor(goldAccent);
            hudFont.draw(batch, localization.getGameOverSubtitle(), BOARD_X + 115f, BOARD_Y + BOARD_PX / 2f - 18f);
        }
    }

    private void drawLanguageModalTexts() {
        if (!localization.isLanguageMenuOpen()) {
            return;
        }
        LocalizationManager.Language[] languages = LocalizationManager.Language.values();
        LocalizationManager.Language active = localization.getCurrentLanguage();

        bigFont.setColor(goldAccent);
        bigFont.draw(batch, localization.getLanguageMenuTitle() + " (20)", LANG_MENU_X + 76f, LANG_MENU_Y + LANG_MENU_H - 18f);

        hudFont.getData().setScale(0.95f);
        for (int i = 0; i < languages.length; i++) {
            float btnX = getLanguageButtonX(i);
            float btnY = getLanguageButtonY(i);
            boolean isSelected = (languages[i] == active);
            hudFont.setColor(isSelected ? panelBottom : Color.WHITE);
            String label = (i + 1) + ". " + languages[i].getDisplayName() + (isSelected ? " *" : "");
            hudFont.draw(batch, label, btnX + 10f, btnY + 21f);
        }

        hudFont.setColor(Color.LIGHT_GRAY);
        hudFont.draw(batch, localization.getLanguageMenuHint(), LANG_MENU_X + 36f, LANG_MENU_Y + 24f);
        hudFont.getData().setScale(1.18f);
    }

    private void drawCompendiumModalTexts(boolean compendiumOpen) {
        if (!compendiumOpen) {
            return;
        }
        boolean isIndo = localization.getCurrentLanguage() == LocalizationManager.Language.INDONESIA
                || localization.getCurrentLanguage() == LocalizationManager.Language.MELAYU
                || localization.getCurrentLanguage() == LocalizationManager.Language.JAWA
                || localization.getCurrentLanguage() == LocalizationManager.Language.SUNDA;

        bigFont.setColor(goldAccent);
        bigFont.draw(batch, localization.getCompendiumTitle(), COMPENDIUM_X + 82f, COMPENDIUM_Y + COMPENDIUM_H - 18f);

        List<PusakaCompendium.RelicEntry> entries = PusakaCompendium.getInstance().getEntries();
        hudFont.getData().setScale(0.96f);

        for (int i = 0; i < entries.size(); i++) {
            PusakaCompendium.RelicEntry entry = entries.get(i);
            float rowY = COMPENDIUM_Y + COMPENDIUM_H - 96f - i * 36f;

            TextureRegion reg = getTileRegion(entry.getBaseTileId());
            if (reg != null) {
                batch.draw(reg, COMPENDIUM_X + 24f, rowY + 3f, 25f, 25f);
            }

            hudFont.setColor(entry.isUnlocked() ? goldAccent : Color.LIGHT_GRAY);
            String meaningText = entry.isUnlocked()
                    ? entry.getMeaning(isIndo)
                    : "??? (" + entry.getClearedCount() + "/4)";
            hudFont.draw(batch, entry.getPinyin() + " - " + meaningText, COMPENDIUM_X + 60f, rowY + 21f);
        }

        hudFont.setColor(Color.LIGHT_GRAY);
        hudFont.draw(batch, localization.getCompendiumHint(), COMPENDIUM_X + 38f, COMPENDIUM_Y + 24f);
        hudFont.getData().setScale(1.18f);
    }

    private TextureRegion getTileRegion(int tileId) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        int baseId = SpecialTileType.extractBaseTileId(tileId);
        int index = Math.floorMod(baseId - 1, tiles.length);
        return tiles[index];
    }

    public Vector2 pixelToCell(float px, float py) {
        int col = (int) Math.floor((px - BOARD_X) / CELL);
        int row = (int) Math.floor((BOARD_Y + BOARD_PX - py) / CELL);
        if (col < 0 || col >= GRID_SIZE || row < 0 || row >= GRID_SIZE) {
            return null;
        }
        return new Vector2(col, row);
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        hudFont.dispose();
        bigFont.dispose();
    }
}