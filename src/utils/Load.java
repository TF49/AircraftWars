package utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Load {

    public static Image image(String path) {
        BufferedImage img = null;
        URL url = Load.class.getResource("/resources/img/" + path);
        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    // 返回一段音乐
    public static Clip sound(String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Load.class.getResourceAsStream("/resources/sound/" + path + ".wav"));
            Clip sound = AudioSystem.getClip();
            sound.open(ais);
            return sound;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void playSound(String path) {
        new Thread(() -> Load.sound(path).start()).start();
    }
}
