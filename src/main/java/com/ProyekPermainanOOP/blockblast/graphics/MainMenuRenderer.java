package com.ProyekPermainanOOP.blockblast.graphics;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager;
import com.ProyekPermainanOOP.blockblast.model.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

/**
 * Merender tampilan Main Menu sebelum pemain masuk ke sesi permainan.
 */
public class MainMenuRenderer {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final BitmapFont fallbackFont;
    private final GlyphLayout layout;

    // Area klik tombol (dalam koordinat dunia 720 x 1000)
    public static final Rectangle BTN_PLAY = new Rectangle(180f, 430f, 360f, 76f);
    public static final Rectangle BTN_MODE = new Rectangle(180f, 330f, 360f, 64f);
    public static final Rectangle BTN_COMPENDIUM = new Rectangle(180f, 242f, 360f, 60f);
    public static final Rectangle BTN_LANGUAGE = new Rectangle(180f, 160f, 360f, 56f);

    public MainMenuRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.fallbackFont = new BitmapFont();
        this.layout = new GlyphLayout();
    }

    private BitmapFont getFont() {
        return (GameAssets.uiFont != null) ? GameAssets.uiFont : fallbackFont;
    }

    public void render(Matrix4 projectionMatrix,
                       GameMode selectedMode,
                       int highScore,
                       float hoverX,
                       float hoverY,
                       LocalizationManager localization) {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Panel kartu utama di tengah layar
        shapeRenderer.setColor(0.04f, 0.14f, 0.11f, 0.88f);
        shapeRenderer.rect(120f, 115f, 480f, 770f);

        // Bingkai emas tipis kartu menu
        shapeRenderer.setColor(0.85f, 0.68f, 0.25f, 0.9f);
        shapeRenderer.rect(120f, 881f, 480f, 4f);
        shapeRenderer.rect(120f, 115f, 480f, 4f);

        // Render kotak masing-masing tombol dengan efek hover
        drawButtonBox(BTN_PLAY, hoverX, hoverY,
                new Color(0.14f, 0.56f, 0.36f, 1f),
                new Color(0.19f, 0.68f, 0.44f, 1f));

        drawButtonBox(BTN_MODE, hoverX, hoverY,
                new Color(0.12f, 0.34f, 0.28f, 1f),
                new Color(0.18f, 0.45f, 0.37f, 1f));

        drawButtonBox(BTN_COMPENDIUM, hoverX, hoverY,
                new Color(0.12f, 0.30f, 0.25f, 1f),
                new Color(0.18f, 0.40f, 0.34f, 1f));

        drawButtonBox(BTN_LANGUAGE, hoverX, hoverY,
                new Color(0.10f, 0.25f, 0.21f, 1f),
                new Color(0.16f, 0.35f, 0.30f, 1f));

        shapeRenderer.end();

        // Render teks judul, skor terbaik, dan label tombol
        batch.setProjectionMatrix(projectionMatrix);
        batch.begin();

        BitmapFont font = getFont();

        // Judul Utama
        font.getData().setScale(3.4f);
        font.setColor(new Color(0.96f, 0.80f, 0.32f, 1f));
        drawCenteredText(font, "P U S A K A", 360f, 825f);

        // Subjudul
        font.getData().setScale(1.4f);
        font.setColor(new Color(0.78f, 0.92f, 0.86f, 1f));
        drawCenteredText(font, "MAHJONG CALLIGRAPHY PUZZLE", 360f, 770f);

        // Info High Score
        font.getData().setScale(1.15f);
        font.setColor(Color.WHITE);
        drawCenteredText(font, "HIGH SCORE : " + highScore, 360f, 695f);

        // Petunjuk singkat
        font.getData().setScale(1.15f);
        font.setColor(new Color(0.72f, 0.85f, 0.80f, 1f));
        drawCenteredText(font, "Cocokkan 4 ubin sebaris atau 5 ubin berkelompok", 360f, 640f);
        drawCenteredText(font, "Gabungkan aksara Hanzi untuk memicu Sinergi!", 360f, 612f);

        // Teks Tombol
        font.getData().setScale(1.65f);
        font.setColor(Color.WHITE);
        drawCenteredText(font, "MULAI BERMAIN", 360f, BTN_PLAY.y + 48f);

        font.getData().setScale(1.4f);
        font.setColor(Color.GOLD);
        String modeLabel = (localization != null) ? localization.getModeName(selectedMode) : selectedMode.name();
        drawCenteredText(font, "MODE: " + modeLabel, 360f, BTN_MODE.y + 41f);

        font.getData().setScale(1.35f);
        font.setColor(Color.WHITE);
        drawCenteredText(font, "KOLEKSI PUSAKA [K]", 360f, BTN_COMPENDIUM.y + 39f);

        font.getData().setScale(1.3f);
        font.setColor(new Color(0.85f, 0.95f, 0.90f, 1f));
        drawCenteredText(font, "GANTI BAHASA [L]", 360f, BTN_LANGUAGE.y + 36f);

        // Footer shortcut keyboard
        font.getData().setScale(1.05f);
        font.setColor(new Color(0.65f, 0.78f, 0.73f, 1f));
        drawCenteredText(font, "Tekan [ENTER] untuk Mulai  |  [ESC] di Game untuk Menu", 360f, 142f);

        // Kembalikan skala font ke normal agar tidak memengaruhi renderer lain
        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void drawButtonBox(Rectangle rect, float hoverX, float hoverY, Color baseColor, Color hoverColor) {
        boolean isHovered = rect.contains(hoverX, hoverY);
        shapeRenderer.setColor(isHovered ? hoverColor : baseColor);
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    }

    private void drawCenteredText(BitmapFont font, String text, float centerX, float y) {
        layout.setText(font, text);
        font.draw(batch, text, centerX - (layout.width / 2f), y);
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        fallbackFont.dispose();
    }
}