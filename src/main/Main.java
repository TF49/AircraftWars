package main;

import data.Data;
import utils.Load;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // 创建窗口
        JFrame frame = new JFrame("飞机大战");
        // 添加 JPanel
        Data.canvas = new Canvas(frame);
        frame.setContentPane(Data.canvas);
        // 初始化 Data
        Data.init();
        // 设置图标
        frame.setIconImage(Load.image("ICON.png"));
        // 设置窗口可见
        frame.setVisible(true);
        // 获取标题栏的高度和宽度
        Data.TITLE_BOX_HEIGHT = frame.getInsets().top;
        // 设置大小
        frame.setSize(Data.WIDTH, Data.HEIGHT + Data.TITLE_BOX_HEIGHT);
        // 窗口大小固定
        frame.setResizable(false);
        // 窗口居中显示
        frame.setLocationRelativeTo(frame.getOwner());
        // 窗口关闭时结束程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 播放背景音乐
        Load.sound("background").loop(Clip.LOOP_CONTINUOUSLY);
    }
}
