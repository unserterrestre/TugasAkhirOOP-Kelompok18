package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class SideDecorationRenderer {

    private final Color bgCenter = Color.valueOf("0B3B2D");
    private final Color bgEdge = Color.valueOf("03140F");
    private final Color bambooDark = Color.valueOf("0A2E23");
    private final Color bambooLight = Color.valueOf("155743");
    private final Color pillarGreen = Color.valueOf("1F7A5C");
    private final Color goldTrim = Color.valueOf("EAB348");
    private final Color lanternRed = Color.valueOf("D93848");
    private final Color lanternGlow = Color.valueOf("FFAA3B");

    public void drawShapes(ShapeRenderer shape, float worldW, float worldH, float elapsedAnimTime,
                           int activeCombo, float clearSurge, int latestScore) {
        float leftEdge = (BoardRenderer.VIEW_W - worldW) * 0.5f;
        float rightEdge = leftEdge + worldW;

        shape.rect(leftEdge, 0f, worldW, worldH, bgEdge, bgEdge, bgCenter, bgCenter);

        float leftBoardStart = BoardRenderer.BOARD_X - 24f;
        float rightBoardEnd = BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 24f;

        if (leftBoardStart - leftEdge > 24f) {
            drawBambooSide(shape, leftEdge, leftBoardStart, worldH, elapsedAnimTime, activeCombo, clearSurge, false);
        }
        if (rightEdge - rightBoardEnd > 24f) {
            drawBambooSide(shape, rightBoardEnd, rightEdge, worldH, elapsedAnimTime, activeCombo, clearSurge, true);
        }
    }

    private void drawBambooSide(ShapeRenderer shape, float minX, float maxX, float worldH,
                                float elapsedAnimTime, int activeCombo, float clearSurge, boolean rightSide) {
        float laneW = maxX - minX;
        float speedFactor = 1.0f + (activeCombo - 1) * 0.35f + clearSurge * 1.4f;
        float pulse = 0.65f + 0.35f * MathUtils.sin(elapsedAnimTime * (3.2f + activeCombo * 0.6f) + clearSurge * 4f);

        int stalks = 6;
        for (int i = 0; i < stalks; i++) {
            float ratio = (i + 0.5f) / stalks;
            float sx = minX + ratio * laneW;
            float sway = MathUtils.sin(elapsedAnimTime * 1.4f + i * 1.3f + (rightSide ? 2f : 0f)) * 5f;
            float width = 5f + (i % 2) * 3f;
            shape.setColor(i % 2 == 0 ? bambooDark : bambooLight);
            shape.rect(sx + sway, 0f, width, worldH);

            for (float y = 70f; y < worldH; y += 135f) {
                shape.setColor(goldTrim.r, goldTrim.g, goldTrim.b, 0.22f);
                shape.rect(sx + sway - 2f, y, width + 4f, 3f);
            }
        }

        float pillarX = rightSide ? minX + 6f : maxX - 14f;
        shape.setColor(pillarGreen);
        shape.rect(pillarX, 0f, 8f, worldH);
        shape.setColor(goldTrim);
        shape.rect(pillarX - 3f, 895f, 14f, 12f);
        shape.rect(pillarX - 3f, 110f, 14f, 12f);

        float lanternX = rightSide ? pillarX + 18f : pillarX - 12f;
        for (int i = 0; i < 3; i++) {
            float baseY = 740f - i * 250f;
            float bobY = baseY + MathUtils.sin(elapsedAnimTime * 2.2f + i * 1.7f) * 9f;
            float radius = 14f + clearSurge * 4f;

            shape.setColor(lanternGlow.r, lanternGlow.g, lanternGlow.b, 0.18f * pulse);
            shape.circle(lanternX, bobY, radius + 10f, 24);

            shape.setColor(i % 2 == 0 ? lanternRed : lanternGlow);
            shape.circle(lanternX, bobY, radius, 24);

            shape.setColor(goldTrim);
            shape.circle(lanternX, bobY, radius * 0.42f, 20);
        }

        for (int p = 0; p < 22; p++) {
            float progress = ((elapsedAnimTime * (0.08f + (p % 4) * 0.025f) * speedFactor) + p * 0.13f) % 1.0f;
            float py = progress * worldH;
            float px = minX + 16f + ((p * 53) % Math.max(24, (int) (laneW - 32f)))
                    + MathUtils.sin(elapsedAnimTime * 2.5f + p) * 10f;
            float pSize = 3f + (p % 3) * 2f + clearSurge * 2f;

            shape.setColor(goldTrim.r, goldTrim.g, goldTrim.b, 0.24f + 0.25f * pulse);
            shape.circle(px, py, pSize, 14);
        }
    }

    public void drawFlyingTiles(SpriteBatch batch, float worldW, float worldH, float elapsedAnimTime,
                                int activeCombo, float clearSurge) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return;
        }

        float leftEdge = (BoardRenderer.VIEW_W - worldW) * 0.5f + 16f;
        float leftBoardStart = BoardRenderer.BOARD_X - 32f;
        float rightBoardEnd = BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 32f;
        float rightEdge = (BoardRenderer.VIEW_W + worldW) * 0.5f - 16f;

        if (leftBoardStart - leftEdge > 36f) {
            drawSideFlyingTiles(batch, tiles, leftEdge, leftBoardStart, worldH, elapsedAnimTime, activeCombo, clearSurge, false);
        }
        if (rightEdge - rightBoardEnd > 36f) {
            drawSideFlyingTiles(batch, tiles, rightBoardEnd, rightEdge, worldH, elapsedAnimTime, activeCombo, clearSurge, true);
        }
    }

    private void drawSideFlyingTiles(SpriteBatch batch, TextureRegion[] tiles, float minX, float maxX, float worldH,
                                     float elapsedAnimTime, int activeCombo, float clearSurge, boolean rightSide) {
        float laneW = maxX - minX;
        float speedFactor = 1.0f + (activeCombo - 1) * 0.25f + clearSurge * 0.9f;

        for (int i = 0; i < 9; i++) {
            float cycle = ((elapsedAnimTime * (0.06f + (i % 3) * 0.02f) * speedFactor) + i * 0.11f) % 1.0f;
            float y = rightSide ? (worldH - cycle * (worldH + 80f) + 40f) : (cycle * (worldH + 80f) - 40f);
            float size = 34f + (i % 3) * 6f;
            float ratio = (i + 0.5f) / 9f;
            float centerX = minX + ratio * laneW + MathUtils.sin(elapsedAnimTime * 1.5f + i * 1.3f) * 24f;
            float x = MathUtils.clamp(centerX - size * 0.5f, minX, maxX - size);
            float rotation = MathUtils.sin(elapsedAnimTime * 1.8f + i) * 20f;

            TextureRegion reg = tiles[(i + (rightSide ? 5 : 0)) % tiles.length];
            batch.setColor(1f, 1f, 1f, 0.36f + clearSurge * 0.22f);
            batch.draw(reg, x, y, size * 0.5f, size * 0.5f, size, size, 1f, 1f, rotation);
        }
        batch.setColor(Color.WHITE);
    }
}