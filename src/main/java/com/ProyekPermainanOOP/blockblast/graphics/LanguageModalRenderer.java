package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LanguageModalRenderer {

    private final LocalizationManager localization = LocalizationManager.getInstance();

    public void drawShapes(ShapeRenderer shape) {
        if (!localization.isLanguageMenuOpen()) {
            return;
        }
        shape.setColor(0f, 0f, 0f, 0.75f);
        shape.rect(0f, 0f, BoardMetrics.VIEW_W, BoardMetrics.VIEW_H);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(BoardMetrics.LANG_MENU_X - 4f, BoardMetrics.LANG_MENU_Y - 4f,
                BoardMetrics.LANG_MENU_W + 8f, BoardMetrics.LANG_MENU_H + 8f);
        shape.rect(BoardMetrics.LANG_MENU_X, BoardMetrics.LANG_MENU_Y,
                BoardMetrics.LANG_MENU_W, BoardMetrics.LANG_MENU_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        LocalizationManager.Language[] languages = LocalizationManager.Language.values();
        LocalizationManager.Language active = localization.getCurrentLanguage();

        for (int i = 0; i < languages.length; i++) {
            float btnX = BoardMetrics.getLanguageButtonX(i);
            float btnY = BoardMetrics.getLanguageButtonY(i);
            boolean isSelected = (languages[i] == active);
            shape.setColor(isSelected ? BoardPalette.GOLD_ACCENT : BoardPalette.EMPTY_TOP);
            shape.rect(btnX, btnY, BoardMetrics.LANG_BTN_W, BoardMetrics.LANG_BTN_H);
        }
    }

    public void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont, GlyphLayout glyphLayout) {
        if (!localization.isLanguageMenuOpen()) {
            return;
        }
        LocalizationManager.Language[] languages = LocalizationManager.Language.values();
        LocalizationManager.Language active = localization.getCurrentLanguage();

        bigFont.getData().setScale(2.15f);
        bigFont.setColor(BoardPalette.GOLD_ACCENT);
        String menuHeader = localization.getLanguageMenuTitle() + " (20)";
        glyphLayout.setText(bigFont, menuHeader);
        bigFont.draw(batch, menuHeader,
                BoardMetrics.LANG_MENU_X + (BoardMetrics.LANG_MENU_W - glyphLayout.width) * 0.5f,
                BoardMetrics.LANG_MENU_Y + BoardMetrics.LANG_MENU_H - 22f);

        hudFont.getData().setScale(1.12f);
        for (int i = 0; i < languages.length; i++) {
            float btnX = BoardMetrics.getLanguageButtonX(i);
            float btnY = BoardMetrics.getLanguageButtonY(i);
            boolean isSelected = (languages[i] == active);
            hudFont.setColor(isSelected ? BoardPalette.PANEL_BOTTOM : Color.WHITE);
            String label = (i + 1) + ". " + languages[i].getDisplayName() + (isSelected ? " *" : "");
            hudFont.draw(batch, label, btnX + 12f, btnY + 25f);
        }

        hudFont.getData().setScale(1.15f);
        hudFont.setColor(Color.LIGHT_GRAY);
        glyphLayout.setText(hudFont, localization.getLanguageMenuHint());
        hudFont.draw(batch, localization.getLanguageMenuHint(),
                BoardMetrics.LANG_MENU_X + (BoardMetrics.LANG_MENU_W - glyphLayout.width) * 0.5f,
                BoardMetrics.LANG_MENU_Y + 30f);
        hudFont.getData().setScale(1.45f);
        bigFont.getData().setScale(2.45f);
    }
}