package com.ProyekPermainanOOP.blockblast.model;

public class ClearResult {
    private final int linesCleared;
    private final int[] clearedRows;
    private final int[] clearedCols;
    private final int[][] clearedCells;
    private final int[] clearedTileValues;
    private final boolean specialTriggered;
    private final String synergyName;
    private final int synergyBonusScore;
    private final int synergyBonusLives;

    public ClearResult(int linesCleared, int[] clearedRows, int[] clearedCols, int[][] clearedCells) {
        this(linesCleared, clearedRows, clearedCols, clearedCells, new int[0], false, "", 0, 0);
    }

    public ClearResult(int linesCleared, int[] clearedRows, int[] clearedCols, int[][] clearedCells,
                       int[] clearedTileValues, boolean specialTriggered) {
        this(linesCleared, clearedRows, clearedCols, clearedCells, clearedTileValues, specialTriggered, "", 0, 0);
    }

    public ClearResult(int linesCleared, int[] clearedRows, int[] clearedCols, int[][] clearedCells,
                       int[] clearedTileValues, boolean specialTriggered,
                       String synergyName, int synergyBonusScore, int synergyBonusLives) {
        this.linesCleared = linesCleared;
        this.clearedRows = clearedRows;
        this.clearedCols = clearedCols;
        this.clearedCells = clearedCells;
        this.clearedTileValues = clearedTileValues;
        this.specialTriggered = specialTriggered;
        this.synergyName = (synergyName != null) ? synergyName : "";
        this.synergyBonusScore = synergyBonusScore;
        this.synergyBonusLives = synergyBonusLives;
    }

    public static ClearResult empty() {
        return new ClearResult(0, new int[0], new int[0], new int[0][0], new int[0], false, "", 0, 0);
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public int[] getClearedRows() {
        return clearedRows;
    }

    public int[] getClearedCols() {
        return clearedCols;
    }

    public int[][] getClearedCells() {
        return clearedCells;
    }

    public int[] getClearedTileValues() {
        return clearedTileValues;
    }

    public boolean isSpecialTriggered() {
        return specialTriggered;
    }

    public String getSynergyName() {
        return synergyName;
    }

    public int getSynergyBonusScore() {
        return synergyBonusScore;
    }

    public int getSynergyBonusLives() {
        return synergyBonusLives;
    }

    public boolean hasSynergy() {
        return synergyName != null && !synergyName.isEmpty();
    }

    public boolean hasClearedLines() {
        return linesCleared > 0 || clearedCells.length > 0;
    }
}