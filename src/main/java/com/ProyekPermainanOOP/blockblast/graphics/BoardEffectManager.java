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

    private static class CellFlash {
        final int row;
        final int col;
        float progress;

        CellFlash(int row, int col) {
            this.row = row;
            this.col = col;
            this.progress = 0f;
        }
    }

    private static class FloatingText {
        final String text;
        float x;
        float y;
        float age;
        final float duration = 0.95f;
        final Color color;

        FloatingText(String text, float x, float y, Color color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.age = 0f;
            this.color = color.cpy();
        }
    }

    private static class BurstTile {
        float x;
        float y;
        float vx;
        float vy;
        float rotation;
        float rotSpeed;
        float size;
        float age;
        final float duration;
        final int tileIndex;

        BurstTile(float x, float y, float vx, float vy, float rotSpeed, float size, float duration, int tileIndex) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.rotation = MathUtils.random(0f, 360f);
            this.rotSpeed = rotSpeed;
            this.size = size;
            this.age = 0f;
            this.duration = duration;
            this.tileIndex = tileIndex;
        }
    }

    private final List<CellFlash> flashes = new ArrayList<>();
    private final List<FloatingText> floatingTexts = new ArrayList<>();
    private final List<BurstTile> burstTiles = new ArrayList<>();
    private final float[] slotSpawnScale = {1f, 1f, 1f};
    private final float[] slotSpawnAge = {0f, 0f, 0f};
    private float clearSurge = 0f;

    public void notifySlotSpawned(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < slotSpawnScale.length) {
            slotSpawnAge[slotIndex] = 0.001f;
            slotSpawnScale[slotIndex] = 0.25f;
        }
    }

    public float getSlotSpawnScale(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < slotSpawnScale.length) {
            return slotSpawnScale[slotIndex];
        }
        return 1f;
    }

    public float getClearSurge() {
        return clearSurge;
    }

    public void triggerLineClearAnimation(int[][] clearedCells, int earnedPoints, Color textColor) {
        clearSurge = 1f;
        if (clearedCells != null) {
            for (int[] cell : clearedCells) {
                if (cell != null && cell.length >= 2) {
                    flashes.add(new CellFlash(cell[0], cell[1]));
                }
            }
        }
        if (earnedPoints > 0) {
            floatingTexts.add(new FloatingText("+" + earnedPoints,
                    BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX / 2f - 36f,
                    BoardRenderer.BOARD_Y + BoardRenderer.BOARD_PX / 2f,
                    textColor));
        }

        for (int i = 0; i < 18; i++) {
            int side = (i % 2 == 0) ? -1 : 1;
            float startX = (side < 0) ? (BoardRenderer.BOARD_X - 10f) : (BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 10f);
            float startY = BoardRenderer.BOARD_Y + MathUtils.random(60f, BoardRenderer.BOARD_PX - 60f);
            float vx = side * MathUtils.random(140f, 340f);
            float vy = MathUtils.random(120f, 320f);
            float rotSpeed = MathUtils.random(-260f, 260f);
            float size = MathUtils.random(24f, 38f);
            float duration = MathUtils.random(1.1f, 1.7f);
            burstTiles.add(new BurstTile(startX, startY, vx, vy, rotSpeed, size, duration, i));
        }
    }

    public void spawnFloatingText(String text, float x, float y, Color color) {
        floatingTexts.add(new FloatingText(text, x, y, color));
    }

    public void update(float delta) {
        clearSurge = Math.max(0f, clearSurge - delta * 1.4f);

        for (int i = 0; i < slotSpawnAge.length; i++) {
            if (slotSpawnAge[i] > 0f) {
                slotSpawnAge[i] += delta * 4.2f;
                float t = Math.min(1f, slotSpawnAge[i]);
                float bounce = 1f + 0.22f * (float) Math.sin(t * Math.PI) * (1f - t);
                slotSpawnScale[i] = t * bounce;
                if (t >= 1f) {
                    slotSpawnAge[i] = 0f;
                    slotSpawnScale[i] = 1f;
                }
            }
        }

        Iterator<CellFlash> flashIt = flashes.iterator();
        while (flashIt.hasNext()) {
            CellFlash cf = flashIt.next();
            cf.progress += delta * 2.4f;
            if (cf.progress >= 1f) {
                flashIt.remove();
            }
        }

        Iterator<FloatingText> textIt = floatingTexts.iterator();
        while (textIt.hasNext()) {
            FloatingText ft = textIt.next();
            ft.age += delta;
            ft.y += delta * 52f;
            if (ft.age >= ft.duration) {
                textIt.remove();
            }
        }

        Iterator<BurstTile> burstIt = burstTiles.iterator();
        while (burstIt.hasNext()) {
            BurstTile bt = burstIt.next();
            bt.age += delta;
            bt.x += bt.vx * delta;
            bt.y += bt.vy * delta;
            bt.vy -= 280f * delta;
            bt.rotation += bt.rotSpeed * delta;
            if (bt.age >= bt.duration) {
                burstIt.remove();
            }
        }
    }

    public void drawClearFlashes(ShapeRenderer shape) {
        for (CellFlash cf : flashes) {
            float alpha = Math.max(0f, 1f - cf.progress);
            float shrink = cf.progress * (BoardRenderer.CELL * 0.55f);
            float x = BoardRenderer.BOARD_X + cf.col * BoardRenderer.CELL + shrink * 0.5f;
            float y = BoardRenderer.BOARD_Y + (BoardRenderer.GRID_SIZE - 1 - cf.row) * BoardRenderer.CELL + shrink * 0.5f;
            float size = Math.max(4f, BoardRenderer.CELL - shrink);
            shape.setColor(1f, 0.86f, 0.35f, alpha);
            shape.rect(x, y, size, size);
        }
    }

    public void drawBurstTiles(SpriteBatch batch) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return;
        }
        for (BurstTile bt : burstTiles) {
            TextureRegion reg = tiles[bt.tileIndex % tiles.length];
            float alpha = Math.max(0f, 1f - (bt.age / bt.duration));
            batch.setColor(1f, 0.92f, 0.65f, alpha);
            batch.draw(reg, bt.x - bt.size * 0.5f, bt.y - bt.size * 0.5f,
                    bt.size * 0.5f, bt.size * 0.5f, bt.size, bt.size, 1f, 1f, bt.rotation);
        }
        batch.setColor(Color.WHITE);
    }

    public void drawFloatingTexts(SpriteBatch batch, BitmapFont font) {
        for (FloatingText ft : floatingTexts) {
            float alpha = Math.max(0f, 1f - (ft.age / ft.duration));
            font.setColor(ft.color.r, ft.color.g, ft.color.b, alpha);
            font.draw(batch, ft.text, ft.x, ft.y);
        }
    }
}