package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseModalRenderer {

    private final LocalizationManager localization = LocalizationManager.getInstance();

    public void drawShapes(ShapeRenderer shape, boolean paused) {
        if (!paused) {
            return;
        }
        shape.setColor(0f, 0f, 0f, 0.78f);
        shape.rect(0f, 0f, BoardMetrics.VIEW_W, BoardMetrics.VIEW_H);

        float menuX = BoardMetrics.PAUSE_MENU_X;
        float menuY = BoardMetrics.PAUSE_MENU_Y;
        float menuW = BoardMetrics.PAUSE_MENU_W;
        float menuH = BoardMetrics.PAUSE_MENU_H;

        shape.setColor(0f, 0f, 0f, 0.55f);
        shape.rect(menuX + 8f, menuY - 10f, menuW, menuH);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(menuX - 5f, menuY - 5f, menuW + 10f, menuH + 10f);
        shape.rect(menuX, menuY, menuW, menuH,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        for (int i = 0; i < BoardMetrics.PAUSE_BTN_COUNT; i++) {
            float btnX = BoardMetrics.PAUSE_BTN_X;
            float btnY = BoardMetrics.getPauseButtonY(i);
            Color borderColor = (i == 0) ? BoardPalette.MINT_ACCENT : BoardPalette.GOLD_ACCENT;
            Color fillTop = (i == 0) ? BoardPalette.EMPTY_TOP : BoardPalette.PANEL_TOP;

            shape.setColor(borderColor);
            shape.rect(btnX - 2f, btnY - 2f, BoardMetrics.PAUSE_BTN_W + 4f, BoardMetrics.PAUSE_BTN_H + 4f);
            shape.rect(btnX, btnY, BoardMetrics.PAUSE_BTN_W, BoardMetrics.PAUSE_BTN_H,
                    BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, fillTop, fillTop);
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
        String header = "PERMAINAN DIJEDA";
        glyphLayout.setText(bigFont, header);
        bigFont.draw(batch, header, menuX + (menuW - glyphLayout.width) * 0.5f, menuY + menuH - 34f);

        String[] labels = {
                "LANJUTKAN [P / ESC]",
                "ULANG PERMAINAN [R]",
                (muted ? "SUARA: MATI [M]" : "SUARA: NYALA [M]"),
                "MODE: " + localization.getModeName(currentMode) + " [G]"
        };

        hudFont.getData().setScale(1.62f * VectorFontFactory.HUD_BASE_SCALE);
        for (int i = 0; i < labels.length; i++) {
            float btnY = BoardMetrics.getPauseButtonY(i);
            hudFont.setColor(i == 0 ? BoardPalette.MINT_ACCENT : Color.WHITE);
            glyphLayout.setText(hudFont, labels[i]);
            hudFont.draw(batch, labels[i],
                    BoardMetrics.PAUSE_BTN_X + (BoardMetrics.PAUSE_BTN_W - glyphLayout.width) * 0.5f,
                    btnY + 41f);
        }

        hudFont.getData().setScale(1.22f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(Color.LIGHT_GRAY);
        String hint = "Klik tombol di atas atau tekan P / ESC untuk kembali";
        glyphLayout.setText(hudFont, hint);
        hudFont.draw(batch, hint, menuX + (menuW - glyphLayout.width) * 0.5f, menuY + 34f);

        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
    }
}