
package com.ProyekPermainanOOP.blockblast.achievement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AchievementRenderer {

    private static final float POPUP_DURATION = 3.5f;

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final ScreenViewport viewport;

    private Achievement currentAchievement;
    private float timer;

    public AchievementRenderer() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        font.getData().setScale(1.1f);
    }

    public void render(AchievementManager achievementManager) {
        if (achievementManager == null) {
            return;
        }

        // Ambil achievement baru dari antrean.
        if (currentAchievement == null) {
            currentAchievement =
                    achievementManager.pollUnlockedAchievement();

            if (currentAchievement != null) {
                timer = POPUP_DURATION;
            }
        }

        // Tidak ada achievement yang sedang ditampilkan.
        if (currentAchievement == null) {
            return;
        }

        timer -= Gdx.graphics.getDeltaTime();

        // Hilangkan popup setelah beberapa detik.
        if (timer <= 0f) {
            currentAchievement = null;
            return;
        }

        drawPopup(currentAchievement);
    }

    private void drawPopup(Achievement achievement) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        viewport.update(screenWidth, screenHeight, true);

        batch.setProjectionMatrix(
                viewport.getCamera().combined
        );

        shapeRenderer.setProjectionMatrix(
                viewport.getCamera().combined
        );

        float popupWidth = Math.min(
                460f,
                screenWidth - 32f
        );

        float popupHeight = 125f;

        float popupX =
                (screenWidth - popupWidth) / 2f;

        float popupY =
                screenHeight - popupHeight - 30f;

        // =========================
        // GAMBAR BACKGROUND POPUP
        // =========================

        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glBlendFunc(
                GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        // Background gelap.
        shapeRenderer.setColor(
                0.08f,
                0.10f,
                0.16f,
                0.95f
        );

        shapeRenderer.rect(
                popupX,
                popupY,
                popupWidth,
                popupHeight
        );

        // Garis emas di bagian atas.
        shapeRenderer.setColor(
                1f,
                0.75f,
                0.20f,
                1f
        );

        shapeRenderer.rect(
                popupX,
                popupY + popupHeight - 5f,
                popupWidth,
                5f
        );

        shapeRenderer.end();

        // =========================
        // GAMBAR TEKS POPUP
        // =========================

        batch.begin();

        // Judul popup.
        font.setColor(
                1f,
                0.78f,
                0.25f,
                1f
        );

        String title = "ACHIEVEMENT UNLOCKED!";

        layout.setText(font, title);

        font.draw(
                batch,
                title,
                popupX + (popupWidth - layout.width) / 2f,
                popupY + popupHeight - 25f
        );

        // Nama achievement.
        font.setColor(Color.WHITE);

        String name = achievement.getName();

        layout.setText(font, name);

        font.draw(
                batch,
                name,
                popupX + (popupWidth - layout.width) / 2f,
                popupY + popupHeight - 55f
        );

        // Deskripsi achievement.
        font.setColor(
                0.82f,
                0.85f,
                0.90f,
                1f
        );

        String description =
                achievement.getDescription();

        font.draw(
                batch,
                description,
                popupX + 15f,
                popupY + 40f,
                popupWidth - 30f,
                Align.center,
                true
        );

        batch.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
