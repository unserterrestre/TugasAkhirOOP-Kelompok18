package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.PusakaCompendium;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CompendiumModalRenderer {

    private final LocalizationManager localization = LocalizationManager.getInstance();

    public void drawShapes(ShapeRenderer shape, boolean compendiumOpen) {
        if (!compendiumOpen) {
            return;
        }
        shape.setColor(0f, 0f, 0f, 0.78f);
        shape.rect(0f, 0f, BoardMetrics.VIEW_W, BoardMetrics.VIEW_H);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(BoardMetrics.COMPENDIUM_X - 5f, BoardMetrics.COMPENDIUM_Y - 5f,
                BoardMetrics.COMPENDIUM_W + 10f, BoardMetrics.COMPENDIUM_H + 10f);
        shape.rect(BoardMetrics.COMPENDIUM_X, BoardMetrics.COMPENDIUM_Y,
                BoardMetrics.COMPENDIUM_W, BoardMetrics.COMPENDIUM_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        float tileBoxSize = 172f;
        float tileBoxX = BoardMetrics.COMPENDIUM_X + (BoardMetrics.COMPENDIUM_W - tileBoxSize) * 0.5f;
        float tileBoxY = BoardMetrics.COMPENDIUM_Y + BoardMetrics.COMPENDIUM_H - 270f;

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(tileBoxX - 4f, tileBoxY - 4f, tileBoxSize + 8f, tileBoxSize + 8f);
        shape.rect(tileBoxX, tileBoxY, tileBoxSize, tileBoxSize,
                BoardPalette.TILE_BOTTOM, BoardPalette.TILE_BOTTOM, BoardPalette.TILE_TOP, BoardPalette.TILE_TOP);

        float descBoxX = BoardMetrics.COMPENDIUM_X + 36f;
        float descBoxY = BoardMetrics.COMPENDIUM_Y + 146f;
        float descBoxW = BoardMetrics.COMPENDIUM_W - 72f;
        float descBoxH = 236f;
        shape.setColor(BoardPalette.PANEL_BOTTOM);
        shape.rect(descBoxX, descBoxY, descBoxW, descBoxH);

        shape.setColor(BoardPalette.EMPTY_TOP);
        shape.rect(BoardMetrics.COMP_PREV_BTN_X, BoardMetrics.COMP_NAV_BTN_Y,
                BoardMetrics.COMP_NAV_BTN_W, BoardMetrics.COMP_NAV_BTN_H);
        shape.rect(BoardMetrics.COMP_NEXT_BTN_X, BoardMetrics.COMP_NAV_BTN_Y,
                BoardMetrics.COMP_NAV_BTN_W, BoardMetrics.COMP_NAV_BTN_H);
    }

    public void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont,
                          GlyphLayout glyphLayout, boolean compendiumOpen) {
        if (!compendiumOpen) {
            return;
        }
        PusakaCompendium compendium = PusakaCompendium.getInstance();
        PusakaCompendium.RelicEntry entry = compendium.getCurrentEntry();
        LocalizationManager.Language lang = localization.getCurrentLanguage();
        PusakaCompendium.LocalizedLore lore = entry.getLore(lang);

        int pageNum = compendium.getCurrentPageIndex() + 1;
        int totalPages = compendium.getEntries().size();

        bigFont.getData().setScale(2.25f * VectorFontFactory.BIG_BASE_SCALE);
        bigFont.setColor(BoardPalette.GOLD_ACCENT);
        String header = localization.getCompendiumTitle() + " (" + pageNum + "/" + totalPages + ")";
        glyphLayout.setText(bigFont, header);
        bigFont.draw(batch, header,
                BoardMetrics.COMPENDIUM_X + (BoardMetrics.COMPENDIUM_W - glyphLayout.width) * 0.5f,
                BoardMetrics.COMPENDIUM_Y + BoardMetrics.COMPENDIUM_H - 26f);

        float tileBoxSize = 172f;
        float tileBoxX = BoardMetrics.COMPENDIUM_X + (BoardMetrics.COMPENDIUM_W - tileBoxSize) * 0.5f;
        float tileBoxY = BoardMetrics.COMPENDIUM_Y + BoardMetrics.COMPENDIUM_H - 270f;

        TextureRegion reg = BoardPalette.getTileRegion(entry.getBaseTileId());
        if (reg != null) {
            batch.draw(reg, tileBoxX + 14f, tileBoxY + 14f, tileBoxSize - 28f, tileBoxSize - 28f);
        }

        bigFont.getData().setScale(1.95f * VectorFontFactory.BIG_BASE_SCALE);
        String pinyinTitle = entry.getPinyin() + (entry.isUnlocked() ? " - " + lore.getTitle() : "");
        bigFont.setColor(entry.isUnlocked() ? BoardPalette.MINT_ACCENT : Color.LIGHT_GRAY);
        glyphLayout.setText(bigFont, pinyinTitle);
        bigFont.draw(batch, pinyinTitle,
                BoardMetrics.COMPENDIUM_X + (BoardMetrics.COMPENDIUM_W - glyphLayout.width) * 0.5f,
                tileBoxY - 22f);

        float descBoxX = BoardMetrics.COMPENDIUM_X + 36f;
        float descBoxY = BoardMetrics.COMPENDIUM_Y + 146f;
        float descBoxW = BoardMetrics.COMPENDIUM_W - 72f;
        float descBoxH = 236f;

        if (entry.isUnlocked()) {
            hudFont.getData().setScale(1.52f * VectorFontFactory.HUD_BASE_SCALE);
            hudFont.setColor(Color.WHITE);
            String[] lines = lore.getLines();
            float lineY = descBoxY + descBoxH - 36f;
            for (String line : lines) {
                glyphLayout.setText(hudFont, line);
                hudFont.draw(batch, line, descBoxX + (descBoxW - glyphLayout.width) * 0.5f, lineY);
                lineY -= 44f;
            }
        } else {
            bigFont.getData().setScale(1.75f * VectorFontFactory.BIG_BASE_SCALE);
            bigFont.setColor(Color.LIGHT_GRAY);
            String lockMsg = localization.getLockedHeader(entry.getClearedCount());
            glyphLayout.setText(bigFont, lockMsg);
            bigFont.draw(batch, lockMsg, descBoxX + (descBoxW - glyphLayout.width) * 0.5f, descBoxY + descBoxH * 0.5f + 26f);

            hudFont.getData().setScale(1.34f * VectorFontFactory.HUD_BASE_SCALE);
            hudFont.setColor(BoardPalette.GOLD_ACCENT);
            String lockSub = localization.getLockedSubLabel();
            glyphLayout.setText(hudFont, lockSub);
            hudFont.draw(batch, lockSub, descBoxX + (descBoxW - glyphLayout.width) * 0.5f, descBoxY + descBoxH * 0.5f - 22f);
        }

        hudFont.getData().setScale(1.42f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        String prevText = localization.getPrevButtonLabel();
        glyphLayout.setText(hudFont, prevText);
        hudFont.draw(batch, prevText,
                BoardMetrics.COMP_PREV_BTN_X + (BoardMetrics.COMP_NAV_BTN_W - glyphLayout.width) * 0.5f,
                BoardMetrics.COMP_NAV_BTN_Y + 37f);

        String nextText = localization.getNextButtonLabel();
        glyphLayout.setText(hudFont, nextText);
        hudFont.draw(batch, nextText,
                BoardMetrics.COMP_NEXT_BTN_X + (BoardMetrics.COMP_NAV_BTN_W - glyphLayout.width) * 0.5f,
                BoardMetrics.COMP_NAV_BTN_Y + 37f);

        hudFont.getData().setScale(1.18f * VectorFontFactory.HUD_BASE_SCALE);
        hudFont.setColor(Color.LIGHT_GRAY);
        String closeHint = localization.getCompendiumHint();
        glyphLayout.setText(hudFont, closeHint);
        hudFont.draw(batch, closeHint,
                BoardMetrics.COMPENDIUM_X + (BoardMetrics.COMPENDIUM_W - glyphLayout.width) * 0.5f,
                BoardMetrics.COMPENDIUM_Y + 36f);
        hudFont.getData().setScale(1.45f * VectorFontFactory.HUD_BASE_SCALE);
        bigFont.getData().setScale(2.45f * VectorFontFactory.BIG_BASE_SCALE);
    }
}