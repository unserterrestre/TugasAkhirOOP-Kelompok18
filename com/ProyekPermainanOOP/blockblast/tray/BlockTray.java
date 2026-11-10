package com.ProyekPermainanOOP.blockblast.tray;

import com.ProyekPermainanOOP.blockblast.factory.BlockFactory;
import com.ProyekPermainanOOP.blockblast.model.Block;

public class BlockTray {
    private final Block[] traySlots = new Block[3];

    public BlockTray() {
        replenishTray();
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
            if (b != null) return false;
        }
        return true;
    }

    public Block getBlock(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 3) {
            return traySlots[slotIndex];
        }
        return null;
    }

    public void removeBlock(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 3 && traySlots[slotIndex] != null) {
            traySlots[slotIndex] = null;
        }

        if (isTrayEmpty()) {
            replenishTray();
        }
    }
}