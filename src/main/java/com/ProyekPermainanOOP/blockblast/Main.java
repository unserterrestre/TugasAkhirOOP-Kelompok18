
package com.ProyekPermainanOOP.blockblast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.ProyekPermainanOOP.blockblast.graphics.BoardRenderer;
import com.ProyekPermainanOOP.blockblast.model.Board;
import com.ProyekPermainanOOP.blockblast.tray.BlockTray;

public class Main extends ApplicationAdapter {

    // Ukuran virtual game
    private static final float GAME_WIDTH = 720;
    private static final float GAME_HEIGHT = 1000;

    // Komponen utama game
    private FitViewport viewport;
    private BoardRenderer boardRenderer;
    private BlockTray blockTray;

    private Board board;

    @Override
    public void create() {

        // Mengatur koordinat tampilan game
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);

        // Menyiapkan komponen dari anggota lain
        boardRenderer = new BoardRenderer();
        blockTray = new BlockTray();

        // Membuat papan 8x8 kosong
        board = new Board();

        // Mengatur viewport pertama kali
        viewport.update(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                true
        );

    }

    @Override
    public void render() {

        // Mengaktifkan viewport
        viewport.apply();

        // Membersihkan layar
        Gdx.gl.glClearColor(
                0.04f, 0.15f, 0.12f, 1f
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Menggambar papan dan tiga blok
        boardRenderer.begin(viewport.getCamera().combined);
        boardRenderer.drawBackground();
        boardRenderer.drawBoard(board.getGrid());
        boardRenderer.drawTray(blockTray);
        boardRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

        // Menyesuaikan tampilan saat window diubah
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {

        // Membersihkan resource grafis
        if (boardRenderer != null) {
            boardRenderer.dispose();
        }

        System.out.println("Game ditutup.");
    }
}
