package com.ProyekPermainanOOP.blockblast.graphics;

public final class BoardMetrics {

    public static final float VIEW_W = 720f;
    public static final float VIEW_H = 1000f;

    public static final int GRID_SIZE = 8;
    public static final float CELL = 86f;
    public static final float BOARD_PX = GRID_SIZE * CELL;
    public static final float BOARD_X = (VIEW_W - BOARD_PX) / 2f;
    public static final float BOARD_Y = 278f;

    public static final float TRAY_TILE_SIZE = 38f;
    public static final float TRAY_CENTER_Y = 148f;

    public static final float SIDE_PANEL_W = 290f;
    public static final float SIDE_MARGIN = 28f;
    public static final float SCORE_CARD_H = 122f;
    public static final float LIVES_CARD_H = 122f;
    public static final float TIME_CARD_H = 122f;
    public static final float INFO_CARD_H = 224f;

    public static final int LANG_COLS = 2;
    public static final int LANG_ROWS = 10;
    public static final float LANG_MENU_W = 540f;
    public static final float LANG_MENU_H = 540f;
    public static final float LANG_MENU_X = (VIEW_W - LANG_MENU_W) / 2f;
    public static final float LANG_MENU_Y = (VIEW_H - LANG_MENU_H) / 2f;
    public static final float LANG_BTN_W = 246f;
    public static final float LANG_BTN_H = 36f;
    public static final float LANG_COL_GAP = 16f;
    public static final float LANG_ROW_GAP = 6f;
    public static final float LANG_GRID_START_X = LANG_MENU_X + (LANG_MENU_W - (LANG_COLS * LANG_BTN_W + LANG_COL_GAP)) / 2f;
    public static final float LANG_GRID_FIRST_Y = LANG_MENU_Y + LANG_MENU_H - 104f;

    public static final float COMPENDIUM_W = 680f;
    public static final float COMPENDIUM_H = 760f;
    public static final float COMPENDIUM_X = (VIEW_W - COMPENDIUM_W) / 2f;
    public static final float COMPENDIUM_Y = (VIEW_H - COMPENDIUM_H) / 2f;

    public static final float COMP_NAV_BTN_W = 215f;
    public static final float COMP_NAV_BTN_H = 58f;
    public static final float COMP_NAV_BTN_Y = COMPENDIUM_Y + 68f;
    public static final float COMP_PREV_BTN_X = COMPENDIUM_X + 36f;
    public static final float COMP_NEXT_BTN_X = COMPENDIUM_X + COMPENDIUM_W - 36f - COMP_NAV_BTN_W;

    private BoardMetrics() {
    }

    public static float getLeftPanelX(float worldW) {
        float screenLeft = (VIEW_W - worldW) * 0.5f;
        float maxLeft = BOARD_X - SIDE_PANEL_W - 22f;
        return Math.min(screenLeft + SIDE_MARGIN, maxLeft);
    }

    public static float getRightPanelX(float worldW) {
        float screenRight = (VIEW_W + worldW) * 0.5f;
        float minRight = BOARD_X + BOARD_PX + 22f;
        return Math.max(screenRight - SIDE_MARGIN - SIDE_PANEL_W, minRight);
    }

    public static float getTopCardY() {
        return BOARD_Y + BOARD_PX - SCORE_CARD_H;
    }

    public static float getSecondLeftCardY() {
        return getTopCardY() - 18f - LIVES_CARD_H;
    }

    public static float getInfoRightCardY() {
        return getTopCardY() - 18f - INFO_CARD_H;
    }

    public static float getLanguageButtonX(int index) {
        int col = index / LANG_ROWS;
        return LANG_GRID_START_X + col * (LANG_BTN_W + LANG_COL_GAP);
    }

    public static float getLanguageButtonY(int index) {
        int row = index % LANG_ROWS;
        return LANG_GRID_FIRST_Y - row * (LANG_BTN_H + LANG_ROW_GAP);
    }
}