package main;

import data.Data;
import scenes.About;
import scenes.Game;
import scenes.Home;
import scenes.connector.Scenes;
import scenes.Recording;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JPanel implements MouseMotionListener{
    // 当前界面所使用的场景
    Scenes nowScenes = null;

    public Canvas(JFrame frame) {
        // 从主界面开始游戏
        switchScenes("Home");
        // 监听鼠标事件
        frame.addMouseListener(new OnMouseEvent());
        frame.addMouseMotionListener(this);
        // 键盘事件
        frame.addKeyListener(new OnKeyEvent());
        // 开始以 Data.FPS 的帧率刷新界面
        new UpdateUI().start();
    }

    public void switchScenes(String scenesName) {
        if (scenesName.equals("Home")) {
            // 切换到首页
            nowScenes = new Home();
        } else if (scenesName.equals("About")) {
            // 切换到关于作者
            nowScenes = new About();
        } else if (scenesName.equals("Recording")) {
            // 切换到排行榜
            nowScenes = new Recording();
        } else if (scenesName.equals("Site")) {
            // 切换到设置
            nowScenes = new Recording();
        } else if(scenesName.equals("Game")) {
            // 切换到战斗界面
            nowScenes = new Game();
        }
    }

    public void paint(Graphics g) {
        // 绘制当前场景
        nowScenes.draw(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_MOVED);
    }

    class OnMouseEvent extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_DOWN);
        }

        public void mouseReleased(MouseEvent e) {
            nowScenes.onMouse(e.getX(), e.getY(), Scenes.MOUSE_UP);
        }
    }

    class OnKeyEvent extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            nowScenes.onKeyDown(e.getKeyCode());
        }

        public void keyReleased(KeyEvent e) {
            nowScenes.onKeyUp(e.getKeyCode());
        }
    }

    class UpdateUI extends Thread {
        public void run() {
            int sleepTime = 1000 / Data.FPS;
            while (true) {
                try {
                    updateUI();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
