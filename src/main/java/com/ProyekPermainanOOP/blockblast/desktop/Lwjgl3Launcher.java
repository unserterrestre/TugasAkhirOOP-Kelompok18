package com.ProyekPermainanOOP.blockblast.desktop;

import com.ProyekPermainanOOP.blockblast.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Pusaka - Ilmu Kuno Menjadi Keseruan");
        config.setWindowedMode(576, 800);
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
        config.setHdpiMode(HdpiMode.Pixels);
        config.useVsync(true);
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        new Lwjgl3Application(new Main(), config);
    }
}