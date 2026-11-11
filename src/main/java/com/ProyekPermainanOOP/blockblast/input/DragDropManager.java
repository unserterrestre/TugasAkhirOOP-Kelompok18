package src.main.java.com.ProyekPermainanOOP.blockblast.input;

import src.main.java.com.ProyekPermainanOOP.blockblast.model.Block;

public class DragDropManager {
    private Block activeBlock;
    private float touchX;
    private float touchY;
    private int selectedSlotIndex;

    public DragDropManager() {
        this.activeBlock = null;
        this.selectedSlotIndex = -1;
    }

    public void startDrag(Block block, int slotIndex, float startX, float startY) {
        this.activeBlock = block;
        this.selectedSlotIndex = slotIndex;
        this.touchX = startX;
        this.touchY = startY;
    }

    public void updateDragPosition(float x, float y) {
        this.touchX = x;
        this.touchY = y;
    }

    public int[] convertScreenToGrid(float screenX, float screenY, float boardStartX, float boardStartY, float cellSize) {
        int gridX = (int) ((screenX - boardStartX) / cellSize);
        int gridY = (int) ((screenY - boardStartY) / cellSize);
        return new int[]{gridX, gridY};
    }

    public void releaseBlock() {
        this.activeBlock = null;
        this.selectedSlotIndex = -1;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }
}