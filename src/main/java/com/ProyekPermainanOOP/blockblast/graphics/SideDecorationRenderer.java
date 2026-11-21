package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

public class SideDecorationRenderer {

    private static class SideParticle {
        final int side;
        final float ratioFromBoard;
        final float speedY;
        final float phase;
        final float radius;
        final Color color;

        SideParticle(int side, float ratioFromBoard, float speedY, float phase, float radius, Color color) {
            this.side = side;
            this.ratioFromBoard = ratioFromBoard;
            this.speedY = speedY;
            this.phase = phase;
            this.radius = radius;
            this.color = color;
        }
    }

    private static class FlyingRelicTile {
        final int side;
        final float wingRatio;
        final float speedY;
        final float swayAmp;
        final float swayFreq;
        final float rotSpeed;
        final float baseRot;
        final float size;
        final float phase;
        final int tileIndex;
        final float alpha;

        FlyingRelicTile(int side, float wingRatio, float speedY, float swayAmp, float swayFreq,
                        float rotSpeed, float baseRot, float size, float phase, int tileIndex, float alpha) {
            this.side = side;
            this.wingRatio = wingRatio;
            this.speedY = speedY;
            this.swayAmp = swayAmp;
            this.swayFreq = swayFreq;
            this.rotSpeed = rotSpeed;
            this.baseRot = baseRot;
            this.size = size;
            this.phase = phase;
            this.tileIndex = tileIndex;
            this.alpha = alpha;
        }
    }

    private static class ThemePalette {
        final Color bgTop;
        final Color bgBottom;
        final Color sideOuterTop;
        final Color sideOuterBottom;
        final Color sideInnerTop;
        final Color sideInnerBottom;
        final Color trim;
        final Color lattice;
        final Color accent;

        ThemePalette(String bgTop, String bgBottom, String sideOuterTop, String sideOuterBottom,
                     String sideInnerTop, String sideInnerBottom, String trim, String lattice, String accent) {
            this.bgTop = Color.valueOf(bgTop);
            this.bgBottom = Color.valueOf(bgBottom);
            this.sideOuterTop = Color.valueOf(sideOuterTop);
            this.sideOuterBottom = Color.valueOf(sideOuterBottom);
            this.sideInnerTop = Color.valueOf(sideInnerTop);
            this.sideInnerBottom = Color.valueOf(sideInnerBottom);
            this.trim = Color.valueOf(trim);
            this.lattice = Color.valueOf(lattice);
            this.accent = Color.valueOf(accent);
        }
    }

    private final ThemePalette[] themes = {
            new ThemePalette("19785A", "093226", "11543F", "062119", "1E7A5D", "0B382B", "2E9A76", "237A5D", "7CF5C4"),
            new ThemePalette("1B4F7A", "0A2136", "123857", "061421", "215C8C", "0D2840", "327BB5", "256294", "7CC8F5"),
            new ThemePalette("7A1E2C", "330A11", "54131D", "21060A", "8C2636", "3D0E16", "B53A4D", "942B3B", "FF9E44"),
            new ThemePalette("785C19", "322509", "543F11", "211806", "8C6C21", "3D2E0B", "B89032", "967324", "FFF099")
    };

    private final Color goldAccent = Color.valueOf("F3C65F");
    private final Color amberGlow = Color.valueOf("FF9E44");
    private final Color crimsonSilk = Color.valueOf("C83E4D");

    private final List<SideParticle> sideParticles = new ArrayList<>();
    private final List<FlyingRelicTile> flyingTiles = new ArrayList<>();

    public SideDecorationRenderer() {
        Color[] palette = {
                new Color(0.95f, 0.78f, 0.37f, 0.55f),
                new Color(0.49f, 0.96f, 0.77f, 0.48f),
                new Color(1.00f, 0.62f, 0.27f, 0.50f),
                new Color(0.85f, 0.95f, 0.88f, 0.40f)
        };
        for (int i = 0; i < 48; i++) {
            int side = (i % 2 == 0) ? -1 : 1;
            float ratio = 0.05f + ((i * 37) % 92) / 100f;
            float speed = 32f + (i % 6) * 15f;
            float phase = i * 0.61f;
            float radius = 3.5f + (i % 4) * 3.5f;
            sideParticles.add(new SideParticle(side, ratio, speed, phase, radius, palette[i % palette.length]));
        }

        for (int i = 0; i < 28; i++) {
            int side = (i % 2 == 0) ? -1 : 1;
            float wingRatio = 0.10f + ((i * 29) % 82) / 100f;
            float speedY = 42f + (i % 5) * 18f;
            float swayAmp = 18f + (i % 4) * 11f;
            float swayFreq = 1.1f + (i % 3) * 0.45f;
            float rotSpeed = ((i % 2 == 0) ? 1f : -1f) * (18f + (i % 4) * 12f);
            float baseRot = (i * 47f) % 360f;
            float size = 28f + (i % 4) * 8f;
            float phase = i * 1.17f;
            float alpha = 0.42f + (i % 3) * 0.16f;
            flyingTiles.add(new FlyingRelicTile(side, wingRatio, speedY, swayAmp, swayFreq,
                    rotSpeed, baseRot, size, phase, i, alpha));
        }
    }

