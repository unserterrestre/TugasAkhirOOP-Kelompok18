package com.ProyekPermainanOOP.blockblast.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.utils.Disposable;

public class BackgroundMusicPlayer implements Disposable {
    private static final int SAMPLE_RATE = 22050;
    private static final double[] PENTATONIC_FREQS = {
            261.63, 293.66, 329.63, 392.00, 440.00, 523.25
    };
    private static final int[] MELODY_STEPS = {
            0, 2, 4, 5, 4, 2, 3, 1,
            0, 3, 4, 2, 1, 2, 0, -1
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
            int samplesPerBeat = (int) (SAMPLE_RATE * 0.42);
            float[] buffer = new float[samplesPerBeat];

            while (running && audioDevice != null) {
                int noteIdx = MELODY_STEPS[stepIndex % MELODY_STEPS.length];
                double freq = noteIdx >= 0 ? PENTATONIC_FREQS[noteIdx % PENTATONIC_FREQS.length] : 0.0;
                double droneFreq = (stepIndex / 4) % 2 == 0 ? 130.81 : 146.83;

                for (int i = 0; i < samplesPerBeat; i++) {
                    if (!running) {
                        break;
                    }
                    if (muted) {
                        buffer[i] = 0f;
                        continue;
                    }
                    double t = (double) i / SAMPLE_RATE;
                    double env = Math.sin(Math.min(1.0, t / 0.04) * (Math.PI / 2.0))
                            * Math.exp(-t * 3.4);
                    double lead = freq > 0.0
                            ? (0.75 * Math.sin(2.0 * Math.PI * freq * t) + 0.25 * Math.sin(4.0 * Math.PI * freq * t))
                            : 0.0;
                    double drone = 0.22 * Math.sin(2.0 * Math.PI * droneFreq * t);
                    buffer[i] = (float) Math.max(-0.35, Math.min(0.35, (lead * env * 0.16) + drone * 0.12));
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