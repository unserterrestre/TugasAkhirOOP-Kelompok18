package com.ProyekPermainanOOP.blockblast.tray;

import com.ProyekPermainanOOP.blockblast.factory.BlockFactory;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;

public class BlockTray {
    public static final int SLOT_COUNT = 3;
    private final Block[] traySlots = new Block[SLOT_COUNT];

    public BlockTray() {
        replenishTray();
    }

    public int getSlotCount() {
        return SLOT_COUNT;
    }

    public boolean isValidIndex(int slotIndex) {
        return slotIndex >= 0 && slotIndex < SLOT_COUNT;
    }

    public boolean hasBlockAt(int slotIndex) {
        return isValidIndex(slotIndex) && traySlots[slotIndex] != null && traySlots[slotIndex].isActive();
    }

    public void replenishTray() {
        for (int i = 0; i < traySlots.length; i++) {
            if (traySlots[i] == null) {
                traySlots[i] = BlockFactory.createRandomBlock();
            }
        }
    }

    public boolean isTrayEmpty() {
        for (Block b : traySlots) {
            if (b != null) {
                return false;
            }
        }
        return true;
    }

    public Block getBlock(int slotIndex) {
        if (isValidIndex(slotIndex)) {
            return traySlots[slotIndex];
        }
        return null;
    }

    public void removeBlock(int slotIndex) {
        if (isValidIndex(slotIndex) && traySlots[slotIndex] != null) {
            Block removed = traySlots[slotIndex];
            traySlots[slotIndex] = null;
            BlockFactory.releaseBlock(removed);
        }

        if (isTrayEmpty()) {
            replenishTray();
        }
    }

    public boolean hasAnyValidPlacement(Board board) {
        if (board == null) {
            return false;
        }
        for (Block b : traySlots) {
            if (b != null && b.isActive() && board.hasValidMove(b)) {
                return true;
            }
        }
        return false;
    }

    public void resetTray() {
        for (int i = 0; i < traySlots.length; i++) {
            if (traySlots[i] != null) {
                BlockFactory.releaseBlock(traySlots[i]);
                traySlots[i] = null;
            }
        }
        replenishTray();
    }
}