package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAssets {
    public static TextureRegion[] chineseCharTiles;
    public static BitmapFont uiFont;

    public static void load() {
        String[] fileNames = {
                "GNU_Unifont_-_U+4EAC.jpg", "GNU_Unifont_-_U+4E91.jpg", "GNU_Unifont_-_U+4E8B.jpg", "GNU_Unifont_-_U+4E70.jpg",
                "GNU_Unifont_-_U+4E66.jpg", "GNU_Unifont_-_U+4E4B.jpg", "GNU_Unifont_-_U+4E0A.jpg", "GNU_Unifont_-_U+4E0B.jpg", "GNU_Unifont_-_U+4E16.jpg", "GNU_Unifont_-_U+4E2D.jpg"
        };

        chineseCharTiles = new TextureRegion[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            Texture tex = new Texture(Gdx.files.internal(fileNames[i]));
            chineseCharTiles[i] = new TextureRegion(tex);
        }

        if (chineseCharTiles != null) {
            for (TextureRegion region : chineseCharTiles) {
                if (region != null && region.getTexture() != null) {
                    region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                }
            }
        }
    }

    public static TextureRegion getRandomChineseTile() {
        if (chineseCharTiles == null || chineseCharTiles.length == 0) {
            return null;
        }
        int randomIndex = (int) (Math.random() * chineseCharTiles.length);
        return chineseCharTiles[randomIndex];
    }

    public static void dispose() {
        if (chineseCharTiles != null) {
            for (TextureRegion region : chineseCharTiles) {
                if (region.getTexture() != null) {
                    region.getTexture().dispose();
                }
            }
        }
    }
}