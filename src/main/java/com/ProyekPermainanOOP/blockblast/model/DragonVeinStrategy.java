package com.ProyekPermainanOOP.blockblast.model;

public class DragonVeinStrategy implements SpecialTileStrategy {
    @Override
    public void applyEffect(int row, int col, int size, boolean[][] toClear) {
        for (int c = 0; c < size; c++) {
            toClear[row][c] = true;
        }
        for (int r = 0; r < size; r++) {
            toClear[r][col] = true;
        }
    }
}