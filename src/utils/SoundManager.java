package utils;

import data.Data;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static final Map<String, Integer> playingCount = new HashMap<>();
    private static final Map<String, Integer> maxConcurrent = new HashMap<>();

    static {
        maxConcurrent.put("击中", 3);
        maxConcurrent.put("死亡", 2);
        maxConcurrent.put("失败", 1);
    }

    public static void play(String path) {
        int max = maxConcurrent.getOrDefault(path, 2);
        int current = playingCount.getOrDefault(path, 0);
        if (current >= max) return;

        playingCount.put(path, current + 1);
        new Thread(() -> {
            try {
                Clip clip = Load.sound(path);
                if (clip == null) return;
                applyVolume(clip, Data.sfxVolume);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        playingCount.put(path, Math.max(0, playingCount.getOrDefault(path, 1) - 1));
                    }
                });
            } catch (Exception e) {
                playingCount.put(path, Math.max(0, playingCount.getOrDefault(path, 1) - 1));
            }
        }).start();
    }

    public static void applyVolume(Clip clip, int volumePercent) {
        if (clip == null) return;
        try {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = control.getMinimum();
            float max = control.getMaximum();
            float gain = min + (max - min) * volumePercent / 100f;
            control.setValue(gain);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
