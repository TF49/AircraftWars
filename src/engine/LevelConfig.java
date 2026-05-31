package engine;

import data.Data;

public class LevelConfig {

    public static int getWaveInterval(int checkpoint) {
        int interval = 100 - (checkpoint - 1) * 8;
        return Math.max(50, (int) (interval * getDifficultySpawnFactor()));
    }

    public static double getHpMultiplier(int checkpoint) {
        return (1.0 + (checkpoint - 1) * 0.2) * getDifficultyHpFactor();
    }

    public static int[] computeHp(int checkpoint, int fps) {
        double mul = getHpMultiplier(checkpoint);
        int base = fps / 2000;
        return new int[]{
                (int) ((base + 1 * checkpoint) * mul),
                (int) ((base + 2 * checkpoint) * mul),
                (int) ((base + 5 * checkpoint) * mul),
                (int) ((base + 8 * checkpoint) * mul),
                (int) ((base + 11 * checkpoint) * mul)
        };
    }

    public static double getDifficultyHpFactor() {
        switch (Data.difficulty) {
            case Data.DIFFICULTY_EASY:
                return 0.8;
            case Data.DIFFICULTY_HARD:
                return 1.3;
            default:
                return 1.0;
        }
    }

    public static double getDifficultySpawnFactor() {
        switch (Data.difficulty) {
            case Data.DIFFICULTY_EASY:
                return 1.2;
            case Data.DIFFICULTY_HARD:
                return 0.85;
            default:
                return 1.0;
        }
    }
}
