package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseModalRenderer {

    private static final Color EXIT_RED = new Color(0.92f, 0.28f, 0.30f, 1f);
    private static final Color EXIT_BG_TOP = new Color(0.28f, 0.08f, 0.10f, 0.96f);
    private static final Color EXIT_BG_BOTTOM = new Color(0.16f, 0.04f, 0.06f, 0.98f);

    private final LocalizationManager localization = LocalizationManager.getInstance();

    public void drawShapes(ShapeRenderer shape, boolean paused) {
        if (!paused) {
            return;
        }

        shape.setColor(0f, 0f, 0f, 0.68f);
        shape.rect(-1200f, 0f, BoardMetrics.VIEW_W + 2400f, BoardMetrics.VIEW_H);

        float menuX = BoardMetrics.PAUSE_MENU_X;
        float menuY = BoardMetrics.PAUSE_MENU_Y;
        float menuW = BoardMetrics.PAUSE_MENU_W;
        float menuH = BoardMetrics.PAUSE_MENU_H;

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(menuX - 5f, menuY - 5f, menuW + 10f, menuH + 10f);
        shape.rect(menuX, menuY, menuW, menuH,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        for (int i = 0; i < BoardMetrics.PAUSE_BTN_COUNT; i++) {
            float btnX = BoardMetrics.PAUSE_BTN_X;
            float btnY = BoardMetrics.getPauseButtonY(i);
            boolean isExit = (i == 4);

            Color border = isExit ? EXIT_RED : (i == 0 ? BoardPalette.MINT_ACCENT : BoardPalette.GOLD_ACCENT);
            Color top = isExit ? EXIT_BG_TOP : (i == 0 ? BoardPalette.EMPTY_TOP : BoardPalette.PANEL_TOP);
            Color bottom = isExit ? EXIT_BG_BOTTOM : BoardPalette.PANEL_BOTTOM;

            shape.setColor(border);
            shape.rect(btnX - 2.5f, btnY - 2.5f, BoardMetrics.PAUSE_BTN_W + 5f, BoardMetrics.PAUSE_BTN_H + 5f);
            shape.rect(btnX, btnY, BoardMetrics.PAUSE_BTN_W, BoardMetrics.PAUSE_BTN_H, bottom, bottom, top, top);
        }
    }

    public void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont, GlyphLayout glyphLayout,
                          boolean paused, boolean muted, GameMode currentMode) {
        if (!paused) {
            return;
        }

        float menuX = BoardMetrics.PAUSE_MENU_X;
        float menuY = BoardMetrics.PAUSE_MENU_Y;
        float menuW = BoardMetrics.PAUSE_MENU_W;
        float menuH = BoardMetrics.PAUSE_MENU_H;

        bigFont.getData().setScale(2.35f * VectorFontFactory.BIG_BASE_SCALE);
        bigFont.setColor(BoardPalette.GOLD_ACCENT);
        String title = "PERMAINAN DIJEDA";
        glyphLayout.setText(bigFont, title);
        bigFont.draw(batch, title, menuX + (menuW - glyphLayout.width) * 0.5f, menuY + menuH - 36f);

        String[] labels = {
                "LANJUTKAN [P / ESC]",
                "ULANG PERMAINAN [R]",
                "SUARA: " + (muted ? "MATI [M]" : "NYALA [M]"),
                "MODE: " + localization.getModeName(currentMode) + " [G]",
                "KELUAR PERMAINAN"
        };

        hudFont.getData().setScale(1.62f * VectorFontFactory.HUD_BASE_SCALE);
        for (int i = 0; i < labels.length; i++) {
            float btnY = BoardMetrics.getPauseButtonY(i);
            if (i == 4) {
                hudFont.setColor(Color.SALMON);
            } else if (i == 0) {
                hudFont.setColor(BoardPalette.MINT_ACCENT);
            } else {
                hudFont.setColor(Color.WHITE);
            }
            glyphLayout.setText(hudFont, labels[i]);
            float tx = BoardMetrics.PAUSE_BTN_X + (BoardMetrics.PAUSE_BTN_W - glyphLayout.width) * 0.5f;
            float ty = btnY + 37f;
            hudFont.draw(batch, labels[i], tx, ty);
        }

        hudFont.getData().setScale(1.22f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(Color.LIGHT_GRAY);
        String hint = "Klik tombol di atas atau tekan P / ESC untuk kembali";
        glyphLayout.setText(hudFont, hint);
        hudFont.draw(batch, hint, menuX + (menuW - glyphLayout.width) * 0.5f, menuY + 30f);

        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
        hudFont.setColor(Color.WHITE);
        bigFont.setColor(Color.WHITE);
    }
}