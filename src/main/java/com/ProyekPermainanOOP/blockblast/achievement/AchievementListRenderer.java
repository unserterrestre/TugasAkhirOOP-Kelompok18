package com.ProyekPermainanOOP.blockblast.achievement;

import com.ProyekPermainanOOP.blockblast.graphics.BoardMetrics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class AchievementListRenderer implements Disposable {

    public static final Rectangle BACK_BUTTON = new Rectangle(
            32f, 28f, 190f, 58f
    );

    private static final float CARD_X = 48f;
    private static final float CARD_W = BoardMetrics.VIEW_W - 96f;
    private static final float CARD_GAP = 8f;
    private static final float TOP_Y = BoardMetrics.VIEW_H - 190f;
    private static final float CARD_H = 72f;

    // Alpha 1f membuat background benar-benar menutupi menu di belakang.
    private static final Color BG = new Color(
            0.015f, 0.055f, 0.045f, 1f
    );

    private static final Color CARD_LOCKED = new Color(
            0.055f, 0.17f, 0.13f, 0.97f
    );

    private static final Color CARD_UNLOCKED = new Color(
            0.09f, 0.30f, 0.22f, 0.98f
    );

    private static final Color GOLD = new Color(
            1f, 0.76f, 0.25f, 1f
    );

    private static final Color MINT = new Color(
            0.40f, 0.95f, 0.72f, 1f
    );

    private static final Color MUTED = new Color(
            0.72f, 0.82f, 0.77f, 1f
    );

    private final ShapeRenderer shape = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();

    private final AchievementInfo[] achievements = {
            new AchievementInfo(
                    "first_move",
                    "Langkah Pertama",
                    "Tempatkan balok pertama.",
                    1,
                    AchievementInfo.Type.BLOCKS
            ),

            new AchievementInfo(
                    "ten_blocks",
                    "Pemula",
                    "Tempatkan 10 balok.",
                    10,
                    AchievementInfo.Type.BLOCKS
            ),

            new AchievementInfo(
                    "fifty_blocks",
                    "Pemain Aktif",
                    "Tempatkan 50 balok.",
                    50,
                    AchievementInfo.Type.BLOCKS
            ),

            new AchievementInfo(
                    "hundred_blocks",
                    "Master Balok",
                    "Tempatkan 100 balok.",
                    100,
                    AchievementInfo.Type.BLOCKS
            ),

            new AchievementInfo(
                    "line_breaker",
                    "Pemecah Baris",
                    "Bersihkan 10 baris.",
                    10,
                    AchievementInfo.Type.LINES
            ),

            new AchievementInfo(
                    "combo_master",
                    "Combo Master",
                    "Capai combo x5.",
                    5,
                    AchievementInfo.Type.COMBO
            )
    };

    public AchievementListRenderer() {
        font.getData().setScale(1.25f);
    }

    public void render(Matrix4 projection, AchievementManager manager) {
        if (manager == null) {
            return;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
                GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shape.setProjectionMatrix(projection);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Background halaman achievement.
        shape.setColor(BG);
        shape.rect(
                0f,
                0f,
                BoardMetrics.VIEW_W,
                BoardMetrics.VIEW_H
        );

        drawHeader();
        drawAchievementCards(manager);
        drawBackButton();

        shape.end();

        batch.setProjectionMatrix(projection);
        batch.begin();

        drawTexts(manager);

        batch.end();
    }

    private void drawHeader() {
        float centerX = BoardMetrics.VIEW_W * 0.5f;

        shape.setColor(GOLD);

        shape.rect(
                centerX - 230f,
                BoardMetrics.VIEW_H - 115f,
                460f,
                3f
        );

        shape.rect(
                centerX - 230f,
                BoardMetrics.VIEW_H - 143f,
                460f,
                2f
        );
    }

    private void drawAchievementCards(AchievementManager manager) {
        for (int i = 0; i < achievements.length; i++) {
            AchievementInfo info = achievements[i];

            float y = TOP_Y - i * (CARD_H + CARD_GAP);

            boolean unlocked =
                    manager.isAchievementUnlocked(info.id);

            // Border kartu.
            shape.setColor(unlocked ? GOLD : MUTED);

            shape.rect(
                    CARD_X - 3f,
                    y - 3f,
                    CARD_W + 6f,
                    CARD_H + 6f
            );

            // Background kartu.
            shape.setColor(
                    unlocked ? CARD_UNLOCKED : CARD_LOCKED
            );

            shape.rect(
                    CARD_X,
                    y,
                    CARD_W,
                    CARD_H
            );

            // Progress bar.
            float progress = getProgress(manager, info);

            float barX = CARD_X + 18f;
            float barY = y + 12f;
            float barW = CARD_W - 36f;

            // Background progress bar.
            shape.setColor(
                    0.015f,
                    0.05f,
                    0.04f,
                    1f
            );

            shape.rect(
                    barX,
                    barY,
                    barW,
                    6f
            );

            // Progress yang sudah dicapai.
            shape.setColor(unlocked ? GOLD : MINT);

            shape.rect(
                    barX,
                    barY,
                    barW * progress,
                    6f
            );
        }
    }

    private void drawBackButton() {
        // Border tombol kembali.
        shape.setColor(GOLD);

        shape.rect(
                BACK_BUTTON.x - 3f,
                BACK_BUTTON.y - 3f,
                BACK_BUTTON.width + 6f,
                BACK_BUTTON.height + 6f
        );

        // Background tombol.
        shape.setColor(
                0.04f,
                0.20f,
                0.15f,
                1f
        );

        shape.rect(
                BACK_BUTTON.x,
                BACK_BUTTON.y,
                BACK_BUTTON.width,
                BACK_BUTTON.height
        );
    }

    private void drawTexts(AchievementManager manager) {
        // Judul halaman.
        font.setColor(GOLD);

        String title = "DAFTAR PENCAPAIAN";

        layout.setText(font, title);

        font.draw(
                batch,
                title,
                (BoardMetrics.VIEW_W - layout.width) * 0.5f,
                BoardMetrics.VIEW_H - 72f
        );

        // Teks setiap pencapaian.
        for (int i = 0; i < achievements.length; i++) {
            AchievementInfo info = achievements[i];

            float y = TOP_Y - i * (CARD_H + CARD_GAP);

            boolean unlocked =
                    manager.isAchievementUnlocked(info.id);

            // Nama achievement.
            font.setColor(unlocked ? GOLD : Color.WHITE);

            font.draw(
                    batch,
                    info.title,
                    CARD_X + 18f,
                    y + 60f
            );

            // Deskripsi achievement.
            font.setColor(MUTED);

            font.draw(
                    batch,
                    info.description,
                    CARD_X + 18f,
                    y + 39f
            );

            // Status achievement.
            font.setColor(unlocked ? MINT : MUTED);

            String status = unlocked
                    ? "TERBUKA"
                    : "TERKUNCI";

            layout.setText(font, status);

            font.draw(
                    batch,
                    status,
                    CARD_X + CARD_W - layout.width - 18f,
                    y + 60f
            );

            // Progress angka.
            int current = getCurrentValue(manager, info);

            String progressText =
                    Math.min(current, info.target)
                            + "/" + info.target;

            font.setColor(Color.WHITE);

            layout.setText(font, progressText);

            font.draw(
                    batch,
                    progressText,
                    CARD_X + CARD_W - layout.width - 18f,
                    y + 22f
            );
        }

        // Teks tombol kembali.
        font.setColor(Color.WHITE);

        String back = "KEMBALI";

        layout.setText(font, back);

        font.draw(
                batch,
                back,
                BACK_BUTTON.x
                        + (BACK_BUTTON.width - layout.width) * 0.5f,
                BACK_BUTTON.y + 36f
        );
    }

    private float getProgress(
            AchievementManager manager,
            AchievementInfo info
    ) {
        return MathUtils.clamp(
                (float) getCurrentValue(manager, info)
                        / info.target,
                0f,
                1f
        );
    }

    private int getCurrentValue(
            AchievementManager manager,
            AchievementInfo info
    ) {
        switch (info.type) {
            case BLOCKS:
                return manager.getTotalBlocksPlaced();

            case LINES:
                return manager.getTotalLinesCleared();

            case COMBO:
                return manager.getHighestCombo();

            default:
                return 0;
        }
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        font.dispose();
    }

    private static class AchievementInfo {

        enum Type {
            BLOCKS,
            LINES,
            COMBO
        }

        final String id;
        final String title;
        final String description;
        final int target;
        final Type type;

        AchievementInfo(
                String id,
                String title,
                String description,
                int target,
                Type type
        ) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.target = target;
            this.type = type;
        }
    }
}
