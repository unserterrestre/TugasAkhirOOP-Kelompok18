package com.ProyekPermainanOOP.blockblast.model;

import java.util.ArrayList;
import java.util.List;

public class PusakaCompendium {

    public static class RelicEntry {
        private final int baseTileId;
        private final String pinyin;
        private final String meaningId;
        private final String meaningEn;
        private int clearedCount;

        public RelicEntry(int baseTileId, String pinyin, String meaningId, String meaningEn) {
            this.baseTileId = baseTileId;
            this.pinyin = pinyin;
            this.meaningId = meaningId;
            this.meaningEn = meaningEn;
            this.clearedCount = 0;
        }

        public int getBaseTileId() {
            return baseTileId;
        }

        public String getPinyin() {
            return pinyin;
        }

        public String getMeaning(boolean indonesian) {
            return indonesian ? meaningId : meaningEn;
        }

        public int getClearedCount() {
            return clearedCount;
        }

        public boolean isUnlocked() {
            return clearedCount >= 4;
        }

        public void addCleared(int count) {
            this.clearedCount += count;
        }
    }

    private static PusakaCompendium instance;
    private final List<RelicEntry> entries;

    private PusakaCompendium() {
        entries = new ArrayList<>();
        entries.add(new RelicEntry(1, "ZHI", "Kebijaksanaan & Aliran", "Wisdom & Flow"));
        entries.add(new RelicEntry(2, "SHU", "Kitab & Ilmu Pengetahuan", "Scroll & Knowledge"));
        entries.add(new RelicEntry(3, "MAI", "Niaga & Kemakmuran", "Trade & Prosperity"));
        entries.add(new RelicEntry(4, "SHI", "Karya & Pengabdian", "Deed & Devotion"));
        entries.add(new RelicEntry(5, "YUN", "Awan & Ketenteraman", "Cloud & Serenity"));
        entries.add(new RelicEntry(6, "JING", "Ibu Kota & Kejayaan", "Capital & Glory"));
        entries.add(new RelicEntry(7, "XIA", "Kerendahan Hati", "Humility & Grounding"));
        entries.add(new RelicEntry(8, "SHANG", "Cita-Cita Luhur", "Aspiration & Ascent"));
        entries.add(new RelicEntry(9, "SHI (II)", "Generasi & Warisan", "Generation & Legacy"));
        entries.add(new RelicEntry(10, "ZHONG", "Keseimbangan Tengah", "Center & Balance"));
    }

    public static PusakaCompendium getInstance() {
        if (instance == null) {
            instance = new PusakaCompendium();
        }
        return instance;
    }

    public void recordClearedTiles(int[] clearedEncodedTileIds) {
        if (clearedEncodedTileIds == null) {
            return;
        }
        for (int encoded : clearedEncodedTileIds) {
            int baseId = SpecialTileType.extractBaseTileId(encoded);
            if (baseId > 0) {
                int idx = Math.floorMod(baseId - 1, entries.size());
                entries.get(idx).addCleared(1);
            }
        }
    }

    public List<RelicEntry> getEntries() {
        return entries;
    }

    public int getUnlockedCount() {
        int count = 0;
        for (RelicEntry e : entries) {
            if (e.isUnlocked()) {
                count++;
            }
        }
        return count;
    }
}