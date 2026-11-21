package com.ProyekPermainanOOP.blockblast.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.utils.Disposable;

public class BackgroundMusicPlayer implements Disposable {
    private static final int SAMPLE_RATE = 22050;
    private static final double[] SCALE_FREQS = {
            261.63, 293.66, 329.63, 392.00, 440.00, 523.25, 587.33, 659.25
    };
    private static final int[][] PHRASES = {
            {0, 2, 4, 7, 5, 4, 2, 4},
            {5, 7, 5, 4, 2, 0, 2, 4},
            {4, 5, 7, 5, 3, 4, 2, 0},
            {2, 4, 5, 7, 4, 2, 1, 0}
    };
    private static final double[] BASS_FREQS = {
            130.81, 146.83, 196.00, 164.81
    };

    private AudioDevice audioDevice;
    private volatile boolean muted = false;
    private volatile boolean running = true;
    private Thread musicThread;

    public BackgroundMusicPlayer() {
        try {
            this.audioDevice = Gdx.audio.newAudioDevice(SAMPLE_RATE, true);
            startLoop();
        } catch (Exception ignored) {
            this.audioDevice = null;
        }
    }

    private void startLoop() {
        if (audioDevice == null) {
            return;
        }
        musicThread = new Thread(() -> {
            int stepIndex = 0;
            int samplesPerBeat = (int) (SAMPLE_RATE * 0.28);
            float[] buffer = new float[samplesPerBeat];

            while (running && audioDevice != null) {
                int phraseIdx = (stepIndex / 8) % PHRASES.length;
                int beatInPhrase = stepIndex % 8;
                int noteIdx = PHRASES[phraseIdx][beatInPhrase];
                double leadFreq = SCALE_FREQS[noteIdx % SCALE_FREQS.length];
                double arpFreq = SCALE_FREQS[(noteIdx + 2) % SCALE_FREQS.length];
                double bassFreq = BASS_FREQS[phraseIdx % BASS_FREQS.length];
                boolean pulseAccent = (stepIndex % 2 == 0) || (stepIndex % 4 == 3);

                for (int i = 0; i < samplesPerBeat; i++) {
                    if (!running) {
                        break;
                    }
                    if (muted) {
                        buffer[i] = 0f;
                        continue;
                    }
                    double t = (double) i / SAMPLE_RATE;
                    double attack = Math.min(1.0, t / 0.015);
                    double decay = Math.exp(-t * 5.2);
                    double env = attack * decay;

                    double lead = 0.70 * Math.sin(2.0 * Math.PI * leadFreq * t)
                            + 0.30 * Math.sin(4.0 * Math.PI * leadFreq * t);
                    double arp = (i > samplesPerBeat / 2)
                            ? 0.45 * Math.sin(2.0 * Math.PI * arpFreq * t)
                            : 0.0;
                    double bass = Math.sin(2.0 * Math.PI * bassFreq * t) * Math.exp(-t * 2.6);
                    double perc = pulseAccent
                            ? Math.sin(2.0 * Math.PI * (110.0 * Math.exp(-t * 28.0) + 55.0) * t) * Math.exp(-t * 18.0)
                            : 0.0;

                    double mixed = (lead * env * 0.16) + (arp * env * 0.09) + (bass * 0.13) + (perc * 0.11);
                    buffer[i] = (float) Math.max(-0.42, Math.min(0.42, mixed));
                }

                if (running && audioDevice != null) {
                    try {
                        audioDevice.writeSamples(buffer, 0, buffer.length);
                    } catch (Exception ignored) {
                        break;
                    }
                }
                stepIndex++;
            }
        }, "Pusaka-BgMusic");
        musicThread.setDaemon(true);
        musicThread.start();
    }

    public void toggleMute() {
        muted = !muted;
    }

    public boolean isMuted() {
        return muted;
    }

    @Override
    public void dispose() {
        running = false;
        if (musicThread != null) {
            musicThread.interrupt();
        }
        if (audioDevice != null) {
            try {
                audioDevice.dispose();
            } catch (Exception ignored) {
            }
            audioDevice = null;
        }
    }
}