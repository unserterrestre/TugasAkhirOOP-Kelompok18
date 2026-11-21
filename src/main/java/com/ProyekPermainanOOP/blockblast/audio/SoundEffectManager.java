package com.ProyekPermainanOOP.blockblast.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffectManager {

    private static SoundEffectManager instance;
    private boolean enabled = true;

    private SoundEffectManager() {
    }

    public static SoundEffectManager getInstance() {
        if (instance == null) {
            instance = new SoundEffectManager();
        }
        return instance;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void playPlaceSound() {
        playTone(320.0, 55, 0.22);
    }

    public void playClearSound(int comboLevel) {
        double baseFreq = 440.0 * Math.pow(1.12, Math.min(8, Math.max(0, comboLevel - 1)));
        playTone(baseFreq, 130, 0.30);
    }

    public void playPowerUpSound() {
        playTone(660.0, 180, 0.35);
    }

    public void playLifeLostSound() {
        playTone(190.0, 210, 0.32);
    }

    public void playUndoSound() {
        playTone(390.0, 90, 0.24);
    }

    private void playTone(double frequency, int durationMs, double volume) {
        if (!enabled) {
            return;
        }
        Thread toneThread = new Thread(() -> {
            try {
                float sampleRate = 22050f;
                int numSamples = (int) ((durationMs / 1000.0) * sampleRate);
                byte[] buffer = new byte[numSamples * 2];

                for (int i = 0; i < numSamples; i++) {
                    double envelope = 1.0 - ((double) i / numSamples);
                    double angle = 2.0 * Math.PI * i * frequency / sampleRate;
                    short val = (short) (Math.sin(angle) * 32767.0 * volume * envelope);
                    buffer[2 * i] = (byte) (val & 0xFF);
                    buffer[2 * i + 1] = (byte) ((val >> 8) & 0xFF);
                }

                AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
                Clip clip = AudioSystem.getClip();
                clip.open(format, buffer, 0, buffer.length);
                clip.start();
            } catch (Exception ignored) {
            }
        });
        toneThread.setDaemon(true);
        toneThread.start();
    }
}