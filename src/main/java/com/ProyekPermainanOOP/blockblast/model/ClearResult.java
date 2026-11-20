package com.ProyekPermainanOOP.blockblast.model;

/**
 * Objek pembawa data (immutable DTO) hasil penghapusan baris dan kolom penuh
 * untuk kebutuhan perhitungan skor serta pemicu animasi efek Mahjong.
 */
public class ClearResult {
    private final int linesCleared;
    private final int[] clearedRows;
    private final int[] clearedCols;
    private final int[][] clearedCells; // Setiap elemen berformat {row, col}

    public ClearResult(int linesCleared, int[] clearedRows, int[] clearedCols, int[][] clearedCells) {
        if (linesCleared < 0) {
            throw new IllegalArgumentException("Jumlah garis terhapus tidak boleh negatif.");
        }
        this.linesCleared = linesCleared;
        this.clearedRows = (clearedRows != null) ? clearedRows.clone() : new int[0];
        this.clearedCols = (clearedCols != null) ? clearedCols.clone() : new int[0];
        this.clearedCells = deepCopyCells(clearedCells);
    }

    public static ClearResult empty() {
        return new ClearResult(0, new int[0], new int[0], new int[0][0]);
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public int[] getClearedRows() {
        return clearedRows.clone();
    }

    public int[] getClearedCols() {
        return clearedCols.clone();
    }

    public int[][] getClearedCells() {
        return deepCopyCells(clearedCells);
    }

    public boolean hasClearedLines() {
        return linesCleared > 0;
    }

    private static int[][] deepCopyCells(int[][] source) {
        if (source == null) {
            return new int[0][0];
        }
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = (source[i] != null) ? source[i].clone() : new int[0];
        }
        return copy;
    }
}