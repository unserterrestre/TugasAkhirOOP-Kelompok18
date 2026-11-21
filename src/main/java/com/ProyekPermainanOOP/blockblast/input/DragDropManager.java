package com.ProyekPermainanOOP.blockblast.input;

import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;

public class DragDropManager {

    private static final float DRAG_OFFSET_Y = 75f;

    private Block activeBlock;
    private int selectedSlotIndex;
    private boolean dragging;

    private float touchX;
    private float touchY;

    private int previewRow;
    private int previewCol;
    private boolean previewValid;

    public DragDropManager() {
        resetDragState();
    }

    public void startDrag(Block block, int slotIndex, float startX, float startY) {
        if (block == null || !block.isActive()) {
            return;
        }
        this.activeBlock = block;
        this.selectedSlotIndex = slotIndex;
        this.dragging = true;
        this.touchX = startX;
        this.touchY = startY;
        this.previewRow = -1;
        this.previewCol = -1;
        this.previewValid = false;
    }

    public void updateDragPosition(float currentX, float currentY, Board board) {
        if (!dragging || activeBlock == null) {
            return;
        }

        this.touchX = currentX;
        this.touchY = currentY;

        float blockCenterX = currentX;
        float blockCenterY = currentY + DRAG_OFFSET_Y;

        float topLeftX = blockCenterX - (activeBlock.getCols() * BoardRenderer.CELL) / 2f;
        float topLeftY = blockCenterY + (activeBlock.getRows() * BoardRenderer.CELL) / 2f;

        int col = Math.round((topLeftX - BoardRenderer.BOARD_X) / BoardRenderer.CELL);
        int row = Math.round((BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX - topLeftY) / BoardRenderer.CELL);

        this.previewCol = col;
        this.previewRow = row;
        this.previewValid = (board != null) && board.canPlace(activeBlock, row, col);
    }

    public boolean tryDropBlock(Board board, BlockTray tray, float releaseX, float releaseY) {
        if (!dragging || activeBlock == null || board == null || tray == null) {
            cancelDrag();
            return false;
        }

        updateDragPosition(releaseX, releaseY, board);

        if (previewValid && board.placeBlock(activeBlock, previewRow, previewCol)) {
            int slotToConsume = selectedSlotIndex;
            resetDragState();
            tray.consumeBlock(slotToConsume, board);
            return true;
        }

        cancelDrag();
        return false;
    }

    public void cancelDrag() {
        resetDragState();
    }

    private void resetDragState() {
        this.activeBlock = null;
        this.selectedSlotIndex = -1;
        this.dragging = false;
        this.previewRow = -1;
        this.previewCol = -1;
        this.previewValid = false;
    }

    public boolean isDragging() {
        return dragging;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }

    public float getTouchX() {
        return touchX;
    }

    public float getTouchY() {
        return touchY;
    }

    public float getDragOffsetY() {
        return DRAG_OFFSET_Y;
    }

    public int getPreviewRow() {
        return previewRow;
    }

    public int getPreviewCol() {
        return previewCol;
    }

    public boolean isPreviewValid() {
        return previewValid;
    }
}