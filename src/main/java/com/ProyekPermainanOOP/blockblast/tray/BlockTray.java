package com.ProyekPermainanOOP.blockblast.tray;

import com.ProyekPermainanOOP.blockblast.factory.BlockFactory;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockTray {
    public static final int TRAY_SIZE = 3;
    private static final int MAX_GENERATION_ATTEMPTS = 120;

    private final Block[] slots;

    public BlockTray() {
        this.slots = new Block[TRAY_SIZE];
        refillTray(null);
    }

    public BlockTray(Board board) {
        this.slots = new Block[TRAY_SIZE];
        refillTray(board);
    }

    public void refillTray() {
        refillTray(null);
    }

    public void refillTray(Board board) {
        if (board == null) {
            for (int i = 0; i < TRAY_SIZE; i++) {
                slots[i] = BlockFactory.createRandomBlock();
            }
            return;
        }

        Board simulatedBoard = copyBoard(board);
        Set<Integer> usedBaseTileIds = new HashSet<>();

        for (int i = 0; i < TRAY_SIZE; i++) {
            Block chosen = generatePlayableBlock(simulatedBoard, usedBaseTileIds);
            if (chosen != null) {
                usedBaseTileIds.add(SpecialTileType.extractBaseTileId(chosen.getTileId()));
                if (MathUtils.randomBoolean(0.32f)) {
                    infuseSpecialTile(chosen);
                }
            }
            slots[i] = chosen;
            if (chosen != null) {
                simulateBestPlacement(simulatedBoard, chosen);
            }
        }
    }

    private void infuseSpecialTile(Block block) {
        int[][] shape = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        List<int[]> activeCells = new ArrayList<>();

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (shape[r][c] == 1) {
                    activeCells.add(new int[]{r, c});
                }
            }
        }

        if (activeCells.isEmpty()) {
            return;
        }

        int[] target = activeCells.get(MathUtils.random(activeCells.size() - 1));
        int currentVal = (tileGrid[target[0]][target[1]] > 0) ? tileGrid[target[0]][target[1]] : block.getTileId();
        SpecialTileType[] specials = {
                SpecialTileType.DRAGON_VEIN,
                SpecialTileType.JADE_WILDCARD,
                SpecialTileType.TALISMAN_BOMB
        };
        SpecialTileType chosenType = specials[MathUtils.random(specials.length - 1)];
        tileGrid[target[0]][target[1]] = SpecialTileType.encode(currentVal, chosenType);
    }

    private Block generatePlayableBlock(Board targetBoard, Set<Integer> usedBaseTileIds) {
        List<Block> distinctMatchingCandidates = new ArrayList<>();
        List<Block> distinctValidCandidates = new ArrayList<>();
        List<Block> anyValidCandidates = new ArrayList<>();

        for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
            Block candidate = BlockFactory.createRandomBlock();
            if (!targetBoard.hasValidMove(candidate)) {
                continue;
            }

            anyValidCandidates.add(candidate);
            int baseId = SpecialTileType.extractBaseTileId(candidate.getTileId());
            boolean isDistinct = !usedBaseTileIds.contains(baseId);

            if (isDistinct) {
                distinctValidCandidates.add(candidate);
                if (hasMatchingNeighborOpportunity(targetBoard, candidate)) {
                    distinctMatchingCandidates.add(candidate);
                }
            }
        }

        if (!distinctMatchingCandidates.isEmpty()) {
            return distinctMatchingCandidates.get(MathUtils.random(distinctMatchingCandidates.size() - 1));
        }

        if (!distinctValidCandidates.isEmpty()) {
            return distinctValidCandidates.get(MathUtils.random(distinctValidCandidates.size() - 1));
        }

        if (!anyValidCandidates.isEmpty()) {
            Block smallest = anyValidCandidates.get(0);
            for (Block b : anyValidCandidates) {
                if (b.getCellCount() < smallest.getCellCount()) {
                    smallest = b;
                }
            }
            return smallest;
        }

        return BlockFactory.createRandomBlock();
    }

    private boolean hasMatchingNeighborOpportunity(Board targetBoard, Block candidate) {
        int[][] grid = targetBoard.getGridSnapshot();
        int[][] shape = candidate.getShape();
        int[][] tileGrid = candidate.getTileGrid();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        int maxR = Board.SIZE - candidate.getRows();
        int maxC = Board.SIZE - candidate.getCols();

        for (int r = 0; r <= maxR; r++) {
            for (int c = 0; c <= maxC; c++) {
                if (!targetBoard.canPlace(candidate, r, c)) {
                    continue;
                }
                for (int br = 0; br < candidate.getRows(); br++) {
                    for (int bc = 0; bc < candidate.getCols(); bc++) {
                        if (shape[br][bc] == 1) {
                            int rawId = (tileGrid[br][bc] > 0) ? tileGrid[br][bc] : candidate.getTileId();
                            int baseId = SpecialTileType.extractBaseTileId(rawId);
                            for (int d = 0; d < 4; d++) {
                                int nr = r + br + dr[d];
                                int nc = c + bc + dc[d];
                                if (nr >= 0 && nr < Board.SIZE && nc >= 0 && nc < Board.SIZE
                                        && SpecialTileType.extractBaseTileId(grid[nr][nc]) == baseId) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void simulateBestPlacement(Board simBoard, Block block) {
        int maxR = Board.SIZE - block.getRows();
        int maxC = Board.SIZE - block.getCols();
        for (int r = 0; r <= maxR; r++) {
            for (int c = 0; c <= maxC; c++) {
                if (simBoard.canPlace(block, r, c)) {
                    simBoard.placeBlock(block, r, c);
                    simBoard.clearLines();
                    return;
                }
            }
        }
    }

    private Board copyBoard(Board source) {
        Board copy = new Board();
        copy.restoreSnapshot(source.getGridSnapshot());
        return copy;
    }

    public Block[] createSlotSnapshot() {
        Block[] snapshot = new Block[TRAY_SIZE];
        for (int i = 0; i < TRAY_SIZE; i++) {
            snapshot[i] = slots[i];
        }
        return snapshot;
    }

    public void restoreSlotSnapshot(Block[] snapshot) {
        if (snapshot == null || snapshot.length != TRAY_SIZE) {
            return;
        }
        for (int i = 0; i < TRAY_SIZE; i++) {
            slots[i] = snapshot[i];
            if (slots[i] != null) {
                slots[i].setActive(true);
            }
        }
    }

    public int getSlotCount() {
        return TRAY_SIZE;
    }

    public Block getBlock(int slotIndex) {
        validateSlotIndex(slotIndex);
        return slots[slotIndex];
    }

    public boolean hasBlockAt(int slotIndex) {
        validateSlotIndex(slotIndex);
        return slots[slotIndex] != null && slots[slotIndex].isActive();
    }

    public Block takeBlock(int slotIndex) {
        validateSlotIndex(slotIndex);
        Block selected = slots[slotIndex];
        slots[slotIndex] = null;
        return selected;
    }

    public boolean consumeBlock(int slotIndex) {
        return consumeBlock(slotIndex, null);
    }

    public boolean consumeBlock(int slotIndex, Board board) {
        validateSlotIndex(slotIndex);
        if (slots[slotIndex] == null) {
            return false;
        }
        slots[slotIndex].setActive(false);
        slots[slotIndex] = null;

        if (isTrayEmpty()) {
            refillTray(board);
        }
        return true;
    }

    public boolean isTrayEmpty() {
        for (int i = 0; i < TRAY_SIZE; i++) {
            if (slots[i] != null && slots[i].isActive()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyValidPlacement(Board board) {
        if (board == null) {
            return false;
        }
        for (int i = 0; i < TRAY_SIZE; i++) {
            Block block = slots[i];
            if (block != null && block.isActive() && board.hasValidMove(block)) {
                return true;
            }
        }
        return false;
    }

    public void resetTray() {
        refillTray(null);
    }

    public void resetTray(Board board) {
        refillTray(board);
    }

    private void validateSlotIndex(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= TRAY_SIZE) {
            throw new IllegalArgumentException("Indeks slot tray tidak valid: " + slotIndex + ". Harus antara 0 dan " + (TRAY_SIZE - 1));
        }
    }
}