package service;

import data.Data;

import java.io.*;
import java.util.Properties;

public class ConfigService {

    private static final String CONFIG_FILE = "config.properties";

    public static void load() {
        Properties props = new Properties();
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            save();
            return;
        }
        try (InputStream in = new FileInputStream(file)) {
            props.load(in);
            Data.difficulty = Integer.parseInt(props.getProperty("difficulty", "1"));
            Data.bgmVolume = Integer.parseInt(props.getProperty("bgmVolume", "80"));
            Data.sfxVolume = Integer.parseInt(props.getProperty("sfxVolume", "80"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        Properties props = new Properties();
        props.setProperty("difficulty", String.valueOf(Data.difficulty));
        props.setProperty("bgmVolume", String.valueOf(Data.bgmVolume));
        props.setProperty("sfxVolume", String.valueOf(Data.sfxVolume));
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "AircraftWars Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String difficultyLabel() {
        switch (Data.difficulty) {
            case Data.DIFFICULTY_EASY:
                return "简单";
            case Data.DIFFICULTY_HARD:
                return "困难";
            default:
                return "普通";
        }
    }

    public static void cycleDifficulty() {
        Data.difficulty = (Data.difficulty + 1) % 3;
        save();
    }

    public static void adjustBgmVolume(int delta) {
        Data.bgmVolume = clamp(Data.bgmVolume + delta);
        save();
    }

    public static void adjustSfxVolume(int delta) {
        Data.sfxVolume = clamp(Data.sfxVolume + delta);
        save();
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}