    public void drawShapes(ShapeRenderer shape, float worldW, float worldH, float elapsedAnimTime,
                           int activeCombo, float clearSurge, int currentScore) {
        ThemePalette theme = themes[(Math.max(0, currentScore) / 1000) % themes.length];

        float halfW = worldW * 0.5f;
        float halfH = worldH * 0.5f;
        float centerX = BoardRenderer.VIEW_W * 0.5f;
        float centerY = BoardRenderer.VIEW_H * 0.5f;
        float minX = centerX - halfW - 40f;
        float maxX = centerX + halfW + 40f;
        float minY = centerY - halfH - 40f;
        float maxY = centerY + halfH + 40f;
        float fullH = maxY - minY;

        shape.rect(minX, minY, (BoardRenderer.BOARD_X - 16f) - minX, fullH, theme.sideOuterBottom, theme.sideInnerBottom, theme.sideOuterTop, theme.sideInnerTop);
        shape.rect(BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 16f, minY, maxX - (BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 16f), fullH, theme.sideInnerBottom, theme.sideOuterBottom, theme.sideInnerTop, theme.sideOuterTop);
        shape.rect(BoardRenderer.BOARD_X - 16f, minY, BoardRenderer.BOARD_PX + 32f, fullH, theme.bgBottom, theme.bgBottom, theme.bgTop, theme.bgTop);

        float leftEdge = BoardRenderer.BOARD_X - 24f;
        float rightEdge = BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 24f;
        float leftSpan = Math.max(160f, leftEdge - minX);
        float rightSpan = Math.max(160f, maxX - rightEdge);
        float energyBoost = Math.min(2.2f, 0.9f + activeCombo * 0.14f + clearSurge * 0.9f);

        for (float gx = leftEdge - 56f; gx > minX; gx -= 64f) {
            shape.setColor(theme.lattice.r, theme.lattice.g, theme.lattice.b, 0.24f);
            shape.rect(gx, minY, 3f, fullH);
        }
        for (float gx = rightEdge + 52f; gx < maxX; gx += 64f) {
            shape.setColor(theme.lattice.r, theme.lattice.g, theme.lattice.b, 0.24f);
            shape.rect(gx, minY, 3f, fullH);
        }

        drawAstrolabeWheels(shape, leftEdge - leftSpan * 0.5f, centerY, Math.min(leftSpan * 0.42f, 190f), 1f, elapsedAnimTime, clearSurge, theme.accent);
        drawAstrolabeWheels(shape, rightEdge + rightSpan * 0.5f, centerY, Math.min(rightSpan * 0.42f, 190f), -1f, elapsedAnimTime, clearSurge, theme.accent);

        drawJadeDragonStream(shape, leftEdge - leftSpan * 0.35f, minY, fullH, -1, elapsedAnimTime, energyBoost, clearSurge, theme.accent);
        drawJadeDragonStream(shape, rightEdge + rightSpan * 0.35f, minY, fullH, 1, elapsedAnimTime, energyBoost, clearSurge, theme.accent);

        shape.setColor(theme.trim);
        shape.rect(leftEdge - 22f, minY + 30f, 14f, fullH - 60f);
        shape.rect(rightEdge + 8f, minY + 30f, 14f, fullH - 60f);

        shape.setColor(goldAccent);
        shape.rect(leftEdge - 26f, 46f, 22f, 12f);
        shape.rect(leftEdge - 26f, BoardRenderer.VIEW_H - 58f, 22f, 12f);
        shape.rect(rightEdge + 4f, 46f, 22f, 12f);
        shape.rect(rightEdge + 4f, BoardRenderer.VIEW_H - 58f, 22f, 12f);

        for (int i = 0; i < 5; i++) {
            float lanternY = 140f + i * 180f;
            float swing = (float) Math.sin(elapsedAnimTime * 2.4f + i * 1.3f) * 14f;
            float pulseR = 14f + 4f * (float) Math.sin(elapsedAnimTime * 3.4f + i) + clearSurge * 5f;

            float lx = leftEdge - 58f + swing;
            float rx = rightEdge + 58f - swing;

            shape.setColor(goldAccent.r, goldAccent.g, goldAccent.b, 0.24f);
            shape.circle(lx, lanternY, pulseR + 9f, 48);
            shape.circle(rx, lanternY, pulseR + 9f, 48);

            shape.setColor(i % 2 == 0 ? crimsonSilk : amberGlow);
            shape.circle(lx, lanternY, pulseR, 44);
            shape.circle(rx, lanternY, pulseR, 44);

            shape.setColor(goldAccent);
            shape.circle(lx, lanternY, pulseR * 0.42f, 36);
            shape.circle(rx, lanternY, pulseR * 0.42f, 36);
        }

        for (SideParticle p : sideParticles) {
            float y = minY + ((p.phase * 150f + elapsedAnimTime * p.speedY * energyBoost) % fullH);
            float wobble = (float) Math.sin(elapsedAnimTime * 2.3f + p.phase) * 22f;
            float x = (p.side < 0)
                    ? (leftEdge - 25f - p.ratioFromBoard * (leftSpan - 35f) + wobble)
                    : (rightEdge + 25f + p.ratioFromBoard * (rightSpan - 35f) + wobble);
            float alphaPulse = 0.50f + 0.50f * (float) Math.sin(elapsedAnimTime * 4.2f + p.phase * 2f);
            shape.setColor(p.color.r, p.color.g, p.color.b, p.color.a * alphaPulse);
            shape.circle(x, y, p.radius, 32);
        }
    }

