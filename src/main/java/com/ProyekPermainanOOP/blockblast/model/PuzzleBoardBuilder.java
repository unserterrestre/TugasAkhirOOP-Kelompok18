package com.ProyekPermainanOOP.blockblast.model;

import java.util.ArrayList;
import java.util.List;

/**
 * GoF Builder Pattern untuk menyusun papan tantangan (Puzzle / Zen Challenge)
 * dengan pola aksara awal dan ubin bersegel tertentu.
 */
public class PuzzleBoardBuilder {
    private final int[][] initialGrid = new int[Board.SIZE][Board.SIZE];
    private final List<int[]> sealedCoords = new ArrayList<>();

    public PuzzleBoardBuilder setTile(int row, int col, int encodedTileId) {
        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            initialGrid[row][col] = encodedTileId;
        }
        return this;
    }

    public PuzzleBoardBuilder setRowPattern(int row, int[] rowValues) {
        if (row >= 0 && row < Board.SIZE && rowValues != null) {
            int limit = Math.min(Board.SIZE, rowValues.length);
            System.arraycopy(rowValues, 0, initialGrid[row], 0, limit);
        }
        return this;
    }

    public PuzzleBoardBuilder addSealAt(int row, int col) {
        if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
            sealedCoords.add(new int[]{row, col});
        }
        return this;
    }

    public Board build() {
        Board board = new Board();
        board.restoreSnapshot(initialGrid);
        for (int[] coord : sealedCoords) {
            board.sealCell(coord[0], coord[1]);
        }
        return board;
    }
}