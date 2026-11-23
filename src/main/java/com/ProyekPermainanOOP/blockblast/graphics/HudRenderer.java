package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.ProyekPermainanOOP.blockblast.model.PusakaCompendium;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HudRenderer {

    private final LocalizationManager localization = LocalizationManager.getInstance();

    public void drawPanels(ShapeRenderer shape, float worldW) {
        float leftX = BoardMetrics.getLeftPanelX(worldW);
        float rightX = BoardMetrics.getRightPanelX(worldW);
        float topY = BoardMetrics.getTopCardY();

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(leftX - 3f, topY - 3f, BoardMetrics.SIDE_PANEL_W + 6f, BoardMetrics.SCORE_CARD_H + 6f);
        shape.rect(leftX, topY, BoardMetrics.SIDE_PANEL_W, BoardMetrics.SCORE_CARD_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        float livesY = BoardMetrics.getSecondLeftCardY();
        shape.setColor(BoardPalette.HEART_RED);
        shape.rect(leftX - 3f, livesY - 3f, BoardMetrics.SIDE_PANEL_W + 6f, BoardMetrics.LIVES_CARD_H + 6f);
        shape.rect(leftX, livesY, BoardMetrics.SIDE_PANEL_W, BoardMetrics.LIVES_CARD_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        shape.setColor(BoardPalette.MINT_ACCENT);
        shape.rect(rightX - 3f, topY - 3f, BoardMetrics.SIDE_PANEL_W + 6f, BoardMetrics.TIME_CARD_H + 6f);
        shape.rect(rightX, topY, BoardMetrics.SIDE_PANEL_W, BoardMetrics.TIME_CARD_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        float infoY = BoardMetrics.getInfoRightCardY();
        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(rightX - 3f, infoY - 3f, BoardMetrics.SIDE_PANEL_W + 6f, BoardMetrics.INFO_CARD_H + 6f);
        shape.rect(rightX, infoY, BoardMetrics.SIDE_PANEL_W, BoardMetrics.INFO_CARD_H,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        float trayPanelY = BoardMetrics.TRAY_CENTER_Y - 82f;
        float trayPanelH = 164f;
        shape.rect(BoardMetrics.BOARD_X - 10f, trayPanelY, BoardMetrics.BOARD_PX + 20f, trayPanelH,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);
    }

    public void drawGameOverPanel(ShapeRenderer shape, boolean gameOver, boolean modalOpen) {
        if (!gameOver || modalOpen) {
            return;
        }
        shape.setColor(0.02f, 0.08f, 0.06f, 0.78f);
        shape.rect(BoardMetrics.BOARD_X - 12f, BoardMetrics.BOARD_Y - 12f,
                BoardMetrics.BOARD_PX + 24f, BoardMetrics.BOARD_PX + 24f);

        float cardX = BoardMetrics.GAME_OVER_CARD_X;
        float cardY = BoardMetrics.GAME_OVER_CARD_Y;
        float cardW = BoardMetrics.GAME_OVER_CARD_W;
        float cardH = BoardMetrics.GAME_OVER_CARD_H;

        shape.setColor(0f, 0f, 0f, 0.55f);
        shape.rect(cardX + 8f, cardY - 10f, cardW, cardH);

        shape.setColor(BoardPalette.GOLD_ACCENT);
        shape.rect(cardX - 5f, cardY - 5f, cardW + 10f, cardH + 10f);

        shape.rect(cardX, cardY, cardW, cardH,
                BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_BOTTOM, BoardPalette.PANEL_TOP, BoardPalette.PANEL_TOP);

        shape.setColor(BoardPalette.HEART_RED.r, BoardPalette.HEART_RED.g, BoardPalette.HEART_RED.b, 0.35f);
        shape.rect(cardX + 14f, cardY + cardH - 76f, cardW - 28f, 62f);

        shape.setColor(BoardPalette.GOLD_ACCENT.r, BoardPalette.GOLD_ACCENT.g, BoardPalette.GOLD_ACCENT.b, 0.45f);
        shape.rect(cardX + 28f, cardY + 74f, cardW - 56f, 2f);
    }

    public void drawTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont, GlyphLayout glyphLayout,
                          float worldW, int score, int highScore, int combo, int lives, float elapsedSeconds,
                          boolean muted, boolean gameOver, GameMode gameMode, int questTileId,
                          int questCurrent, int questTarget, int undoRemaining, boolean compendiumOpen) {
        float leftX = BoardMetrics.getLeftPanelX(worldW);
        float rightX = BoardMetrics.getRightPanelX(worldW);
        float topY = BoardMetrics.getTopCardY();

        hudFont.getData().setScale(1.65f);
        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        hudFont.draw(batch, localization.getScoreLabel(), leftX + 20f, topY + BoardMetrics.SCORE_CARD_H - 18f);
        bigFont.getData().setScale(2.75f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, String.valueOf(score), leftX + 20f, topY + 56f);

        float livesY = BoardMetrics.getSecondLeftCardY();
        hudFont.setColor(BoardPalette.HEART_RED);
        hudFont.draw(batch, localization.getLivesLabel(), leftX + 20f, livesY + BoardMetrics.LIVES_CARD_H - 18f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, lives + " / 3", leftX + 20f, livesY + 56f);

        int totalSec = Math.max(0, (int) elapsedSeconds);
        String timeStr = String.format("%02d:%02d", totalSec / 60, totalSec % 60);
        hudFont.setColor(BoardPalette.MINT_ACCENT);
        hudFont.draw(batch, localization.getTimeLabel(), rightX + 20f, topY + BoardMetrics.TIME_CARD_H - 18f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, timeStr, rightX + 20f, topY + 56f);

        float infoY = BoardMetrics.getInfoRightCardY();
        hudFont.getData().setScale(1.42f);
        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        hudFont.draw(batch, localization.getBestHeader(highScore, combo), rightX + 20f, infoY + BoardMetrics.INFO_CARD_H - 22f);

        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, "[G] " + localization.getModeName(gameMode), rightX + 20f, infoY + BoardMetrics.INFO_CARD_H - 72f);

        hudFont.setColor(BoardPalette.MINT_ACCENT);
        hudFont.draw(batch, "[Z] " + localization.getUndoHeader(undoRemaining), rightX + 20f, infoY + BoardMetrics.INFO_CARD_H - 122f);

        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        hudFont.draw(batch, "[L] " + localization.getLangHeader(), rightX + 20f, infoY + BoardMetrics.INFO_CARD_H - 172f);

        float screenLeftX = (BoardMetrics.VIEW_W - worldW) * 0.5f + 26f;
        float screenRightX = (BoardMetrics.VIEW_W + worldW) * 0.5f - 26f;

        hudFont.getData().setScale(1.85f);
        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        String bottomLeftText = localization.formatBottomLeftControls(muted, gameMode, questCurrent, questTarget);
        hudFont.draw(batch, bottomLeftText, screenLeftX, 54f);
        if (gameMode == GameMode.QUEST) {
            glyphLayout.setText(hudFont, bottomLeftText);
            TextureRegion questReg = BoardPalette.getTileRegion(questTileId);
            if (questReg != null) {
                batch.draw(questReg, screenLeftX + glyphLayout.width + 12f, 24f, 32f, 32f);
            }
        }

        hudFont.getData().setScale(1.90f);
        hudFont.setColor(BoardPalette.MINT_ACCENT);
        String collectionText = localization.formatBottomRightCollection(PusakaCompendium.getInstance().getUnlockedCount(), 10);
        glyphLayout.setText(hudFont, collectionText);
        hudFont.draw(batch, collectionText, screenRightX - glyphLayout.width, 55f);

        if (gameOver && !localization.isLanguageMenuOpen() && !compendiumOpen) {
            drawGameOverTexts(batch, hudFont, bigFont, glyphLayout, score);
        }

        hudFont.getData().setScale(1.45f);
        bigFont.getData().setScale(2.45f);
    }

    private void drawGameOverTexts(SpriteBatch batch, BitmapFont hudFont, BitmapFont bigFont,
                                   GlyphLayout glyphLayout, int score) {
        float cardX = BoardMetrics.GAME_OVER_CARD_X;
        float cardY = BoardMetrics.GAME_OVER_CARD_Y;
        float cardW = BoardMetrics.GAME_OVER_CARD_W;
        float cardH = BoardMetrics.GAME_OVER_CARD_H;

        String title = localization.getGameOverTitle();
        bigFont.getData().setScale(2.35f);
        glyphLayout.setText(bigFont, title);
        float titleX = cardX + (cardW - glyphLayout.width) * 0.5f;
        float titleY = cardY + cardH - 30f;

        bigFont.setColor(Color.BLACK);
        bigFont.draw(batch, title, titleX + 3f, titleY - 3f);
        bigFont.setColor(Color.WHITE);
        bigFont.draw(batch, title, titleX, titleY);

        String finalScoreLine = localization.getScoreLabel() + ": " + score;
        bigFont.getData().setScale(2.05f);
        glyphLayout.setText(bigFont, finalScoreLine);
        float scoreX = cardX + (cardW - glyphLayout.width) * 0.5f;
        float scoreY = cardY + 144f;

        bigFont.setColor(Color.BLACK);
        bigFont.draw(batch, finalScoreLine, scoreX + 2f, scoreY - 2f);
        bigFont.setColor(BoardPalette.MINT_ACCENT);
        bigFont.draw(batch, finalScoreLine, scoreX, scoreY);

        String subtitle = localization.getGameOverSubtitle();
        hudFont.getData().setScale(1.52f);
        glyphLayout.setText(hudFont, subtitle);
        float subX = cardX + (cardW - glyphLayout.width) * 0.5f;
        float subY = cardY + 46f;

        hudFont.setColor(Color.BLACK);
        hudFont.draw(batch, subtitle, subX + 2f, subY - 2f);
        hudFont.setColor(BoardPalette.GOLD_ACCENT);
        hudFont.draw(batch, subtitle, subX, subY);
    }
}