package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SpecialBadgeRenderer {

    public void drawBadge(ShapeRenderer shape, float x, float y, float size, SpecialTileType type, float elapsedAnimTime) {
        if (type == SpecialTileType.NONE) {
            return;
        }
        float pulse = 0.72f + 0.28f * (float) Math.sin(elapsedAnimTime * 7f);
        Color badgeColor = BoardPalette.DRAGON_COLOR;
        if (type == SpecialTileType.JADE_WILDCARD) {
            badgeColor = BoardPalette.WILDCARD_COLOR;
        } else if (type == SpecialTileType.TALISMAN_BOMB) {
            badgeColor = BoardPalette.BOMB_COLOR;
        }
        shape.setColor(badgeColor.r, badgeColor.g, badgeColor.b, pulse);
        shape.rect(x, y, size, 5f);
        shape.rect(x, y + size - 5f, size, 5f);
        shape.rect(x, y, 5f, size);
        shape.rect(x + size - 5f, y, 5f, size);
        shape.circle(x + size - 9f, y + size - 9f, 5.5f, 20);
    }
}