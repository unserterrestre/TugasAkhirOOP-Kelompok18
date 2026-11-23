package com.ProyekPermainanOOP.blockblast.synergy;

import com.ProyekPermainanOOP.blockblast.model.Board;
import java.util.Set;

/**
 * Resep Sinergi "Langit & Bumi" (Atas [1] + Bawah [2]):
 * Memicu ledakan pembersihan area 3x3 di pusat papan serta +300 bonus skor.
 */
public class HeavenEarthSynergy extends SynergyHandler {
    private static final int TILE_ATAS = 1;  // 0x4E0A
    private static final int TILE_BAWAH = 2; // 0x4E0B

    @Override
    protected void applyRule(Set<Integer> clearedBaseIds, boolean[][] toClear, Board board, SynergyResult result) {
        if (clearedBaseIds.contains(TILE_ATAS) && clearedBaseIds.contains(TILE_BAWAH)) {
            int centerR = Board.SIZE / 2;
            int centerC = Board.SIZE / 2;

            // Cari titik tengah dari petak yang sedang dihancurkan
            outer:
            for (int r = 0; r < Board.SIZE; r++) {
                for (int c = 0; c < Board.SIZE; c++) {
                    if (toClear[r][c]) {
                        centerR = r;
                        centerC = c;
                        break outer;
                    }
                }
            }

            for (int r = centerR - 1; r <= centerR + 1; r++) {
                for (int c = centerC - 1; c <= centerC + 1; c++) {
                    if (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                        toClear[r][c] = true;
                    }
                }
            }

            result.addEffect("SINERGI LANGIT-BUMI (3x3)", 300, 0);
        }
    }
}