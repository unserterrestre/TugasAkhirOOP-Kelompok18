package com.ProyekPermainanOOP.blockblast.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.io.File;

public final class VectorFontFactory {

    public static final float HUD_BASE_SCALE = 0.50f;
    public static final float BIG_BASE_SCALE = 0.34f;

    private static final String EXTRA_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789" +
                    ".,:;!?()[]{}+-*/=%&@#$'\"<>_|\\~^` " +
                    "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞß" +
                    "àáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ" +
                    "ĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģ" +
                    "ĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁł" +
                    "ŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠš" +
                    "ŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽž" +
                    "ƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặ" +
                    "ẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộ" +
                    "ỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ";

    private VectorFontFactory() {
    }

    public static BitmapFont createHudFont() {
        BitmapFont font = generateVectorFont(32, false);
        font.getData().setScale(HUD_BASE_SCALE);
        return font;
    }

    public static BitmapFont createBigFont() {
        BitmapFont font = generateVectorFont(52, true);
        font.getData().setScale(BIG_BASE_SCALE);
        return font;
    }

    private static BitmapFont generateVectorFont(int pixelSize, boolean bold) {
        FileHandle fontFile = resolveFontFile(bold);
        if (fontFile != null && fontFile.exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            try {
                FreeTypeFontParameter param = new FreeTypeFontParameter();
                param.size = pixelSize;
                param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + EXTRA_CHARS;
                param.minFilter = Texture.TextureFilter.Linear;
                param.magFilter = Texture.TextureFilter.Linear;
                param.genMipMaps = true;
                param.incremental = true;

                BitmapFont font = generator.generateFont(param);
                font.setUseIntegerPositions(false);
                return font;
            } finally {
                generator.dispose();
            }
        }

        BitmapFont fallback = new BitmapFont();
        fallback.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fallback.setUseIntegerPositions(false);
        return fallback;
    }

    private static FileHandle resolveFontFile(boolean bold) {
        String[] assetCandidates = bold
                ? new String[]{"fonts/game_font_bold.ttf", "fonts/game_font.ttf", "font.ttf"}
                : new String[]{"fonts/game_font.ttf", "font.ttf"};

        for (String path : assetCandidates) {
            FileHandle internal = Gdx.files.internal(path);
            if (internal.exists()) {
                return internal;
            }
        }

        String[] systemCandidates = bold
                ? new String[]{
                "C:/Windows/Fonts/segoeuib.ttf",
                "C:/Windows/Fonts/arialbd.ttf",
                "C:/Windows/Fonts/calibrib.ttf",
                "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",
                "/usr/share/fonts/truetype/liberation/LiberationSans-Bold.ttf",
                "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
        }
                : new String[]{
                "C:/Windows/Fonts/segoeui.ttf",
                "C:/Windows/Fonts/arial.ttf",
                "C:/Windows/Fonts/calibri.ttf",
                "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
                "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf",
                "/System/Library/Fonts/Supplemental/Arial.ttf"
        };

        for (String sysPath : systemCandidates) {
            File file = new File(sysPath);
            if (file.exists() && file.isFile()) {
                return Gdx.files.absolute(file.getAbsolutePath());
            }
        }
        return null;
    }
}