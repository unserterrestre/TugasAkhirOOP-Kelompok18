package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class BoardRenderer implements Disposable {

    public static final float VIEW_W = BoardMetrics.VIEW_W;
    public static final float VIEW_H = BoardMetrics.VIEW_H;
    public static final int GRID_SIZE = BoardMetrics.GRID_SIZE;
    public static final float CELL = BoardMetrics.CELL;
    public static final float BOARD_PX = BoardMetrics.BOARD_PX;
    public static final float BOARD_X = BoardMetrics.BOARD_X;
    public static final float BOARD_Y = BoardMetrics.BOARD_Y;
    public static final float TRAY_TILE_SIZE = BoardMetrics.TRAY_TILE_SIZE;
    public static final float TRAY_CENTER_Y = BoardMetrics.TRAY_CENTER_Y;
    public static final float SIDE_PANEL_W = BoardMetrics.SIDE_PANEL_W;
    public static final float INFO_CARD_H = BoardMetrics.INFO_CARD_H;
    public static final int LANG_ROWS = BoardMetrics.LANG_ROWS;
    public static final float LANG_BTN_W = BoardMetrics.LANG_BTN_W;
    public static final float LANG_BTN_H = BoardMetrics.LANG_BTN_H;
    public static final float COMPENDIUM_W = BoardMetrics.COMPENDIUM_W;
    public static final float COMPENDIUM_H = BoardMetrics.COMPENDIUM_H;
    public static final float COMPENDIUM_X = BoardMetrics.COMPENDIUM_X;
    public static final float COMPENDIUM_Y = BoardMetrics.COMPENDIUM_Y;
    public static final float COMP_NAV_BTN_W = BoardMetrics.COMP_NAV_BTN_W;
    public static final float COMP_NAV_BTN_H = BoardMetrics.COMP_NAV_BTN_H;
    public static final float COMP_NAV_BTN_Y = BoardMetrics.COMP_NAV_BTN_Y;
    public static final float COMP_PREV_BTN_X = BoardMetrics.COMP_PREV_BTN_X;
    public static final float COMP_NEXT_BTN_X = BoardMetrics.COMP_NEXT_BTN_X;

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont hudFont = new BitmapFont();
    private final BitmapFont bigFont = new BitmapFont();
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private final SideDecorationRenderer sideDecoration = new SideDecorationRenderer();
    private final BoardEffectManager effectManager = new BoardEffectManager();
    private final BoardGridRenderer gridRenderer = new BoardGridRenderer();
    private final PlacementGuidanceRenderer guidanceRenderer = new PlacementGuidanceRenderer();
    private final TrayBlockRenderer trayBlockRenderer = new TrayBlockRenderer();
    private final HudRenderer hudRenderer = new HudRenderer();
    private final LanguageModalRenderer languageModalRenderer = new LanguageModalRenderer();
    private final CompendiumModalRenderer compendiumModalRenderer = new CompendiumModalRenderer();
    private final LocalizationManager localization = LocalizationManager.getInstance();

    private float elapsedAnimTime = 0f;
    private int activeCombo = 1;
    private int latestScore = 0;

    public BoardRenderer() {
        hudFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hudFont.setUseIntegerPositions(false);
        bigFont.setUseIntegerPositions(false);
        hudFont.getData().setScale(1.45f);
        bigFont.getData().setScale(2.45f);
    }

    public static float getLeftPanelX(float worldW) {
        return BoardMetrics.getLeftPanelX(worldW);
    }

    public static float getRightPanelX(float worldW) {
        return BoardMetrics.getRightPanelX(worldW);
    }

    public static float getInfoRightCardY() {
        return BoardMetrics.getInfoRightCardY();
    }

    public static float getLanguageButtonX(int index) {
        return BoardMetrics.getLanguageButtonX(index);
    }

    public static float getLanguageButtonY(int index) {
        return BoardMetrics.getLanguageButtonY(index);
    }

    public void notifySlotSpawned(int slotIndex) {
        effectManager.notifySlotSpawned(slotIndex);
    }

    public void triggerLineClearAnimation(int[][] clearedCells, int earnedPoints) {
        effectManager.triggerLineClearAnimation(clearedCells, earnedPoints, BoardPalette.GOLD_ACCENT);
    }

    public void spawnFloatingText(String text, float x, float y, Color color) {
        effectManager.spawnFloatingText(text, x, y, color);
    }

    public void updateAnimations(float delta) {
        elapsedAnimTime += delta;
        effectManager.update(delta);
    }

    public void begin(Matrix4 projection) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void end() {
        shape.end();
    }

    public void drawBackground(float worldW, float worldH) {
        sideDecoration.drawShapes(shape, worldW, worldH, elapsedAnimTime, activeCombo, effectManager.getClearSurge(), latestScore);
    }

    public void drawBackgroundFlyingTiles(Matrix4 projection, float worldW, float worldH, int combo, int score) {
        this.activeCombo = Math.max(1, combo);
        this.latestScore = score;
        batch.setProjectionMatrix(projection);
        batch.begin();
        sideDecoration.drawFlyingTiles(batch, worldW, worldH, elapsedAnimTime, activeCombo, effectManager.getClearSurge());
        batch.end();
    }

    public void drawHudPanels(float worldW) {
        hudRenderer.drawPanels(shape, worldW);
    }

    public void drawBoard(int[][] grid) {
        gridRenderer.drawShapes(shape, grid, elapsedAnimTime);
    }

    public void drawPlacementGuidance(int[][] grid, DragDropManager dragDropManager, boolean modalOpen) {
        guidanceRenderer.draw(shape, grid, dragDropManager, modalOpen, elapsedAnimTime);
    }

    public void drawTray(BlockTray tray, int selectedSlotIndex) {
        trayBlockRenderer.drawTrayShapes(shape, tray, selectedSlotIndex, effectManager, elapsedAnimTime);
    }

    public void drawDraggedBlock(DragDropManager dragDropManager) {
        trayBlockRenderer.drawDraggedShape(shape, dragDropManager, elapsedAnimTime);
    }

    public void drawClearFlashes() {
        effectManager.drawClearFlashes(shape);
    }

    public void drawModalShapes(boolean compendiumOpen) {
        if (localization.isLanguageMenuOpen()) {
            languageModalRenderer.drawShapes(shape);
        } else {
            compendiumModalRenderer.drawShapes(shape, compendiumOpen);
        }
    }

    public void drawOverlay(Matrix4 projection, float worldW, float worldH, int[][] grid, BlockTray tray,
                            DragDropManager dragDropManager, int score, int highScore, int combo, int lives,
                            float elapsedSeconds, boolean muted, boolean gameOver,
                            GameMode gameMode, int questTileId, int questCurrent, int questTarget,
                            int undoRemaining, boolean compendiumOpen) {
        this.activeCombo = Math.max(1, combo);
        this.latestScore = score;

        batch.setProjectionMatrix(projection);
        batch.begin();

        effectManager.drawBurstTiles(batch);

        if (!localization.isLanguageMenuOpen() && !compendiumOpen) {
            gridRenderer.drawTiles(batch, grid);
            trayBlockRenderer.drawTrayAndDraggedTiles(batch, tray, dragDropManager, effectManager, elapsedAnimTime);
        }

        hudRenderer.drawTexts(batch, hudFont, bigFont, glyphLayout, worldW, score, highScore, combo, lives,
                elapsedSeconds, muted, gameOver, gameMode, questTileId, questCurrent, questTarget,
                undoRemaining, compendiumOpen);
        effectManager.drawFloatingTexts(batch, bigFont);
        languageModalRenderer.drawTexts(batch, hudFont, bigFont, glyphLayout);
        compendiumModalRenderer.drawTexts(batch, hudFont, bigFont, glyphLayout, compendiumOpen);

        batch.end();
    }

    public Vector2 pixelToCell(float px, float py) {
        int col = (int) Math.floor((px - BOARD_X) / CELL);
        int row = (int) Math.floor((BOARD_Y + BOARD_PX - py) / CELL);
        if (col < 0 || col >= GRID_SIZE || row < 0 || row >= GRID_SIZE) {
            return null;
        }
        return new Vector2(col, row);
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        hudFont.dispose();
        bigFont.dispose();
    }
}