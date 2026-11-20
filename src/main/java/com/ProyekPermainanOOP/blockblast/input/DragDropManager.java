package com.ProyekPermainanOOP.blockblast.input;

import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;

public class DragDropManager {
    private static final float DRAG_OFFSET_Y = 48f;
    private static final int TRAY_SLOTS = 3;

    private Block activeBlock;
    private float touchX;
    private float touchY;
    private float dragOffsetY;
    private int selectedSlotIndex;
    private int previewRow;
    private int previewCol;
    private boolean previewValid;

    public DragDropManager() {
        this(DRAG_OFFSET_Y);
    }

    public DragDropManager(float dragOffsetY) {
        this.activeBlock = null;
        this.touchX = 0f;
        this.touchY = 0f;
        this.dragOffsetY = dragOffsetY;
        this.selectedSlotIndex = -1;
        this.previewRow = -1;
        this.previewCol = -1;
        this.previewValid = false;
    }

    public boolean isDragging() {
        return activeBlock != null && activeBlock.isActive();
    }

    public boolean startDrag(Block block, int slotIndex, float startX, float startY) {
        if (block == null || !block.isActive() || slotIndex < 0 || slotIndex >= TRAY_SLOTS) {
            return false;
        }
        clearActiveVisuals();
        this.activeBlock = block;
        this.selectedSlotIndex = slotIndex;
        this.touchX = startX;
        this.touchY = startY;
        this.activeBlock.setSelected(true);
        this.activeBlock.setHighlighted(false);
        resetPreview();
        return true;
    }

    public void updateDragPosition(float x, float y, Board board) {
        this.touchX = x;
        this.touchY = y;

        if (!isDragging()) {
            resetPreview();
            return;
        }

        float liftedY = y + dragOffsetY;
        int[] hoveredCell = convertWorldToTopDownGrid(
                x,
                liftedY,
                BoardRenderer.BOARD_X,
                BoardRenderer.BOARD_Y,
                BoardRenderer.CELL,
                BoardRenderer.GRID_SIZE
        );

        if (hoveredCell != null && board != null) {
            int anchorCol = hoveredCell[0] - (activeBlock.getCols() / 2);
            int anchorRow = hoveredCell[1] - (activeBlock.getRows() / 2);
            this.previewRow = anchorRow;
            this.previewCol = anchorCol;
            this.previewValid = board.canPlace(activeBlock, anchorRow, anchorCol);
            this.activeBlock.setHighlighted(this.previewValid);
        } else {
            resetPreview();
            this.activeBlock.setHighlighted(false);
        }
    }

    public int[] convertWorldToTopDownGrid(float worldX, float worldY, float boardX, float boardY, float cellSize, int gridSize) {
        int col = (int) Math.floor((worldX - boardX) / cellSize);
        int row = (int) Math.floor((boardY + (gridSize * cellSize) - worldY) / cellSize);
        if (col < 0 || col >= gridSize || row < 0 || row >= gridSize) {
            return null;
        }
        return new int[]{col, row};
    }

    public boolean tryDropBlock(Board board, BlockTray tray, float dropX, float dropY) {
        if (!isDragging() || board == null || tray == null) {
            cancelDrag();
            return false;
        }

        updateDragPosition(dropX, dropY, board);
        if (previewValid && board.placeBlock(activeBlock, previewRow, previewCol)) {
            int slotToRemove = selectedSlotIndex;
            clearActiveVisuals();
            activeBlock = null;
            selectedSlotIndex = -1;
            resetPreview();
            tray.removeBlock(slotToRemove);
            return true;
        }

        cancelDrag();
        return false;
    }

    public void cancelDrag() {
        clearActiveVisuals();
        this.activeBlock = null;
        this.selectedSlotIndex = -1;
        resetPreview();
    }

    private void clearActiveVisuals() {
        if (this.activeBlock != null) {
            this.activeBlock.setSelected(false);
            this.activeBlock.setHighlighted(false);
        }
    }

    private void resetPreview() {
        this.previewRow = -1;
        this.previewCol = -1;
        this.previewValid = false;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public float getTouchX() {
        return touchX;
    }

    public float getTouchY() {
        return touchY;
    }

    public float getDragOffsetY() {
        return dragOffsetY;
    }

    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
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