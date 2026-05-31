package utils;

import data.Data;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Load {

    public static Image image(String path) {
        BufferedImage img = null;
        URL url = Load.class.getResource("/resources/img/" + path);
        try {
            if (url != null) {
                img = ImageIO.read(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static Clip sound(String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    Load.class.getResourceAsStream("/resources/sound/" + path + ".wav"));
            Clip sound = AudioSystem.getClip();
            sound.open(ais);
            return sound;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void playSound(String path) {
        SoundManager.play(path);
    }

    public static Clip soundWithVolume(String path, int volumePercent) {
        Clip clip = sound(path);
        if (clip != null) {
            SoundManager.applyVolume(clip, volumePercent);
        }
        return clip;
    }
}
