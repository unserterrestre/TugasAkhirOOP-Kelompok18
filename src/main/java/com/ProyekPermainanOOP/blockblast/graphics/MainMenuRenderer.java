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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class MainMenuRenderer implements Disposable {

    public static final float PANEL_W = 540f;
    public static final float PANEL_H = 660f;
    public static final float PANEL_X = (BoardMetrics.VIEW_W - PANEL_W) * 0.5f;
    public static final float PANEL_Y = (BoardMetrics.VIEW_H - PANEL_H) * 0.5f;

    public static final float BTN_W = 430f;
    public static final float BTN_H = 66f;
    public static final float BTN_X = (BoardMetrics.VIEW_W - BTN_W) * 0.5f;
    public static final float FIRST_BTN_Y = PANEL_Y + PANEL_H - 280f;
    public static final float BTN_GAP = 18f;

    public static final Rectangle BTN_PLAY = new Rectangle(BTN_X, FIRST_BTN_Y, BTN_W, BTN_H);
    public static final Rectangle BTN_MODE = new Rectangle(BTN_X, FIRST_BTN_Y - (BTN_H + BTN_GAP), BTN_W, BTN_H);
    public static final Rectangle BTN_COMPENDIUM = new Rectangle(BTN_X, FIRST_BTN_Y - 2f * (BTN_H + BTN_GAP), BTN_W, BTN_H);
    public static final Rectangle BTN_LANGUAGE = new Rectangle(BTN_X, FIRST_BTN_Y - 3f * (BTN_H + BTN_GAP), BTN_W, BTN_H);

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont hudFont = VectorFontFactory.createHudFont();
    private final BitmapFont bigFont = VectorFontFactory.createBigFont();
    private final GlyphLayout glyphLayout = new GlyphLayout();

    public MainMenuRenderer() {
        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
    }

    public void render(Matrix4 projection, GameMode selectedMode, int highScore,
                       float hoverX, float hoverY, LocalizationManager localization) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        drawShapes(shape, hoverX, hoverY);
        shape.end();

        batch.setProjectionMatrix(projection);
        batch.begin();
        drawTexts(batch, hudFont, bigFont, glyphLayout, highScore, selectedMode, hoverX, hoverY, localization);
        batch.end();
    }

    private void drawShapes(ShapeRenderer shape, float hoverX, float hoverY) {
        shape.setColor(0.02f, 0.08f, 0.06f, 0.62f);
        shape.rect(-1000f, 0f, BoardMetrics.VIEW_W + 2000f, BoardMetrics.VIEW_H);

        shape.setColor(0f, 0f, 0f, 0.55f);
        shape.rect(PANEL_X + 10f, PANEL_Y - 12f, PANEL_W, PANEL_H);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(PANEL_X - 5f, PANEL_Y - 5f, PANEL_W + 10f, PANEL_H + 10f);
        shape.rect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        Rectangle[] buttons = {BTN_PLAY, BTN_MODE, BTN_COMPENDIUM, BTN_LANGUAGE};

        for (int i = 0; i < buttons.length; i++) {
            Rectangle btn = buttons[i];
            boolean hovered = btn.contains(hoverX, hoverY);
            Color border = (i == 0 || hovered) ? BoardPalette.MINT_ACCENT : BoardPalette.GOLD_ACCENT;
            Color top = hovered
                    ? BoardPalette.EMPTY_TOP.cpy().lerp(BoardPalette.MINT_ACCENT, 0.25f)
                    : (i == 0 ? BoardPalette.EMPTY_TOP : BoardPalette.PANEL_TOP);

            shape.setColor(border);
            shape.rect(btn.x - 3f, btn.y - 3f, btn.width + 6f, btn.height + 6f);
            shape.rect(btn.x, btn.y, btn.width, btn.height,
                    BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, top, top);
        }
    }

    private void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont, GlyphLayout glyphLayout,
                           int highScore, GameMode selectedMode, float hoverX, float hoverY,
                           LocalizationManager localization) {
        bigFont.getData().setScale(3.15f * VectorFontFactory.BIG_BASE_SCALE);
        String title = "PUSAKA";
        glyphLayout.setText(bigFont, title);
        float titleX = PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f;
        float titleY = PANEL_Y + PANEL_H - 42f;
        bigFont.setColor(Color.BLACK);
        bigFont.draw(batch, title, titleX + 3f, titleY - 3f);
        bigFont.setColor(BoardPalette.GOLD_ACCENT);
        bigFont.draw(batch, title, titleX, titleY);

        hudFont.getData().setScale(1.48f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(BoardPalette.MINT_ACCENT);
        String sub = "Ilmu Kuno Menjadi Keseruan";
        glyphLayout.setText(hudFont, sub);
        hudFont.draw(batch, sub, PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f, titleY - 58f);

        hudFont.getData().setScale(1.38f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(Color.WHITE);
        String stats = "REKOR: " + highScore + "   |   PUSAKA: " + PusakaCompendium.getInstance().getUnlockedCount() + "/10";
        glyphLayout.setText(hudFont, stats);
        hudFont.draw(batch, stats, PANEL_X + (PANEL_W - glyphLayout.width) * 0.5f, titleY - 104f);

        String[] items = {
                "MULAI BERMAIN [ENTER]",
                "MODE: " + localization.getModeName(selectedMode) + " [G]",
                "KOLEKSI PUSAKA [K]",
                "PILIH BAHASA [L]"
        };
        Rectangle[] buttons = {BTN_PLAY, BTN_MODE, BTN_COMPENDIUM, BTN_LANGUAGE};

        hudFont.getData().setScale(1.68f * VectorFontFactory.HUD_BASE_SCALE);
        for (int i = 0; i < items.length; i++) {
            Rectangle btn = buttons[i];
            boolean hovered = btn.contains(hoverX, hoverY);
            hudFont.setColor((i == 0 || hovered) ? BoardPalette.MINT_ACCENT : Color.WHITE);
            glyphLayout.setText(hudFont, items[i]);
            hudFont.draw(batch, items[i], btn.x + (btn.width - glyphLayout.width) * 0.5f, btn.y + 44f);
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