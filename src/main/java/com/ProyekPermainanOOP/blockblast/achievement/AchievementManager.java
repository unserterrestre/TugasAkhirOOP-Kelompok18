
package com.ProyekPermainanOOP.blockblast.achievement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public class AchievementManager {

    private static final String PREF_NAME =
            "BlockBlastAchievements";

    private final Preferences preferences;

    private final Map<String, Achievement> achievements =
            new LinkedHashMap<>();

    private final Queue<Achievement> unlockedQueue =
            new ArrayDeque<>();

    private int totalBlocksPlaced;
    private int totalLinesCleared;
    private int highestCombo;

    public AchievementManager() {
        preferences = Gdx.app.getPreferences(PREF_NAME);

        // Memuat statistik yang tersimpan.
        totalBlocksPlaced =
                preferences.getInteger("totalBlocksPlaced", 0);

        totalLinesCleared =
                preferences.getInteger("totalLinesCleared", 0);

        highestCombo =
                preferences.getInteger("highestCombo", 0);

        // Achievement langkah pertama.
        addAchievement(
                "first_move",
                "First Move",
                "Berhasil menaruh balok pertama!"
        );

        // Achievement jumlah balok.
        addAchievement(
                "ten_blocks",
                "10 Blocks",
                "Berhasil menaruh 10 balok!"
        );

        addAchievement(
                "fifty_blocks",
                "50 Blocks",
                "Berhasil menaruh 50 balok!"
        );

        addAchievement(
                "hundred_blocks",
                "100 Blocks",
                "Berhasil menaruh 100 balok!"
        );

        // Achievement garis dan combo.
        addAchievement(
                "line_breaker",
                "Line Breaker",
                "Berhasil membersihkan 10 garis!"
        );

        addAchievement(
                "combo_master",
                "Combo Master",
                "Berhasil mencapai combo 5!"
        );

        // Memuat status achievement yang sudah terbuka.
        loadUnlockedAchievements();
    }

    private void addAchievement(
            String id,
            String name,
            String description
    ) {
        achievements.put(
                id,
                new Achievement(
                        id,
                        name,
                        description
                )
        );
    }

    private void loadUnlockedAchievements() {
        for (Achievement achievement : achievements.values()) {
            boolean unlocked = preferences.getBoolean(
                    achievement.getId(),
                    false
            );

            if (unlocked) {
                achievement.unlock();
            }
        }
    }

    /**
     * Dipanggil ketika pemain berhasil menaruh balok.
     */
    public void onBlockPlaced() {
        totalBlocksPlaced++;

        preferences.putInteger(
                "totalBlocksPlaced",
                totalBlocksPlaced
        );

        preferences.flush();

        // Achievement balok pertama.
        if (totalBlocksPlaced >= 1) {
            unlockAchievement("first_move");
        }

        // Achievement 10 balok.
        if (totalBlocksPlaced >= 10) {
            unlockAchievement("ten_blocks");
        }

        // Achievement 50 balok.
        if (totalBlocksPlaced >= 50) {
            unlockAchievement("fifty_blocks");
        }

        // Achievement 100 balok.
        if (totalBlocksPlaced >= 100) {
            unlockAchievement("hundred_blocks");
        }
    }

    /**
     * Dipanggil ketika pemain membersihkan garis.
     */
    public void onLinesCleared(int lines) {
        if (lines <= 0) {
            return;
        }

        totalLinesCleared += lines;

        preferences.putInteger(
                "totalLinesCleared",
                totalLinesCleared
        );

        preferences.flush();

        if (totalLinesCleared >= 10) {
            unlockAchievement("line_breaker");
        }
    }

    /**
     * Dipanggil ketika combo pemain berubah.
     */
    public void onComboChanged(int combo) {
        if (combo > highestCombo) {
            highestCombo = combo;

            preferences.putInteger(
                    "highestCombo",
                    highestCombo
            );

            preferences.flush();
        }

        if (combo >= 5) {
            unlockAchievement("combo_master");
        }
    }

    /**
     * Membuka achievement dan memasukkannya
     * ke antrean popup jika belum pernah terbuka.
     */
    private void unlockAchievement(String id) {
        Achievement achievement = achievements.get(id);

        if (achievement == null) {
            return;
        }

        if (achievement.isUnlocked()) {
            return;
        }

        achievement.unlock();

        preferences.putBoolean(id, true);
        preferences.flush();

        unlockedQueue.add(achievement);
    }

    /**
     * Diambil oleh AchievementRenderer
     * untuk menampilkan popup.
     */
    public Achievement pollUnlockedAchievement() {
        return unlockedQueue.poll();
    }

    public int getTotalBlocksPlaced() {
        return totalBlocksPlaced;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    public int getHighestCombo() {
        return highestCombo;
    }

    public boolean isAchievementUnlocked(String id) {
        Achievement achievement = achievements.get(id);

        return achievement != null
                && achievement.isUnlocked();
    }
}
