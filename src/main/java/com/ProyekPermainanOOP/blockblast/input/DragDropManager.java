package com.ProyekPermainanOOP.blockblast.input;

import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import java.util.ArrayList;
import java.util.List;

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
    private final List<DragDropListener> listeners = new ArrayList<>();

    public interface DragDropListener {
        void onDragStarted(Block block, int slotIndex, float x, float y);
        void onDragMoved(Block block, float x, float y, int targetRow, int targetCol, boolean valid);
        void onBlockPlaced(Block block, int slotIndex, int targetRow, int targetCol);
        void onDragCancelled(Block block, int slotIndex);
    }

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

    public void addListener(DragDropListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(DragDropListener listener) {
        listeners.remove(listener);
    }

    public boolean isDragging() {
        return activeBlock != null && activeBlock.isActive();
    }

    private boolean isValidTraySlot(int slotIndex) {
        return slotIndex >= 0 && slotIndex < TRAY_SLOTS;
    }

    public void startDrag(Block block, int slotIndex, float startX, float startY) {
        clearActiveBlockVisualState();
        this.activeBlock = block;
        this.selectedSlotIndex = slotIndex;
        this.touchX = startX;
        this.touchY = startY;
        this.previewRow = -1;
        this.previewCol = -1;
        this.previewValid = false;

        if (this.activeBlock != null) {
            this.activeBlock.setSelected(true);
            this.activeBlock.setHighlighted(false);
            notifyDragStarted(this.activeBlock, slotIndex, startX, startY);
        }
    }

    public boolean startDragFromTray(BlockTray tray, int slotIndex, float startX, float startY) {
        if (tray == null || !isValidTraySlot(slotIndex)) {
            return false;
        }
        Block block = tray.getBlock(slotIndex);
        if (block == null || !block.isActive()) {
            return false;
        }
        startDrag(block, slotIndex, startX, startY);
        return true;
    }

    public void updateDragPosition(float x, float y) {
        updateDragPosition(x, y, null);
    }

    public void updateDragPosition(float x, float y, Board board) {
        this.touchX = x;
        this.touchY = y;

        if (!isDragging()) {
            resetPreview();
            return;
        }

        float liftedY = y + dragOffsetY;
        int[] cell = convertScreenToTopDownGrid(
                x,
                liftedY,
                BoardRenderer.BOARD_X,
                BoardRenderer.BOARD_Y,
                BoardRenderer.CELL,
                BoardRenderer.GRID_SIZE
        );

        if (cell != null && board != null) {
            int topRow = cell[1] - (activeBlock.getRows() / 2);
            int leftCol = cell[0] - (activeBlock.getCols() / 2);
            this.previewRow = topRow;
            this.previewCol = leftCol;
            this.previewValid = canPlaceOnBoard(board, activeBlock, topRow, leftCol);
            this.activeBlock.setHighlighted(this.previewValid);
        } else {
            resetPreview();
            this.activeBlock.setHighlighted(false);
        }

        notifyDragMoved(this.activeBlock, x, y, this.previewRow, this.previewCol, this.previewValid);
    }

    public int[] convertScreenToGrid(float screenX, float screenY, float boardStartX, float boardStartY, float cellSize) {
        int gridX = (int) Math.floor((screenX - boardStartX) / cellSize);
        int gridY = (int) Math.floor((screenY - boardStartY) / cellSize);
        return new int[]{gridX, gridY};
    }

    public int[] convertScreenToTopDownGrid(float worldX, float worldY, float boardX, float boardY, float cellSize, int gridSize) {
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
        Block blockToPlace = this.activeBlock;
        int slotIndex = this.selectedSlotIndex;
        int targetRow = this.previewRow;
        int targetCol = this.previewCol;

        if (this.previewValid && canPlaceOnBoard(board, blockToPlace, targetRow, targetCol)) {
            placeBlockOnBoard(board, blockToPlace, targetRow, targetCol);
            clearActiveBlockVisualState();
            this.activeBlock = null;
            this.selectedSlotIndex = -1;
            resetPreview();

            if (isValidTraySlot(slotIndex)) {
                tray.removeBlock(slotIndex);
            }
            notifyBlockPlaced(blockToPlace, slotIndex, targetRow, targetCol);
            return true;
        }

        cancelDrag();
        return false;
    }

    private boolean canPlaceOnBoard(Board board, Block block, int startRow, int startCol) {
        if (board == null || block == null) {
            return false;
        }
        int[][] grid = board.getGrid();
        if (grid == null || grid.length == 0) {
            return false;
        }
        int boardRows = grid.length;
        int boardCols = grid[0].length;
        int[][] shape = block.getShape();

        if (startRow < 0 || startCol < 0 || startRow + block.getRows() > boardRows || startCol + block.getCols() > boardCols) {
            return false;
        }

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1 && grid[startRow + r][startCol + c] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeBlockOnBoard(Board board, Block block, int startRow, int startCol) {
        int[][] grid = board.getGrid();
        int[][] tileGrid = block.getTileGrid();
        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (tileGrid[r][c] != 0) {
                    grid[startRow + r][startCol + c] = tileGrid[r][c];
                }
            }
        }
    }

    public void cancelDrag() {
        Block cancelledBlock = this.activeBlock;
        int slotIndex = this.selectedSlotIndex;
        clearActiveBlockVisualState();
        this.activeBlock = null;
        this.selectedSlotIndex = -1;
        resetPreview();
        if (cancelledBlock != null) {
            notifyDragCancelled(cancelledBlock, slotIndex);
        }
    }

    public void releaseBlock() {
        cancelDrag();
    }

    private void clearActiveBlockVisualState() {
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

    public void setDragOffsetY(float dragOffsetY) {
        this.dragOffsetY = dragOffsetY;
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

    private void notifyDragStarted(Block block, int slotIndex, float x, float y) {
        for (DragDropListener listener : listeners) {
            listener.onDragStarted(block, slotIndex, x, y);
        }
    }

    private void notifyDragMoved(Block block, float x, float y, int row, int col, boolean valid) {
        for (DragDropListener listener : listeners) {
            listener.onDragMoved(block, x, y, row, col, valid);
        }
    }

    private void notifyBlockPlaced(Block block, int slotIndex, int row, int col) {
        for (DragDropListener listener : listeners) {
            listener.onBlockPlaced(block, slotIndex, row, col);
        }
    }

    private void notifyDragCancelled(Block block, int slotIndex) {
        for (DragDropListener listener : listeners) {
            listener.onDragCancelled(block, slotIndex);
        }
    }
}