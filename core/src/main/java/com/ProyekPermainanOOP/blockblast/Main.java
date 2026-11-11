
package com.ProyekPermainanOOP.blockblast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {
    private static final int BOARD_SIZE = 8;
    private static final float CELL_SIZE = 64f;
    private static final float GAP = 3f;

    private static final float VIEW_WIDTH = 720f;
    private static final float VIEW_HEIGHT = 1000f;

    private ShapeRenderer renderer;
    private FitViewport viewport;

    @Override
    public void create() {
        renderer = new ShapeRenderer();

        viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.app.log("TileFusion", "Game OOP Kelompok 6 berhasil dimulai!");
    }

    @Override
    public void render() {

        viewport.apply();

        Gdx.gl.glClearColor(
                0.04f, 0.18f, 0.13f, 1f
        );

        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
        );

        renderer.setProjectionMatrix(
                viewport.getCamera().combined
        );

        renderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        float boardWidth = BOARD_SIZE * CELL_SIZE;

        float startX =
                (VIEW_WIDTH - boardWidth) / 2f;

        float startY = 350f;

        // Background papan
        renderer.setColor(
                0.03f, 0.12f, 0.09f, 1f
        );

        renderer.rect(
                startX - 12,
                startY - 12,
                boardWidth + 24,
                boardWidth + 24
        );

        // Membuat papan 8x8
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                float x = startX + col * CELL_SIZE + GAP;

                float y = startY + (BOARD_SIZE - 1 - row) * CELL_SIZE + GAP;

                renderer.setColor(0.10f, 0.35f, 0.27f, 1f);

                renderer.rect(x, y, CELL_SIZE - 2 * GAP, CELL_SIZE - 2 * GAP);
            }
        }

        renderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (renderer != null) {
            renderer.dispose();
        }
    }
}