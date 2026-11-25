package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoardEffectManager {

    private static class ClearFlash {
        final int row;
        final int col;
        float timer;
        final float duration;

        ClearFlash(int row, int col, float duration) {
            this.row = row;
            this.col = col;
            this.timer = duration;
            this.duration = duration;
        }
    }

    private static class BurstTile {
        final TextureRegion region;
        float x;
        float y;
        final float vx;
        float vy;
        float rotation;
        final float vRot;
        float timer;
        final float duration;

        BurstTile(TextureRegion region, float x, float y, float vx, float vy, float vRot, float duration) {
            this.region = region;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.rotation = 0f;
            this.vRot = vRot;
            this.timer = duration;
            this.duration = duration;
        }
    }

    private static class FloatingText {
        final String text;
        float x;
        float y;
        final Color color;
        float timer;
        final float duration;

        FloatingText(String text, float x, float y, Color color, float duration) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = new Color(color);
            this.timer = duration;
            this.duration = duration;
        }
    }

    private final List<ClearFlash> clearFlashes = new ArrayList<>();
    private final List<BurstTile> burstTiles = new ArrayList<>();
    private final List<FloatingText> floatingTexts = new ArrayList<>();
    private final float[] slotSpawnTimers = new float[3];
    private float clearSurge = 0f;

    public void notifySlotSpawned(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < slotSpawnTimers.length) {
            slotSpawnTimers[slotIndex] = 0.28f;
        }
    }

    public float getSlotSpawnScale(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= slotSpawnTimers.length || slotSpawnTimers[slotIndex] <= 0f) {
            return 1f;
        }
        float p = 1f - (slotSpawnTimers[slotIndex] / 0.28f);
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1f + c3 * (float) Math.pow(p - 1f, 3) + c1 * (float) Math.pow(p - 1f, 2);
    }

    public float getClearSurge() {
        return clearSurge;
    }

    public void triggerLineClearAnimation(int[][] clearedCells, int earnedPoints, Color accentColor) {
        if (clearedCells == null) {
            return;
        }
        clearSurge = 1f;
        float sumX = 0f;
        float sumY = 0f;
        int count = 0;

        for (int r = 0; r < BoardMetrics.GRID_SIZE; r++) {
            for (int c = 0; c < BoardMetrics.GRID_SIZE; c++) {
                int tileId = clearedCells[r][c];
                if (tileId > 0) {
                    clearFlashes.add(new ClearFlash(r, c, 0.42f));
                    float cx = BoardMetrics.BOARD_X + (c + 0.5f) * BoardMetrics.CELL;
                    float cy = BoardMetrics.BOARD_Y + (BoardMetrics.GRID_SIZE - 0.5f - r) * BoardMetrics.CELL;
                    sumX += cx;
                    sumY += cy;
                    count++;

                    TextureRegion reg = BoardPalette.getTileRegion(tileId);
                    if (reg != null) {
                        float vx = MathUtils.random(-260f, 260f);
                        float vy = MathUtils.random(180f, 460f);
                        float vRot = MathUtils.random(-360f, 360f);
                        burstTiles.add(new BurstTile(reg, cx, cy, vx, vy, vRot, 0.78f));
                    }
                }
            }
        }

        if (count > 0 && earnedPoints > 0) {
            spawnFloatingText("+" + earnedPoints, (sumX / count) - 45f, (sumY / count) + 12f, accentColor);
        }
    }

    public void spawnFloatingText(String text, float x, float y, Color color) {
        floatingTexts.add(new FloatingText(text, x, y, color, 0.95f));
    }

    public void update(float delta) {
        clearSurge = Math.max(0f, clearSurge - delta * 2.2f);
        for (int i = 0; i < slotSpawnTimers.length; i++) {
            if (slotSpawnTimers[i] > 0f) {
                slotSpawnTimers[i] = Math.max(0f, slotSpawnTimers[i] - delta);
            }
        }

        Iterator<ClearFlash> flashIt = clearFlashes.iterator();
        while (flashIt.hasNext()) {
            ClearFlash f = flashIt.next();
            f.timer -= delta;
            if (f.timer <= 0f) {
                flashIt.remove();
            }
        }

        Iterator<BurstTile> burstIt = burstTiles.iterator();
        while (burstIt.hasNext()) {
            BurstTile b = burstIt.next();
            b.timer -= delta;
            b.x += b.vx * delta;
            b.y += b.vy * delta;
            b.vy -= 980f * delta;
            b.rotation += b.vRot * delta;
            if (b.timer <= 0f) {
                burstIt.remove();
            }
        }

        Iterator<FloatingText> textIt = floatingTexts.iterator();
        while (textIt.hasNext()) {
            FloatingText t = textIt.next();
            t.timer -= delta;
            t.y += 72f * delta;
            if (t.timer <= 0f) {
                textIt.remove();
            }
        }
    }

    public void drawClearFlashes(ShapeRenderer shape) {
        for (ClearFlash f : clearFlashes) {
            float alpha = Math.max(0f, f.timer / f.duration);
            float expand = (1f - alpha) * 14f;
            float x = BoardMetrics.BOARD_X + f.col * BoardMetrics.CELL - expand * 0.5f;
            float y = BoardMetrics.BOARD_Y + (BoardMetrics.GRID_SIZE - 1 - f.row) * BoardMetrics.CELL - expand * 0.5f;
            shape.setColor(1f, 0.94f, 0.65f, alpha * 0.72f);
            shape.rect(x, y, BoardMetrics.CELL + expand, BoardMetrics.CELL + expand);
        }
    }

    public void drawBurstTiles(SpriteBatch batch) {
        for (BurstTile b : burstTiles) {
            float alpha = Math.max(0f, b.timer / b.duration);
            float size = (BoardMetrics.CELL - 16f) * (0.55f + 0.45f * alpha);
            batch.setColor(1f, 1f, 1f, alpha);
            batch.draw(b.region, b.x - size * 0.5f, b.y - size * 0.5f,
                    size * 0.5f, size * 0.5f, size, size, 1f, 1f, b.rotation);
        }
        batch.setColor(Color.WHITE);
    }

    public void drawFloatingTexts(SpriteBatch batch, BitmapFont bigFont) {
        for (FloatingText t : floatingTexts) {
            float alpha = Math.max(0f, t.timer / t.duration);
            float scale = (2.15f + (1f - alpha) * 0.45f) * VectorFontFactory.BIG_BASE_SCALE;
            bigFont.getData().setScale(scale);
            bigFont.setColor(0f, 0f, 0f, alpha * 0.75f);
            bigFont.draw(batch, t.text, t.x + 2.5f, t.y - 2.5f);
            bigFont.setColor(t.color.r, t.color.g, t.color.b, alpha);
            bigFont.draw(batch, t.text, t.x, t.y);
        }
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
        bigFont.setColor(Color.WHITE);
    }
}