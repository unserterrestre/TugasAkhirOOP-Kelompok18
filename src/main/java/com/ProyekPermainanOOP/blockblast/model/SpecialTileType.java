package com.ProyekPermainanOOP.blockblast.model;

public enum SpecialTileType {
    NONE(0),
    DRAGON_VEIN(100),
    JADE_WILDCARD(200),
    TALISMAN_BOMB(300);

    private final int codeOffset;

    SpecialTileType(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    public int getCodeOffset() {
        return codeOffset;
    }

    public static int encode(int baseTileId, SpecialTileType type) {
        int cleanBase = Math.max(1, baseTileId % 100);
        return cleanBase + type.codeOffset;
    }

    public static int extractBaseTileId(int encodedValue) {
        if (encodedValue <= 0) {
            return 0;
        }
        return encodedValue % 100;
    }

    public static SpecialTileType fromEncoded(int encodedValue) {
        if (encodedValue >= 300) {
            return TALISMAN_BOMB;
        }
        if (encodedValue >= 200) {
            return JADE_WILDCARD;
        }
        if (encodedValue >= 100) {
            return DRAGON_VEIN;
        }
        return NONE;
    }
}