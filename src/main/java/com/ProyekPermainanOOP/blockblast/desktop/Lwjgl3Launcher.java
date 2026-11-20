package com.ProyekPermainanOOP.blockblast.desktop;

import com.ProyekPermainanOOP.blockblast.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Pusaka - Mahjong");
        config.setWindowedMode(720, 1000);
        config.useVsync(true);
        new Lwjgl3Application(new Main(), config);
    }
}
