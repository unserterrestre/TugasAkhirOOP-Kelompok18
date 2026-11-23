
package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BoardGridRenderer {

    private final SpecialBadgeRenderer badgeRenderer =
            new SpecialBadgeRenderer();

    public void drawShapes(
            ShapeRenderer shape,
            int[][] grid,
            float elapsedAnimTime) {

        shape.rect(
                BoardMetrics.BOARD_X - 12f,
                BoardMetrics.BOARD_Y - 12f,
                BoardMetrics.BOARD_PX + 24f,
                BoardMetrics.BOARD_PX + 24f,
                BoardPalette.PANEL_BOTTOM,
                BoardPalette.PANEL_BOTTOM,
                BoardPalette.PANEL_TOP,
                BoardPalette.PANEL_TOP
        );

        float pulse = 0.5f
                + 0.5f * (float) Math.sin(elapsedAnimTime * 3.0f);

        for (int r = 0; r < BoardMetrics.GRID_SIZE; r++) {
            for (int c = 0; c < BoardMetrics.GRID_SIZE; c++) {

                float x = BoardMetrics.BOARD_X
                        + c * BoardMetrics.CELL;

                float y = BoardMetrics.BOARD_Y
                        + (BoardMetrics.GRID_SIZE - 1 - r)
                        * BoardMetrics.CELL;

                int val = grid[r][c];

                if (val == 0) {
                    Color top = BoardPalette.EMPTY_TOP.cpy()
                            .lerp(BoardPalette.PANEL_TOP, pulse * 0.25f);

                    shape.rect(
                            x + 2.5f,
                            y + 2.5f,
                            BoardMetrics.CELL - 5f,
                            BoardMetrics.CELL - 5f,
                            BoardPalette.EMPTY_BOTTOM,
                            BoardPalette.EMPTY_BOTTOM,
                            top,
                            top
                    );

                } else {
                    shape.rect(
                            x + 2.5f,
                            y + 2.5f,
                            BoardMetrics.CELL - 5f,
                            BoardMetrics.CELL - 5f,
                            BoardPalette.TILE_BOTTOM,
                            BoardPalette.TILE_BOTTOM,
                            BoardPalette.TILE_TOP,
                            BoardPalette.TILE_TOP
                    );

                    badgeRenderer.drawBadge(
                            shape,
                            x + 2.5f,
                            y + 2.5f,
                            BoardMetrics.CELL - 5f,
                            SpecialTileType.fromEncoded(val),
                            elapsedAnimTime
                    );
                }
            }
        }
    }

    public void drawTiles(SpriteBatch batch, int[][] grid) {
        drawTiles(batch, grid, null);
    }

    public void drawTiles(
            SpriteBatch batch,
            int[][] grid,
            AnimationManager animationManager) {

        if (GameAssets.chineseCharTiles == null
                || GameAssets.chineseCharTiles.length == 0) {
            return;
        }

        for (int r = 0; r < BoardMetrics.GRID_SIZE; r++) {
            for (int c = 0; c < BoardMetrics.GRID_SIZE; c++) {

                int tileId = grid[r][c];

                if (tileId > 0) {
                    TextureRegion region =
                            BoardPalette.getTileRegion(tileId);

                    if (region != null) {
                        float x = BoardMetrics.BOARD_X
                                + c * BoardMetrics.CELL + 8f;

                        float y = BoardMetrics.BOARD_Y
                                + (BoardMetrics.GRID_SIZE - 1 - r)
                                * BoardMetrics.CELL + 8f;

                        float tileSize = BoardMetrics.CELL - 16f;

                        float scale = 1f;

                        if (animationManager != null) {
                            scale = animationManager.getScaleAt(r, c);
                        }

                        float drawSize = tileSize * scale;

                        float centerX = x + tileSize / 2f;
                        float centerY = y + tileSize / 2f;

                        batch.draw(
                                region,
                                centerX - drawSize / 2f,
                                centerY - drawSize / 2f,
                                drawSize,
                                drawSize
                        );
                    }
                }
            }
        }
    }
}
