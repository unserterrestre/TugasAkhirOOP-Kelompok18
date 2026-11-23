package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.input.DragDropManager;
import com.ProyekPermainanOOP.blockblast.model.Block;
import com.ProyekPermainanOOP.blockblast.model.SpecialTileType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlacementGuidanceRenderer {

    public void draw(ShapeRenderer shape, int[][] grid, DragDropManager dragDropManager, boolean modalOpen, float elapsedAnimTime) {
        if (dragDropManager == null || !dragDropManager.isDragging() || modalOpen) {
            return;
        }
        Block block = dragDropManager.getActiveBlock();
        int startRow = dragDropManager.getPreviewRow();
        int startCol = dragDropManager.getPreviewCol();
        if (block == null) {
            return;
        }

        int[][] shapeGrid = block.getShape();
        int[][] tileGrid = block.getTileGrid();
        boolean valid = dragDropManager.isPreviewValid();
        float pulse = 0.5f + 0.5f * (float) Math.sin(elapsedAnimTime * 9f);
        float shake = valid ? 0f : (float) Math.sin(elapsedAnimTime * 36f) * 3.5f;

        int matchingNeighbors = 0;
        if (valid && startRow >= 0 && startCol >= 0) {
            matchingNeighbors = highlightMatchingNeighbors(shape, grid, block, shapeGrid, tileGrid, startRow, startCol, pulse);
        }

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (shapeGrid[r][c] == 1) {
                    int boardRow = startRow + r;
                    int boardCol = startCol + c;
                    if (boardRow >= 0 && boardRow < BoardMetrics.GRID_SIZE && boardCol >= 0 && boardCol < BoardMetrics.GRID_SIZE) {
                        float x = BoardMetrics.BOARD_X + boardCol * BoardMetrics.CELL + shake;
                        float y = BoardMetrics.BOARD_Y + (BoardMetrics.GRID_SIZE - 1 - boardRow) * BoardMetrics.CELL;
                        if (valid) {
                            if (matchingNeighbors > 0) {
                                shape.setColor(0.98f, 0.82f, 0.25f, 0.42f + 0.24f * pulse);
                            } else {
                                shape.setColor(0.24f, 0.94f, 0.56f, 0.34f + 0.20f * pulse);
                            }
                        } else {
                            shape.setColor(0.96f, 0.26f, 0.28f, 0.42f + 0.22f * pulse);
                        }
                        shape.rect(x + 2.5f, y + 2.5f, BoardMetrics.CELL - 5f, BoardMetrics.CELL - 5f);
                    }
                }
            }
        }
    }

    private int highlightMatchingNeighbors(ShapeRenderer shape, int[][] grid, Block block, int[][] shapeGrid,
                                           int[][] tileGrid, int startRow, int startCol, float pulse) {
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        boolean[][] highlightedMatch = new boolean[BoardMetrics.GRID_SIZE][BoardMetrics.GRID_SIZE];
        int matchingNeighbors = 0;

        for (int r = 0; r < block.getRows(); r++) {
            for (int c = 0; c < block.getCols(); c++) {
                if (shapeGrid[r][c] == 1) {
                    int br = startRow + r;
                    int bc = startCol + c;
                    if (br >= 0 && br < BoardMetrics.GRID_SIZE && bc >= 0 && bc < BoardMetrics.GRID_SIZE) {
                        int rawTile = (tileGrid[r][c] > 0) ? tileGrid[r][c] : block.getTileId();
                        int baseTile = SpecialTileType.extractBaseTileId(rawTile);
                        boolean isWild = SpecialTileType.fromEncoded(rawTile) == SpecialTileType.JADE_WILDCARD;

                        for (int d = 0; d < 4; d++) {
                            int nr = br + dr[d];
                            int nc = bc + dc[d];
                            if (nr >= 0 && nr < BoardMetrics.GRID_SIZE && nc >= 0 && nc < BoardMetrics.GRID_SIZE && grid[nr][nc] > 0) {
                                int neighborBase = SpecialTileType.extractBaseTileId(grid[nr][nc]);
                                boolean neighborWild = SpecialTileType.fromEncoded(grid[nr][nc]) == SpecialTileType.JADE_WILDCARD;
                                if (isWild || neighborWild || neighborBase == baseTile) {
                                    matchingNeighbors++;
                                    if (!highlightedMatch[nr][nc]) {
                                        highlightedMatch[nr][nc] = true;
                                        float mx = BoardMetrics.BOARD_X + nc * BoardMetrics.CELL;
                                        float my = BoardMetrics.BOARD_Y + (BoardMetrics.GRID_SIZE - 1 - nr) * BoardMetrics.CELL;
                                        shape.setColor(1f, 0.84f, 0.22f, 0.32f + 0.25f * pulse);
                                        shape.rect(mx + 1.5f, my + 1.5f, BoardMetrics.CELL - 3f, BoardMetrics.CELL - 3f);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return matchingNeighbors;
    }
}