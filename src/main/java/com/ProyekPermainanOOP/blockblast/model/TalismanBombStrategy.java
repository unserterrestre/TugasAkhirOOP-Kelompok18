package com.ProyekPermainanOOP.blockblast.model;

public class TalismanBombStrategy implements SpecialTileStrategy {
    @Override
    public void applyEffect(int row, int col, int size, boolean[][] toClear) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                    toClear[nr][nc] = true;
                }
            }
        }
    }
}