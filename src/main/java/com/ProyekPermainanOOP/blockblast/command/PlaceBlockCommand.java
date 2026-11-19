package src.main.java.com.ProyekPermainanOOP.blockblast.command;

import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.systems.GameManager;

/**
 * Concrete Command (GoF Command Pattern) yang membungkus aksi penempatan balok
 * beserta penyimpanan snapshot (board, tray, score) untuk mendukung rollback (undo).
 */
public class PlaceBlockCommand implements GameCommand {
    private final GameManager gameManager;
    private final int slot;
    private final int row;
    private final int col;

    private int[][] previousGrid;
    private Block[] previousTraySlots;
    private int previousScore;
    private boolean executed;

    public PlaceBlockCommand(GameManager gameManager, int slot, int row, int col) {
        if (gameManager == null) {
            throw new IllegalArgumentException("GameManager tidak boleh null.");
        }
        this.gameManager = gameManager;
        this.slot = slot;
        this.row = row;
        this.col = col;
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (executed) {
            return false;
        }

        this.previousGrid = gameManager.getBoard().getGridSnapshot();
        this.previousTraySlots = gameManager.getTray().getSnapshot();
        this.previousScore = gameManager.getScoreManager().getScore();

        boolean success = gameManager.tryPlaceBlock(slot, row, col);
        if (success) {
            this.executed = true;
        }
        return success;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }
        gameManager.getBoard().restoreSnapshot(previousGrid);
        gameManager.getTray().restoreSnapshot(previousTraySlots);
        gameManager.getScoreManager().restoreScore(previousScore);
        this.executed = false;
    }
}
