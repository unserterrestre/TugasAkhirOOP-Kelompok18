package com.ProyekPermainanOOP.blockblast.command;

import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.model.CellSealState;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;

public class PlaceBlockCommand {
    private final int[][] previousGrid;
    private final CellSealState[][] previousSeals;
    private final Block[] previousTraySlots;
    private final int previousScore;
    private final int previousCombo;
    private final int previousQuestCount;

    public PlaceBlockCommand(Board board, BlockTray tray, int score, int combo, int questCount) {
        this.previousGrid = board.getGridSnapshot();
        this.previousSeals = board.getSealSnapshot();
        this.previousTraySlots = tray.createSlotSnapshot();
        this.previousScore = score;
        this.previousCombo = combo;
        this.previousQuestCount = questCount;
    }

    public void undo(Board board, BlockTray tray) {
        board.restoreSnapshot(previousGrid);
        board.restoreSealSnapshot(previousSeals);
        tray.restoreSlotSnapshot(previousTraySlots);
    }

    public int getPreviousScore() {
        return previousScore;
    }

    public int getPreviousCombo() {
        return previousCombo;
    }

    public int getPreviousQuestCount() {
        return previousQuestCount;
    }
}