package com.ProyekPermainanOOP.blockblast.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 8;
    private final int[][] grid;

    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    public int getSize() {
        return SIZE;
    }

    public int[][] getGrid() {
        return getGridSnapshot();
    }

    public boolean canPlaceBlock(Block block, int col, int row) {
        return canPlace(block, row, col);
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
        int[][] tileGrid = block.getTileGrid();
        int blockRows = block.getRows();
        int blockCols = block.getCols();

        for (int r = 0; r < blockRows; r++) {
            for (int c = 0; c < blockCols; c++) {
                if (shape[r][c] == 1) {
                    int tileValue = (tileGrid[r][c] > 0) ? tileGrid[r][c] : block.getTileId();
                    grid[row + r][col + c] = tileValue;
                }
            }
        }
        return true;
    }

    public ClearResult clearLines() {
        boolean[] matchingRows = new boolean[SIZE];
        boolean[] matchingCols = new boolean[SIZE];
        List<Integer> rowIndices = new ArrayList<>();
        List<Integer> colIndices = new ArrayList<>();

        for (int r = 0; r < SIZE; r++) {
            int expectedTile = grid[r][0];
            if (expectedTile == 0) {
                continue;
            }
            boolean sameTileRow = true;
            for (int c = 1; c < SIZE; c++) {
                if (grid[r][c] != expectedTile) {
                    sameTileRow = false;
                    break;
                }
            }
            if (sameTileRow) {
                matchingRows[r] = true;
                rowIndices.add(r);
            }
        }

        for (int c = 0; c < SIZE; c++) {
            int expectedTile = grid[0][c];
            if (expectedTile == 0) {
                continue;
            }
            boolean sameTileCol = true;
            for (int r = 1; r < SIZE; r++) {
                if (grid[r][c] != expectedTile) {
                    sameTileCol = false;
                    break;
                }
            }
            if (sameTileCol) {
                matchingCols[c] = true;
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
                if (matchingRows[r] || matchingCols[c]) {
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
                if (snapshot[r][c] < 0) {
                    throw new IllegalArgumentException("Nilai sel snapshot tidak boleh negatif.");
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