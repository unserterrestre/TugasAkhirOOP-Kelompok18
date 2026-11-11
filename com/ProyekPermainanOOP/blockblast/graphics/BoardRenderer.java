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

    public static final int GRID_SIZE = 8;
    public static final float CELL = 64f;
    public static final float BOARD_PX = GRID_SIZE * CELL;
    public static final float BOARD_X = (VIEW_W - BOARD_PX) / 2f;
    public static final float BOARD_Y = 360f;

    private final ShapeRenderer shape = new ShapeRenderer();

    // Warna sementara; minggu depan dipindah ke GameAssets (tema Mahjong)
    private final Color background = Color.valueOf("0B3D2E");
    private final Color panel = Color.valueOf("082A20");
    private final Color emptyCell = Color.valueOf("114D3B");
    private final Color filledCell = Color.valueOf("FBF6E6");

    public void begin(Matrix4 projection) {

    }
}