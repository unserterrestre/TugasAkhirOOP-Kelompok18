package com.ProyekPermainanOOP.blockblast.synergy;

import com.ProyekPermainanOOP.blockblast.model.Board;
import java.util.Set;

/**
 * Resep Sinergi "Kitab Awan" (Buku [6] + Awan [9]):
 * Memulihkan +1 nyawa pemain dan memberikan +250 bonus skor.
 */
public class ScholarCloudSynergy extends SynergyHandler {
    private static final int TILE_BUKU = 6; // 0x4E66
    private static final int TILE_AWAN = 9; // 0x4E91

    @Override
    protected void applyRule(Set<Integer> clearedBaseIds, boolean[][] toClear, Board board, SynergyResult result) {
        if (clearedBaseIds.contains(TILE_BUKU) && clearedBaseIds.contains(TILE_AWAN)) {
            result.addEffect("SINERGI KITAB AWAN (+1 NYAWA)", 250, 1);
        }
    }
}