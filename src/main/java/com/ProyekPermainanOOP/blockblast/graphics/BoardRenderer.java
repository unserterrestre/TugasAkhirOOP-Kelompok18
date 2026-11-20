package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.Iterator;
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

    private static class CellFlash {
        final int row;
        final int col;
        float progress;

        CellFlash(int row, int col) {
            this.row = row;
            this.col = col;
            this.progress = 0f;
        }
    }

    private static class FloatingText {
        final String text;
        float x;
        float y;
        float age;
        final float duration = 0.95f;
        final Color color;

        FloatingText(String text, float x, float y, Color color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.age = 0f;
            this.color = color.cpy();
        }
    }

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont hudFont = new BitmapFont();
    private final BitmapFont bigFont = new BitmapFont();

    private final Color bgTop = Color.valueOf("125A44");
    private final Color bgBottom = Color.valueOf("051E17");
    private final Color panelTop = Color.valueOf("0D3D2E");
    private final Color panelBottom = Color.valueOf("061E17");
    private final Color emptyTop = Color.valueOf("185C47");
    private final Color emptyBottom = Color.valueOf("0E3D2E");
    private final Color tileTop = Color.valueOf("FFFDF7");
    private final Color tileBottom = Color.valueOf("EADBB7");
    private final Color validPreview = new Color(0.25f, 0.95f, 0.55f, 0.42f);
    private final Color invalidPreview = new Color(0.95f, 0.30f, 0.30f, 0.36f);
    private final Color goldAccent = Color.valueOf("F3C65F");
    private final Color heartRed = Color.valueOf("FF5C6C");
    private final Color mintAccent = Color.valueOf("7CF5C4");

    private final List<CellFlash> flashes = new ArrayList<>();
    private final List<FloatingText> floatingTexts = new ArrayList<>();
    private float elapsedAnimTime = 0f;

    public BoardRenderer() {
        hudFont.getData().setScale(1.35f);
        bigFont.getData().setScale(2.1f);
    }

    public void triggerLineClearAnimation(int[][] clearedCells, int earnedPoints) {
        if (clearedCells != null) {
            for (int[] cell : clearedCells) {
                if (cell != null && cell.length >= 2) {
                    flashes.add(new CellFlash(cell[0], cell[1]));
                }
            }
        }
        if (earnedPoints > 0) {
            floatingTexts.add(new FloatingText("+" + earnedPoints, BOARD_X + BOARD_PX / 2f - 36f, BOARD_Y + BOARD_PX / 2f, goldAccent));
        }
    }

    public void spawnFloatingText(String text, float x, float y, Color color) {
        floatingTexts.add(new FloatingText(text, x, y, color));
    }

    public void updateAnimations(float delta) {
        elapsedAnimTime += delta;

        Iterator<CellFlash> flashIt = flashes.iterator();
        while (flashIt.hasNext()) {
            CellFlash cf = flashIt.next();
            cf.progress += delta * 2.8f;
            if (cf.progress >= 1f) {
                flashIt.remove();
            }
        }

        Iterator<FloatingText> textIt = floatingTexts.iterator();
        while (textIt.hasNext()) {
            FloatingText ft = textIt.next();
            ft.age += delta;
            ft.y += delta * 48f;
            if (ft.age >= ft.duration) {
                textIt.remove();
            }
        }
    }

    public void begin(Matrix4 projection) {
        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void end() {
        shape.end();
    }

    public void drawBackground() {
        shape.rect(0, 0, VIEW_W, VIEW_H, bgBottom, bgBottom, bgTop, bgTop);
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

    public void drawBoard(int[][] grid) {
        shape.rect(BOARD_X - 10f, BOARD_Y - 10f, BOARD_PX + 20f, BOARD_PX + 20f, panelBottom, panelBottom, panelTop, panelTop);

        float pulse = 0.5f + 0.5f * (float) Math.sin(elapsedAnimTime * 3.0f);
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                float x = BOARD_X + c * CELL;
                float y = BOARD_Y + (GRID_SIZE - 1 - r) * CELL;
                if (grid[r][c] == 0) {
                    Color top = emptyTop.cpy().lerp(panelTop, pulse * 0.25f);
                    shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f, emptyBottom, emptyBottom, top, top);
                } else {
                    shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f, tileBottom, tileBottom, tileTop, tileTop);
                }
            }
        }
    }

    public void drawDragPreview(DragDropManager dragDropManager) {
        if (dragDropManager == null || !dragDropManager.isDragging()) {
            return;
        }
        Block block = dragDropManager.getActiveBlock();
        int startRow = dragDropManager.getPreviewRow();
        int startCol = dragDropManager.getPreviewCol();
        if (block == null || startRow < 0 || startCol < 0) {
            return;
        }

        int[][] shapeGrid = block.getShape();
        Color previewColor = dragDropManager.isPreviewValid() ? validPreview : invalidPreview;
        shape.setColor(previewColor);

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (shapeGrid[r][c] == 1) {
                    int boardRow = startRow + r;
                    int boardCol = startCol + c;
                    if (boardRow >= 0 && boardRow < GRID_SIZE && boardCol >= 0 && boardCol < GRID_SIZE) {
                        float x = BOARD_X + boardCol * CELL;
                        float y = BOARD_Y + (GRID_SIZE - 1 - boardRow) * CELL;
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
            int[][] cells = block.getShape();
            float startX = BOARD_X + (slot + 0.5f) * slotWidth - block.getCols() * TRAY_TILE_SIZE / 2f;
            float startY = TRAY_CENTER_Y + block.getRows() * TRAY_TILE_SIZE / 2f;
            for (int row = 0; row < cells.length; row++) {
                for (int col = 0; col < cells[row].length; col++) {
                    if (cells[row][col] != 0) {
                        float x = startX + col * TRAY_TILE_SIZE;
                        float y = startY - (row + 1) * TRAY_TILE_SIZE;
                        shape.rect(x + 1f, y + 1f, TRAY_TILE_SIZE - 2f, TRAY_TILE_SIZE - 2f, tileBottom, tileBottom, tileTop, tileTop);
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
        float centerX = dragDropManager.getTouchX();
        float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY();

        float startX = centerX - (block.getCols() * CELL) / 2f;
        float startY = centerY + (block.getRows() * CELL) / 2f;

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (cells[r][c] == 1) {
                    float x = startX + c * CELL;
                    float y = startY - (r + 1) * CELL;
                    shape.rect(x + 2f, y + 2f, CELL - 4f, CELL - 4f, tileBottom, tileBottom, tileTop, tileTop);
                }
            }
        }
    }

    public void drawClearFlashes() {
        for (CellFlash cf : flashes) {
            float alpha = Math.max(0f, 1f - cf.progress);
            float expand = cf.progress * 10f;
            float x = BOARD_X + cf.col * CELL - expand * 0.5f;
            float y = BOARD_Y + (GRID_SIZE - 1 - cf.row) * CELL - expand * 0.5f;
            Color flashColor = new Color(1f, 0.92f, 0.55f, alpha * 0.85f);
            shape.setColor(flashColor);
            shape.rect(x, y, CELL + expand, CELL + expand);
        }
    }

    public void drawOverlay(Matrix4 projection, int[][] grid, BlockTray tray, DragDropManager dragDropManager,
                            int score, int highScore, int combo, int lives, float elapsedSeconds,
                            boolean muted, boolean gameOver) {
        batch.setProjectionMatrix(projection);
        batch.begin();

        if (GameAssets.chineseCharTiles != null && GameAssets.chineseCharTiles.length > 0) {
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
                    int[][] tileGrid = block.getTileGrid();
                    float startX = BOARD_X + (slot + 0.5f) * slotWidth - block.getCols() * TRAY_TILE_SIZE / 2f;
                    float startY = TRAY_CENTER_Y + block.getRows() * TRAY_TILE_SIZE / 2f;

                    for (int row = 0; row < block.getRows(); row++) {
                        for (int col = 0; col < block.getCols(); col++) {
                            int tileId = tileGrid[row][col];
                            if (tileId > 0) {
                                TextureRegion region = getTileRegion(tileId);
                                if (region != null) {
                                    float x = startX + col * TRAY_TILE_SIZE + 3f;
                                    float y = startY - (row + 1) * TRAY_TILE_SIZE + 3f;
                                    batch.draw(region, x, y, TRAY_TILE_SIZE - 6f, TRAY_TILE_SIZE - 6f);
                                }
                            }
                        }
                    }
                }
            }

            if (dragDropManager != null && dragDropManager.isDragging()) {
                Block block = dragDropManager.getActiveBlock();
                int[][] tileGrid = block.getTileGrid();
                float centerX = dragDropManager.getTouchX();
                float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY();
                float startX = centerX - (block.getCols() * CELL) / 2f;
                float startY = centerY + (block.getRows() * CELL) / 2f;

                for (int r = 0; r < block.getRows(); r++) {
                    for (int c = 0; c < block.getCols(); c++) {
                        int tileId = tileGrid[r][c];
                        if (tileId > 0) {
                            TextureRegion region = getTileRegion(tileId);
                            if (region != null) {
                                float x = startX + c * CELL + 6f;
                                float y = startY - (r + 1) * CELL + 6f;
                                batch.draw(region, x, y, CELL - 12f, CELL - 12f);
                            }
                        }
                    }
                }
            }
        }

        float hudY = BOARD_Y + BOARD_PX + 28f;
        float hudH = 88f;
        float gap = 14f;
        float cardW = (BOARD_PX - gap * 2f) / 3f;

        hudFont.setColor(goldAccent);
        hudFont.draw(batch, "SKOR", BOARD_X + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, String.valueOf(score), BOARD_X + 14f, hudY + 42f);

        float midX = BOARD_X + cardW + gap;
        hudFont.setColor(heartRed);
        hudFont.draw(batch, "NYAWA", midX + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, lives + " / 3", midX + 14f, hudY + 42f);

        float rightX = BOARD_X + (cardW + gap) * 2f;
        int totalSec = Math.max(0, (int) elapsedSeconds);
        String timeStr = String.format("%02d:%02d", totalSec / 60, totalSec % 60);
        hudFont.setColor(mintAccent);
        hudFont.draw(batch, "WAKTU", rightX + 14f, hudY + hudH - 14f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, timeStr, rightX + 14f, hudY + 42f);

        hudFont.setColor(Color.LIGHT_GRAY);
        String statusLine = "BEST: " + highScore + "   x" + combo + "   [R] RESET   [M] MUSIK: " + (muted ? "OFF" : "ON");
        hudFont.draw(batch, statusLine, BOARD_X, VIEW_H - 22f);

        for (FloatingText ft : floatingTexts) {
            float alpha = Math.max(0f, 1f - (ft.age / ft.duration));
            bigFont.setColor(ft.color.r, ft.color.g, ft.color.b, alpha);
            bigFont.draw(batch, ft.text, ft.x, ft.y);
        }

        if (gameOver) {
            bigFont.setColor(Color.WHITE);
            bigFont.draw(batch, "GAME OVER", BOARD_X + 150f, BOARD_Y + BOARD_PX / 2f + 24f);
            hudFont.setColor(goldAccent);
            hudFont.draw(batch, "Klik layar atau tekan R untuk ulang", BOARD_X + 115f, BOARD_Y + BOARD_PX / 2f - 18f);
        }

        batch.end();
    }

    private TextureRegion getTileRegion(int tileId) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        int index = Math.floorMod(tileId - 1, tiles.length);
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