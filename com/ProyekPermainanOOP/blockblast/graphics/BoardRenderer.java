package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Minggu 1: menggambar papan 8x8.
 * Hanya bertugas menggambar; data papan diterima sebagai int[][]
 * (0 = kosong, lebih dari 0 = terisi). Baris 0 ada di sisi ATAS papan.
 *
 * Koordinat dunia: (0,0) kiri-bawah, ukuran virtual 720 x 1000.
 */
public class BoardRenderer implements Disposable {

    public static final float VIEW_W = 720f;
    public static final float VIEW_H = 1000f;

    public static final int   GRID_SIZE = 8;
    public static final float CELL      = 64f;
    public static final float BOARD_PX  = GRID_SIZE * CELL;
    public static final float BOARD_X   = (VIEW_W - BOARD_PX) / 2f;
    public static final float BOARD_Y   = 360f;

    private final ShapeRenderer shape = new ShapeRenderer();

    // Warna sementara; minggu depan dipindah ke GameAssets (tema Mahjong)
    private final Color background = Color.valueOf("0B3D2E");
    private final Color panel      = Color.valueOf("082A20");
    private final Color emptyCell  = Color.valueOf("114D3B");
    private final Color filledCell = Color.valueOf("FBF6E6");

    public void begin(Matrix4 projection) {
        shape.setProjectionMatrix(projection);
        shape.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void end() {
        shape.end();
    }

    public void drawBackground() {
        shape.setColor(background);
        shape.rect(0, 0, VIEW_W, VIEW_H);
    }

    public void drawBoard(int[][] grid) {
        shape.setColor(panel);
        shape.rect(BOARD_X - 10, BOARD_Y - 10, BOARD_PX + 20, BOARD_PX + 20);

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                float x = BOARD_X + c * CELL;
                float y = BOARD_Y + (GRID_SIZE - 1 - r) * CELL;
                shape.setColor(grid[r][c] == 0 ? emptyCell : filledCell);
                shape.rect(x + 2, y + 2, CELL - 4, CELL - 4);
            }
        }
    }

    /** Piksel dunia -> (col, row) sel papan, atau null jika di luar papan. */
    public Vector2 pixelToCell(float px, float py) {
        int col = (int) Math.floor((px - BOARD_X) / CELL);
        int row = (int) Math.floor((BOARD_Y + BOARD_PX - py) / CELL);
        if (col < 0 || col >= GRID_SIZE || row < 0 || row >= GRID_SIZE) return null;
        return new Vector2(col, row);
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}