    public void drawFlyingTiles(SpriteBatch batch, float worldW, float worldH, float elapsedAnimTime, int activeCombo, float clearSurge) {
        TextureRegion[] tiles = GameAssets.chineseCharTiles;
        if (tiles == null || tiles.length == 0) {
            return;
        }

        float halfW = worldW * 0.5f;
        float halfH = worldH * 0.5f;
        float leftEdge = BoardRenderer.BOARD_X - 24f;
        float rightEdge = BoardRenderer.BOARD_X + BoardRenderer.BOARD_PX + 24f;
        float minX = (BoardRenderer.VIEW_W * 0.5f) - halfW;
        float minY = (BoardRenderer.VIEW_H * 0.5f) - halfH - 60f;
        float fullH = worldH + 120f;
        float leftSpan = Math.max(140f, leftEdge - minX);
        float speedBoost = Math.min(2.2f, 1f + activeCombo * 0.12f + clearSurge * 0.8f);

        for (FlyingRelicTile ft : flyingTiles) {
            TextureRegion reg = tiles[ft.tileIndex % tiles.length];
            float yPos = minY + ((ft.phase * 190f + elapsedAnimTime * ft.speedY * speedBoost) % fullH);
            float sway = (float) Math.sin(elapsedAnimTime * ft.swayFreq + ft.phase) * ft.swayAmp;
            float baseX = (ft.side < 0)
                    ? (leftEdge - 36f - ft.wingRatio * (leftSpan - 44f))
                    : (rightEdge + 36f + ft.wingRatio * (leftSpan - 44f));
            float drawX = baseX + sway - ft.size * 0.5f;
            float rotation = ft.baseRot + elapsedAnimTime * ft.rotSpeed;

            batch.setColor(1f, 0.97f, 0.88f, ft.alpha);
            batch.draw(reg, drawX, yPos, ft.size * 0.5f, ft.size * 0.5f,
                    ft.size, ft.size, 1f, 1f, rotation);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawAstrolabeWheels(ShapeRenderer shape, float cx, float cy, float maxRadius, float dir,
                                     float elapsedAnimTime, float clearSurge, Color ringAccent) {
        if (maxRadius < 50f) {
            return;
        }
        float[] radii = {maxRadius, maxRadius * 0.68f, maxRadius * 0.36f};
        for (int ring = 0; ring < radii.length; ring++) {
            float r = radii[ring];
            int nodes = 12 - ring * 3;
            float rot = elapsedAnimTime * (0.35f + ring * 0.25f) * dir * (ring % 2 == 0 ? 1f : -1f);
            for (int n = 0; n < nodes; n++) {
                float angle = rot + (MathUtils.PI2 * n) / nodes;
                float nx = cx + MathUtils.cos(angle) * r;
                float ny = cy + MathUtils.sin(angle) * r;
                if (ring == 0) {
                    shape.setColor(goldAccent.r, goldAccent.g, goldAccent.b, 0.24f + clearSurge * 0.2f);
                    shape.circle(nx, ny, 5.5f, 28);
                } else if (ring == 1) {
                    shape.setColor(ringAccent.r, ringAccent.g, ringAccent.b, 0.26f);
                    shape.circle(nx, ny, 4.5f, 28);
                } else {
                    shape.setColor(amberGlow.r, amberGlow.g, amberGlow.b, 0.30f);
                    shape.circle(nx, ny, 4f, 28);
                }
            }
        }
    }

    private void drawJadeDragonStream(ShapeRenderer shape, float baseX, float minY, float fullH, int side,
                                      float elapsedAnimTime, float energyBoost, float clearSurge, Color streamAccent) {
        int segments = 32;
        for (int s = 0; s < segments; s++) {
            float progress = (float) s / segments;
            float y = minY + ((progress * fullH + elapsedAnimTime * 95f * energyBoost) % fullH);
            float wave = (float) Math.sin(progress * 10f + elapsedAnimTime * 3.2f * energyBoost + side) * 46f;
            float size = 6f + 6f * (float) Math.sin(progress * MathUtils.PI);
            Color streamCol = (s % 3 == 0) ? goldAccent : streamAccent;
            shape.setColor(streamCol.r, streamCol.g, streamCol.b, 0.22f + clearSurge * 0.25f);
            shape.circle(baseX + wave, y, size, 32);
        }
    }
}