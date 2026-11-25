package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.ProyekPermainanOOP.blockblast.model.PusakaCompendium;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class MainMenuRenderer implements Disposable {

    public static final float PANEL_W = 550f;
    public static final float PANEL_H = 740f;
    public static final float PANEL_X = (BoardMetrics.VIEW_W - PANEL_W) * 0.5f;
    public static final float PANEL_Y = (BoardMetrics.VIEW_H - PANEL_H) * 0.5f;

    public static final float BTN_W = 436f;
    public static final float BTN_H = 58f;
    public static final float BTN_X = (BoardMetrics.VIEW_W - BTN_W) * 0.5f;
    public static final float FIRST_BTN_Y = PANEL_Y + PANEL_H - 278f;
    public static final float BTN_GAP = 14f;

    public static final Rectangle BTN_PLAY = new Rectangle(BTN_X, FIRST_BTN_Y, BTN_W, BTN_H);
    public static final Rectangle BTN_MODE = new Rectangle(BTN_X, FIRST_BTN_Y - (BTN_H + BTN_GAP), BTN_W, BTN_H);
    public static final Rectangle BTN_COMPENDIUM = new Rectangle(BTN_X, FIRST_BTN_Y - 2f * (BTN_H + BTN_GAP), BTN_W, BTN_H);
    public static final Rectangle BTN_LANGUAGE = new Rectangle(BTN_X, FIRST_BTN_Y - 3f * (BTN_H + BTN_GAP), BTN_W, BTN_H);
    public static final Rectangle BTN_EXIT = new Rectangle(BTN_X, FIRST_BTN_Y - 4f * (BTN_H + BTN_GAP), BTN_W, BTN_H);

    private static final Color JADE_DARK = new Color(0.03f, 0.16f, 0.12f, 1f);
    private static final Color JADE_SCALE = new Color(0.11f, 0.62f, 0.46f, 1f);
    private static final Color JADE_BRIGHT = new Color(0.32f, 0.94f, 0.72f, 1f);
    private static final Color DRAGON_FIRE = new Color(1.0f, 0.55f, 0.12f, 1f);
    private static final Color EXIT_RED = new Color(0.92f, 0.28f, 0.30f, 1f);
    private static final Color EXIT_BG_TOP = new Color(0.28f, 0.08f, 0.10f, 0.96f);
    private static final Color EXIT_BG_BOTTOM = new Color(0.16f, 0.04f, 0.06f, 0.98f);

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont hudFont = VectorFontFactory.createHudFont();
    private final BitmapFont bigFont = VectorFontFactory.createBigFont();
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private float animTime = 0f;

    public MainMenuRenderer() {
        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
    }

    public void render(Matrix4 projection, GameMode selectedMode, int highScore,
                       float hoverX, float hoverY, LocalizationManager localization) {
        animTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        drawBackdropAndMagicRings(shape);
        drawAnimatedDragon(shape);
        drawEmbers(shape);
        drawMenuCardAndButtons(shape, hoverX, hoverY);
        shape.end();

        batch.setProjectionMatrix(projection);
        batch.begin();
        drawTexts(batch, hudFont, bigFont, glyphLayout, highScore, selectedMode, hoverX, hoverY, localization);
        batch.end();
    }

    private void drawBackdropAndMagicRings(ShapeRenderer shape) {
        shape.setColor(0.01f, 0.06f, 0.04f, 0.68f);
        shape.rect(-1200f, 0f, BoardMetrics.VIEW_W + 2400f, BoardMetrics.VIEW_H);

        float cx = BoardMetrics.VIEW_W * 0.5f;
        float cy = BoardMetrics.VIEW_H * 0.5f;

        float beamPulse = 0.08f + 0.04f * MathUtils.sin(animTime * 2.2f);
        shape.setColor(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, beamPulse);
        shape.rect(cx - 210f, 0f, 420f, BoardMetrics.VIEW_H);
        shape.setColor(JADE_BRIGHT.r, JADE_BRIGHT.g, JADE_BRIGHT.b, beamPulse * 0.8f);
        shape.rect(cx - 110f, 0f, 220f, BoardMetrics.VIEW_H);

        drawAstrolabeRing(shape, cx, cy, 415f, 8f, animTime * 0.35f,
                new Color(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, 0.34f));
        drawAstrolabeRing(shape, cx, cy, 355f, 6f, -animTime * 0.55f,
                new Color(JADE_BRIGHT.r, JADE_BRIGHT.g, JADE_BRIGHT.b, 0.28f));
    }

    private void drawAstrolabeRing(ShapeRenderer shape, float cx, float cy, float radius, float thickness, float angleOffset, Color color) {
        shape.setColor(color);
        int segments = 48;
        float step = MathUtils.PI2 / segments;
        for (int i = 0; i < segments; i++) {
            float a1 = i * step + angleOffset;
            float a2 = (i + 1) * step + angleOffset;
            float x1 = cx + MathUtils.cos(a1) * radius;
            float y1 = cy + MathUtils.sin(a1) * radius;
            float x2 = cx + MathUtils.cos(a2) * radius;
            float y2 = cy + MathUtils.sin(a2) * radius;
            shape.rectLine(x1, y1, x2, y2, thickness);

            if (i % 6 == 0) {
                float ox = cx + MathUtils.cos(a1) * (radius + 18f);
                float oy = cy + MathUtils.sin(a1) * (radius + 18f);
                shape.circle(ox, oy, 6f, 12);
            }
        }
    }

    private void drawAnimatedDragon(ShapeRenderer shape) {
        float cx = BoardMetrics.VIEW_W * 0.5f;
        float cy = BoardMetrics.VIEW_H * 0.52f;
        int segmentCount = 36;
        float spacing = 0.13f;

        float[] segX = new float[segmentCount];
        float[] segY = new float[segmentCount];
        float[] segRadius = new float[segmentCount];

        for (int i = 0; i < segmentCount; i++) {
            float t = animTime * 1.45f - i * spacing;
            float slither = MathUtils.sin(t * 3.2f) * 34f;
            float rx = 345f + slither;
            float ry = 415f + MathUtils.cos(t * 2.4f) * 26f;

            segX[i] = cx + MathUtils.cos(t) * rx;
            segY[i] = cy + MathUtils.sin(t) * ry;

            float progress = (float) i / (segmentCount - 1);
            float taper = (progress < 0.15f)
                    ? MathUtils.lerp(22f, 28f, progress / 0.15f)
                    : MathUtils.lerp(28f, 4f, (progress - 0.15f) / 0.85f);
            segRadius[i] = taper;
        }

        for (int i = segmentCount - 1; i >= 1; i--) {
            float x = segX[i];
            float y = segY[i];
            float r = segRadius[i];

            float dx = segX[i - 1] - x;
            float dy = segY[i - 1] - y;
            float angle = MathUtils.atan2(dy, dx);
            float nx = -MathUtils.sin(angle);
            float ny = MathUtils.cos(angle);

            shape.setColor(0f, 0f, 0f, 0.35f);
            shape.circle(x + 8f, y - 10f, r + 3f, 18);

            shape.setColor(JADE_DARK);
            shape.circle(x, y, r + 3.5f, 18);

            float shimmer = 0.5f + 0.5f * MathUtils.sin(animTime * 6f - i * 0.45f);
            Color bodyColor = JADE_SCALE.cpy().lerp(JADE_BRIGHT, shimmer * 0.45f);
            shape.setColor(bodyColor);
            shape.circle(x, y, r, 18);

            shape.setColor(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, 0.88f);
            shape.circle(x - nx * (r * 0.35f), y - ny * (r * 0.35f), r * 0.48f, 14);

            if (i % 2 == 0 && r > 8f) {
                float spikeTipX = x + nx * (r + 14f);
                float spikeTipY = y + ny * (r + 14f);
                float base1X = x + nx * (r * 0.6f) - MathUtils.cos(angle) * 6f;
                float base1Y = y + ny * (r * 0.6f) - MathUtils.sin(angle) * 6f;
                float base2X = x + nx * (r * 0.6f) + MathUtils.cos(angle) * 6f;
                float base2Y = y + ny * (r * 0.6f) + MathUtils.sin(angle) * 6f;
                shape.setColor(BoardPalette.GOLD_ACCENT);
                shape.triangle(spikeTipX, spikeTipY, base1X, base1Y, base2X, base2Y);
            }

            if (i == 7 || i == 21) {
                drawDragonClaw(shape, x, y, angle, 1f, r);
                drawDragonClaw(shape, x, y, angle, -1f, r);
            }
        }

        drawDragonHead(shape, segX[0], segY[0], segX[1], segY[1]);
    }

    private void drawDragonClaw(ShapeRenderer shape, float x, float y, float bodyAngle, float side, float radius) {
        float perp = bodyAngle + side * (MathUtils.PI * 0.55f);
        float elbowX = x + MathUtils.cos(perp) * (radius + 20f);
        float elbowY = y + MathUtils.sin(perp) * (radius + 20f);
        float handX = elbowX + MathUtils.cos(bodyAngle + side * 0.3f) * 18f;
        float handY = elbowY + MathUtils.sin(bodyAngle + side * 0.3f) * 18f;

        shape.setColor(JADE_SCALE);
        shape.rectLine(x, y, elbowX, elbowY, 8f);
        shape.rectLine(elbowX, elbowY, handX, handY, 6f);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        for (int c = -1; c <= 1; c++) {
            float clawAng = bodyAngle + side * 0.3f + c * 0.35f;
            shape.rectLine(handX, handY, handX + MathUtils.cos(clawAng) * 12f, handY + MathUtils.sin(clawAng) * 12f, 3f);
        }
    }

    private void drawDragonHead(ShapeRenderer shape, float hx, float hy, float neckX, float neckY) {
        float headAngle = MathUtils.atan2(hy - neckY, hx - neckX);
        float cos = MathUtils.cos(headAngle);
        float sin = MathUtils.sin(headAngle);
        float nx = -sin;
        float ny = cos;

        float snoutX = hx + cos * 38f;
        float snoutY = hy + sin * 38f;

        for (int f = 0; f < 6; f++) {
            float prog = (f + 1) / 6f;
            float wave = MathUtils.sin(animTime * 14f + f * 1.1f) * (6f * prog);
            float fx = snoutX + cos * (14f + f * 13f) + nx * wave;
            float fy = snoutY + sin * (14f + f * 13f) + ny * wave;
            float fr = MathUtils.lerp(13f, 3f, prog);
            Color fireCol = BoardPalette.GOLD_ACCENT.cpy().lerp(DRAGON_FIRE, prog);
            fireCol.a = (1f - prog * 0.75f) * 0.85f;
            shape.setColor(fireCol);
            shape.circle(fx, fy, fr, 12);
        }

        shape.setColor(BoardPalette.GOLD_ACCENT);
        for (int side = -1; side <= 1; side += 2) {
            float hornBaseX = hx - cos * 6f + nx * side * 14f;
            float hornBaseY = hy - sin * 6f + ny * side * 14f;
            float hornMidX = hornBaseX - cos * 26f + nx * side * 22f;
            float hornMidY = hornBaseY - sin * 26f + ny * side * 22f;
            float hornTipX = hornMidX - cos * 24f + nx * side * 10f;
            float hornTipY = hornMidY - sin * 24f + ny * side * 10f;
            shape.rectLine(hornBaseX, hornBaseY, hornMidX, hornMidY, 6.5f);
            shape.rectLine(hornMidX, hornMidY, hornTipX, hornTipY, 3.5f);

            float whiskerWave = MathUtils.sin(animTime * 7f + side) * 10f;
            float wStartX = hx + cos * 24f + nx * side * 12f;
            float wStartY = hy + sin * 24f + ny * side * 12f;
            float wEndX = wStartX - cos * 34f + nx * (side * 32f + whiskerWave);
            float wEndY = wStartY - sin * 34f + ny * (side * 32f + whiskerWave);
            shape.setColor(JADE_BRIGHT);
            shape.rectLine(wStartX, wStartY, wEndX, wEndY, 2.5f);
            shape.setColor(BoardPalette.GOLD_ACCENT);
        }

        shape.setColor(JADE_DARK);
        shape.circle(hx, hy, 27f, 20);
        shape.setColor(JADE_SCALE);
        shape.circle(hx, hy, 23f, 20);

        shape.triangle(
                snoutX + cos * 6f, snoutY + sin * 6f,
                hx + nx * 20f, hy + ny * 20f,
                hx - nx * 20f, hy - ny * 20f
        );

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.circle(snoutX, snoutY, 9f, 14);

        for (int side = -1; side <= 1; side += 2) {
            float eyeX = hx + cos * 11f + nx * side * 13f;
            float eyeY = hy + sin * 11f + ny * side * 13f;
            shape.setColor(DRAGON_FIRE);
            shape.circle(eyeX, eyeY, 6.5f, 12);
            shape.setColor(Color.WHITE);
            shape.circle(eyeX + cos * 1.5f, eyeY + sin * 1.5f, 3f, 10);
        }
    }

    private void drawEmbers(ShapeRenderer shape) {
        int emberCount = 26;
        for (int i = 0; i < emberCount; i++) {
            float seed = i * 137.5f;
            float speed = 38f + (i % 5) * 14f;
            float y = ((animTime * speed + seed * 7f) % (BoardMetrics.VIEW_H + 80f)) - 40f;
            float xBase = (BoardMetrics.VIEW_W * 0.5f) + MathUtils.sin(seed) * 460f;
            float x = xBase + MathUtils.sin(animTime * 2.1f + i) * 24f;
            float size = 2.5f + (i % 3) * 1.4f;
            float alpha = 0.35f + 0.45f * MathUtils.sin(animTime * 4f + i);

            if (i % 3 == 0) {
                shape.setColor(JADE_BRIGHT.r, JADE_BRIGHT.g, JADE_BRIGHT.b, alpha);
            } else {
                shape.setColor(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, alpha);
            }
            shape.circle(x, y, size, 10);
        }
    }

    private void drawMenuCardAndButtons(ShapeRenderer shape, float hoverX, float hoverY) {
        shape.setColor(0f, 0f, 0f, 0.65f);
        shape.rect(PANEL_X + 12f, PANEL_Y - 14f, PANEL_W, PANEL_H);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(PANEL_X - 5f, PANEL_Y - 5f, PANEL_W + 10f, PANEL_H + 10f);

        Color cardTop = new Color(0.05f, 0.24f, 0.18f, 0.94f);
        Color cardBottom = new Color(0.02f, 0.12f, 0.09f, 0.96f);
        shape.rect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, cardBottom, cardBottom, cardTop, cardTop);

        shape.setColor(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, 0.45f);
        shape.rect(PANEL_X + 12f, PANEL_Y + 12f, PANEL_W - 24f, 2f);
        shape.rect(PANEL_X + 12f, PANEL_Y + PANEL_H - 14f, PANEL_W - 24f, 2f);
        shape.rect(PANEL_X + 12f, PANEL_Y + 12f, 2f, PANEL_H - 24f);
        shape.rect(PANEL_X + PANEL_W - 14f, PANEL_Y + 12f, 2f, PANEL_H - 24f);

        float cornerSize = 18f;
        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(PANEL_X - 5f, PANEL_Y + PANEL_H - cornerSize + 5f, cornerSize, cornerSize);
        shape.rect(PANEL_X + PANEL_W - cornerSize + 5f, PANEL_Y + PANEL_H - cornerSize + 5f, cornerSize, cornerSize);
        shape.rect(PANEL_X - 5f, PANEL_Y - 5f, cornerSize, cornerSize);
        shape.rect(PANEL_X + PANEL_W - cornerSize + 5f, PANEL_Y - 5f, cornerSize, cornerSize);

        float pulse = 0.5f + 0.5f * MathUtils.sin(animTime * 4.5f);
        Rectangle[] buttons = {BTN_PLAY, BTN_MODE, BTN_COMPENDIUM, BTN_LANGUAGE, BTN_EXIT};

        for (int i = 0; i < buttons.length; i++) {
            Rectangle btn = buttons[i];
            boolean hovered = btn.contains(hoverX, hoverY);
            boolean isExit = (i == 4);

            Color border;
            Color top;
            Color bottom;

            if (isExit) {
                border = hovered ? Color.SALMON : EXIT_RED;
                top = hovered ? EXIT_RED.cpy().lerp(EXIT_BG_TOP, 0.55f) : EXIT_BG_TOP;
                bottom = EXIT_BG_BOTTOM;
            } else if (i == 0) {
                border = hovered ? Color.WHITE : BoardPalette.MINT_ACCENT;
                top = BoardPalette.EMPTY_TOP.cpy().lerp(BoardPalette.MINT_ACCENT, hovered ? 0.38f : 0.16f + pulse * 0.12f);
                bottom = BoardPalette.PANEL_BOTTOM;
            } else {
                border = hovered ? BoardPalette.MINT_ACCENT : BoardPalette.GOLD_ACCENT;
                top = hovered ? BoardPalette.EMPTY_TOP.cpy().lerp(BoardPalette.GOLD_ACCENT, 0.22f) : BoardPalette.PANEL_TOP;
                bottom = BoardPalette.PANEL_BOTTOM;
            }

            shape.setColor(0f, 0f, 0f, 0.42f);
            shape.rect(btn.x + 4f, btn.y - 5f, btn.width, btn.height);

            shape.setColor(border);
            shape.rect(btn.x - 3f, btn.y - 3f, btn.width + 6f, btn.height + 6f);
            shape.rect(btn.x, btn.y, btn.width, btn.height, bottom, bottom, top, top);

            if (i == 0 || hovered) {
                float sweep = ((animTime * 260f) % (btn.width + 120f)) - 60f;
                float barX = MathUtils.clamp(btn.x + sweep, btn.x, btn.x + btn.width - 28f);
                shape.setColor(1f, 1f, 1f, 0.14f);
                shape.rect(barX, btn.y + 4f, 24f, btn.height - 8f);
            }
        }
    }

    private void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont, GlyphLayout glyphLayout,
                           int highScore, GameMode selectedMode, float hoverX, float hoverY,
                           LocalizationManager localization) {
        bigFont.getData().setScale(3.35f * VectorFontFactory.BIG_BASE_SCALE);
        String title = "PUSAKA";
        glyphLayout.setText(bigFont, title);
        float titleX = PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f;
        float titleY = PANEL_Y + PANEL_H - 42f;
        bigFont.setColor(Color.BLACK);
        bigFont.draw(batch, title, titleX + 3.5f, titleY - 3.5f);
        bigFont.setColor(BoardPalette.GOLD_ACCENT);
        bigFont.draw(batch, title, titleX, titleY);

        hudFont.getData().setScale(1.48f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(BoardPalette.MINT_ACCENT);
        String sub = "Ilmu Kuno Menjadi Keseruan";
        glyphLayout.setText(hudFont, sub);
        hudFont.draw(batch, sub, PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f, titleY - 62f);

        hudFont.getData().setScale(1.36f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(Color.WHITE);
        String stats = "REKOR: " + highScore + "   |   PUSAKA: " + PusakaCompendium.getInstance().getUnlockedCount() + "/10";
        glyphLayout.setText(hudFont, stats);
        hudFont.draw(batch, stats, PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f, titleY - 108f);

        String[] items = {
                "MULAI BERMAIN [ENTER]",
                "MODE: " + localization.getModeName(selectedMode) + " [G]",
                "KOLEKSI PUSAKA [K]",
                "PILIH BAHASA [L]",
                "KELUAR PERMAINAN [ESC]"
        };
        Rectangle[] buttons = {BTN_PLAY, BTN_MODE, BTN_COMPENDIUM, BTN_LANGUAGE, BTN_EXIT};

        hudFont.getData().setScale(1.62f * VectorFontFactory.HUD_BASE_SCALE);
        for (int i = 0; i < items.length; i++) {
            Rectangle btn = buttons[i];
            boolean hovered = btn.contains(hoverX, hoverY);

            if (i == 4) {
                hudFont.setColor(hovered ? Color.WHITE : Color.SALMON);
            } else if (i == 0 || hovered) {
                hudFont.setColor(BoardPalette.MINT_ACCENT);
            } else {
                hudFont.setColor(Color.WHITE);
            }

            glyphLayout.setText(hudFont, items[i]);
            float tx = btn.x + (btn.width - glyphLayout.width) * 0.5f;
            float ty = btn.y + 39f;
            hudFont.draw(batch, items[i], tx, ty);
        }

        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        hudFont.dispose();
        bigFont.dispose();
    }
}