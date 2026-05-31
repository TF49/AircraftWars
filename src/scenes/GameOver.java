package scenes;

import data.Data;
import scenes.connector.Scenes;
import service.ScoreService;
import utils.Load;
import utils.Rect;

import java.awt.*;
import java.util.List;

public class GameOver implements Scenes {

    public static int lastScore = 0;
    public static int lastCheckpoint = 1;
    public static int lastMaxCombo = 0;

    Image background;
    Image[] buttonRetry, buttonSave, buttonHome;
    int retryStatus, saveStatus, homeStatus;

    public GameOver() {
        background = Load.image("历史排行-背景.png");
        buttonRetry = loadButton("开始游戏");
        buttonSave = loadButton("历史排行");
        buttonHome = loadButton("结束游戏");
    }

    Image[] loadButton(String prefix) {
        return new Image[]{
                Load.image(prefix + ".png"),
                Load.image(prefix + "-鼠标移上.png"),
                Load.image(prefix + "-鼠标按下.png")
        };
    }

    @Override
    public void onKeyDown(int keyCode) {
    }

    @Override
    public void onKeyUp(int keyCode) {
    }

    @Override
    public void onMouse(int x, int y, int struts) {
        retryStatus = saveStatus = homeStatus = 0;
        int btn = struts == Scenes.MOUSE_MOVED ? 1 : struts == Scenes.MOUSE_DOWN ? 2 : 0;

        if (Rect.isInternal(x, y, 127, 320 + Data.TITLE_BOX_HEIGHT, 265, 58)) {
            retryStatus = btn;
            if (struts == Scenes.MOUSE_UP) Data.canvas.switchScenes("Game");
        } else if (Rect.isInternal(x, y, 127, 400 + Data.TITLE_BOX_HEIGHT, 265, 58)) {
            saveStatus = btn;
            if (struts == Scenes.MOUSE_UP) {
                ScoreService.addScore("Player", lastScore);
                Data.canvas.switchScenes("Recording");
            }
        } else if (Rect.isInternal(x, y, 127, 480 + Data.TITLE_BOX_HEIGHT, 265, 58)) {
            homeStatus = btn;
            if (struts == Scenes.MOUSE_UP) Data.canvas.switchScenes("Home");
        }
    }

    @Override
    public void draw(Graphics g) {
        Data.background.show(g);
        if (background != null) g.drawImage(background, 48, 60, null);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 22f));
        g.drawString("游戏结束", 200, 140);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 16f));
        g.drawString("最终分数: " + lastScore, 160, 180);
        g.drawString("到达关卡: " + lastCheckpoint, 160, 205);
        g.drawString("最高 Combo: x" + lastMaxCombo, 160, 230);

        g.drawImage(buttonRetry[retryStatus], 120, 320, null);
        g.drawImage(buttonSave[saveStatus], 120, 400, null);
        g.drawImage(buttonHome[homeStatus], 120, 480, null);
    }
}
