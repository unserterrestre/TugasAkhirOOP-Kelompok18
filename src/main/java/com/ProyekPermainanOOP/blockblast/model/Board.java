package com.ProyekPermainanOOP.blockblast.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Mengelola papan permainan 8x8 (0 = kosong, 1 = terisi), validasi batas & benturan,
 * penempatan balok, penghapusan garis penuh simultan, serta deteksi Game Over.
 */
public class Board {
    public static final int SIZE = 8;
    private final int[][] grid;

    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    public boolean canPlace(Block block, int row, int col) {
        if (block == null) {
            return false;
        }

        int[][] shape = block.getShape();
        int blockRows = block.getRows();
        int blockCols = block.getCols();

        for (int r = 0; r < blockRows; r++) {
            for (int c = 0; c < blockCols; c++) {
                if (shape[r][c] == 1) {
                    int targetRow = row + r;
                    int targetCol = col + c;

                    if (targetRow < 0 || targetRow >= SIZE || targetCol < 0 || targetCol >= SIZE) {
                        return false;
                    }
                    if (grid[targetRow][targetCol] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean placeBlock(Block block, int row, int col) {
        if (!canPlace(block, row, col)) {
            return false;
        }

        int[][] shape = block.getShape();
        int blockRows = block.getRows();
        int blockCols = block.getCols();

        for (int r = 0; r < blockRows; r++) {
            for (int c = 0; c < blockCols; c++) {
                if (shape[r][c] == 1) {
                    grid[row + r][col + c] = 1;
                }
            }
        }
        return true;
    }

    public ClearResult clearLines() {
        boolean[] fullRows = new boolean[SIZE];
        boolean[] fullCols = new boolean[SIZE];
        List<Integer> rowIndices = new ArrayList<>();
        List<Integer> colIndices = new ArrayList<>();

        for (int r = 0; r < SIZE; r++) {
            boolean rowFull = true;
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) {
                    rowFull = false;
                    break;
                }
            }
            if (rowFull) {
                fullRows[r] = true;
                rowIndices.add(r);
            }
        }

        for (int c = 0; c < SIZE; c++) {
            boolean colFull = true;
            for (int r = 0; r < SIZE; r++) {
                if (grid[r][c] == 0) {
                    colFull = false;
                    break;
                }
            }
            if (colFull) {
                fullCols[c] = true;
                colIndices.add(c);
            }
        }

        int totalLinesCleared = rowIndices.size() + colIndices.size();
        if (totalLinesCleared == 0) {
            return ClearResult.empty();
        }

        List<int[]> uniqueClearedCells = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (fullRows[r] || fullCols[c]) {
                    grid[r][c] = 0;
                    uniqueClearedCells.add(new int[]{r, c});
                }
            }
        }

        int[] clearedRowsArr = rowIndices.stream().mapToInt(Integer::intValue).toArray();
        int[] clearedColsArr = colIndices.stream().mapToInt(Integer::intValue).toArray();
        int[][] clearedCellsArr = uniqueClearedCells.toArray(new int[0][]);

        return new ClearResult(totalLinesCleared, clearedRowsArr, clearedColsArr, clearedCellsArr);
    }

    public boolean hasValidMove(Block block) {
        if (block == null) {
            return false;
        }

        int maxStartRow = SIZE - block.getRows();
        int maxStartCol = SIZE - block.getCols();

        if (maxStartRow < 0 || maxStartCol < 0) {
            return false;
        }

        for (int r = 0; r <= maxStartRow; r++) {
            for (int c = 0; c <= maxStartCol; c++) {
                if (canPlace(block, r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getGridSnapshot() {
        int[][] snapshot = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            snapshot[r] = grid[r].clone();
        }
        return snapshot;
    }

    public void restoreSnapshot(int[][] snapshot) {
        if (snapshot == null || snapshot.length != SIZE) {
            throw new IllegalArgumentException("Snapshot papan harus berukuran 8x8.");
        }
        for (int r = 0; r < SIZE; r++) {
            if (snapshot[r] == null || snapshot[r].length != SIZE) {
                throw new IllegalArgumentException("Snapshot papan harus berukuran 8x8.");
            }
            for (int c = 0; c < SIZE; c++) {
                if (snapshot[r][c] != 0 && snapshot[r][c] != 1) {
                    throw new IllegalArgumentException("Nilai sel snapshot hanya boleh 0 atau 1.");
                }
            }
        }
        for (int r = 0; r < SIZE; r++) {
            grid[r] = snapshot[r].clone();
        }
    }

    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
            }
        }
    }
}