package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class BoardPalette {

    public static final Color PANEL_TOP = Color.valueOf("0F4736");
    public static final Color PANEL_BOTTOM = Color.valueOf("062119");
    public static final Color EMPTY_TOP = Color.valueOf("1A634C");
    public static final Color EMPTY_BOTTOM = Color.valueOf("0E3E2F");
    public static final Color TILE_TOP = Color.valueOf("FFFDF7");
    public static final Color TILE_BOTTOM = Color.valueOf("EADBB7");
    public static final Color GOLD_ACCENT = Color.valueOf("F3C65F");
    public static final Color HEART_RED = Color.valueOf("FF5C6C");
    public static final Color MINT_ACCENT = Color.valueOf("7CF5C4");
    public static final Color DRAGON_COLOR = Color.valueOf("38BDF8");
    public static final Color WILDCARD_COLOR = Color.valueOf("34D399");
    public static final Color BOMB_COLOR = Color.valueOf("FB7185");

    private BoardPalette() {
    }

    public static TextureRegion getTileRegion(int tileId) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        int baseId = SpecialTileType.extractBaseTileId(tileId);
        int index = Math.floorMod(baseId - 1, tiles.length);
        return tiles[index];
    }
}