package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TrayBlockRenderer {

    private final SpecialBadgeRenderer badgeRenderer = new SpecialBadgeRenderer();

    public void drawTrayShapes(ShapeRenderer shape, BlockTray tray, int selectedSlotIndex,
                               BoardEffectManager effectManager, float elapsedAnimTime) {
        if (tray == null) {
            return;
        }
        int slotCount = tray.getSlotCount();
        float slotWidth = BoardMetrics.BOARD_PX / slotCount;

        for (int slot = 0; slot < slotCount; slot++) {
            if (slot == selectedSlotIndex) {
                continue;
            }
            Block block = tray.getBlock(slot);
            if (block == null || !block.isActive()) {
                continue;
            }
            float scale = effectManager.getSlotSpawnScale(slot);
            float size = BoardMetrics.TRAY_TILE_SIZE * scale;
            int[][] cells = block.getShape();
            int[][] tileGrid = block.getTileGrid();
            float centerX = BoardMetrics.BOARD_X + (slot + 0.5f) * slotWidth;
            float startX = centerX - (block.getCols() * size) / 2f;
            float startY = BoardMetrics.TRAY_CENTER_Y + (block.getRows() * size) / 2f;
            for (int row = 0; row < cells.length; row++) {
                for (int col = 0; col < cells[row].length; col++) {
                    if (cells[row][col] != 0) {
                        float x = startX + col * size;
                        float y = startY - (row + 1) * size;
                        shape.rect(x + 1.5f, y + 1.5f, Math.max(2f, size - 3f), Math.max(2f, size - 3f),
                                BoardPalette.TILE_BOTTOM, BoardPalette.TILE_BOTTOM, BoardPalette.TILE_TOP, BoardPalette.TILE_TOP);
                        badgeRenderer.drawBadge(shape, x + 1.5f, y + 1.5f, Math.max(2f, size - 3f),
                                SpecialTileType.fromEncoded(tileGrid[row][col]), elapsedAnimTime);
                    }
                }
            }
        }
    }

    public void drawDraggedShape(ShapeRenderer shape, DragDropManager dragDropManager, float elapsedAnimTime) {
        if (dragDropManager == null || !dragDropManager.isDragging()) {
            return;
        }
        Block block = dragDropManager.getActiveBlock();
        int[][] cells = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        float liftPulse = 1.06f + 0.04f * (float) Math.sin(elapsedAnimTime * 14f);
        float tileW = BoardMetrics.CELL * liftPulse;
        float centerX = dragDropManager.getTouchX();
        float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY() + 8f;

        float startX = centerX - (block.getCols() * tileW) / 2f;
        float startY = centerY + (block.getRows() * tileW) / 2f;

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (cells[r][c] == 1) {
                    float x = startX + c * tileW;
                    float y = startY - (r + 1) * tileW;
                    shape.rect(x + 2.5f, y + 2.5f, tileW - 5f, tileW - 5f,
                            BoardPalette.TILE_BOTTOM, BoardPalette.TILE_BOTTOM, BoardPalette.TILE_TOP, BoardPalette.TILE_TOP);
                    badgeRenderer.drawBadge(shape, x + 2.5f, y + 2.5f, tileW - 5f,
                            SpecialTileType.fromEncoded(tileGrid[r][c]), elapsedAnimTime);
                }
            }
        }
    }

    public void drawTrayAndDraggedTiles(SpriteBatch batch, BlockTray tray, DragDropManager dragDropManager,
                                        BoardEffectManager effectManager, float elapsedAnimTime) {
        if (GameAssets.chineseCharTiles == null || GameAssets.chineseCharTiles.length == 0) {
            return;
        }
        if (tray != null) {
            int slotCount = tray.getSlotCount();
            float slotWidth = BoardMetrics.BOARD_PX / slotCount;
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
                float size = BoardMetrics.TRAY_TILE_SIZE * scale;
                int[][] tileGrid = block.getTileGrid();
                float startX = BoardMetrics.BOARD_X + (slot + 0.5f) * slotWidth - (block.getCols() * size) / 2f;
                float startY = BoardMetrics.TRAY_CENTER_Y + (block.getRows() * size) / 2f;

                for (int row = 0; row < block.getRows(); row++) {
                    for (int col = 0; col < block.getCols(); col++) {
                        int tileId = tileGrid[row][col];
                        if (tileId > 0) {
                            TextureRegion region = BoardPalette.getTileRegion(tileId);
                            if (region != null) {
                                float pad = Math.max(2f, 4f * scale);
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
            float tileW = BoardMetrics.CELL * liftPulse;
            float centerX = dragDropManager.getTouchX();
            float centerY = dragDropManager.getTouchY() + dragDropManager.getDragOffsetY() + 8f;
            float startX = centerX - (block.getCols() * tileW) / 2f;
            float startY = centerY + (block.getRows() * tileW) / 2f;

            for (int r = 0; r < block.getRows(); r++) {
                for (int c = 0; c < block.getCols(); c++) {
                    int tileId = tileGrid[r][c];
                    if (tileId > 0) {
                        TextureRegion region = BoardPalette.getTileRegion(tileId);
                        if (region != null) {
                            float x = startX + c * tileW + 8f;
                            float y = startY - (r + 1) * tileW + 8f;
                            batch.draw(region, x, y, tileW - 16f, tileW - 16f);
                        }
                    }
                }
            }
        }
    }
}