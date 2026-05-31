package main;

import data.Data;
import scenes.*;
import scenes.connector.Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Canvas extends JPanel implements MouseMotionListener {

    private static final Map<String, Supplier<Scenes>> SCENES = new HashMap<>();

    static {
        SCENES.put("Home", Home::new);
        SCENES.put("About", About::new);
        SCENES.put("Recording", Recording::new);
        SCENES.put("Site", Site::new);
        SCENES.put("Game", Game::new);
        SCENES.put("GameOver", GameOver::new);
    }

    Scenes nowScenes = null;

    public Canvas(JFrame frame) {
        setDoubleBuffered(true);
        switchScenes("Home");
        frame.addMouseListener(new OnMouseEvent());
        frame.addMouseMotionListener(this);
        frame.addKeyListener(new OnKeyEvent());
        new UpdateUI().start();
    }

    public void switchScenes(String scenesName) {
        Supplier<Scenes> supplier = SCENES.get(scenesName);
        if (supplier != null) {
            nowScenes = supplier.get();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (nowScenes != null) {
            nowScenes.draw(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (nowScenes != null) {
            nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_MOVED);
        }
    }

    class OnMouseEvent extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (nowScenes != null) nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_DOWN);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (nowScenes != null) nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_UP);
        }
    }

    class OnKeyEvent extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (nowScenes != null) nowScenes.onKeyDown(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (nowScenes != null) nowScenes.onKeyUp(e.getKeyCode());
        }
    }

    class UpdateUI extends Thread {
        @Override
        public void run() {
            int sleepTime = 1000 / Data.FPS;
            while (true) {
                try {
                    repaint();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
