package scenes;

import data.Data;
import scenes.connector.Scenes;
import service.ConfigService;
import utils.Load;
import utils.Rect;

import java.awt.*;

public class Site implements Scenes {

    Image background;
    Image[] buttonOk;
    int buttonOkStruts = 0;

    public Site() {
        buttonOk = new Image[3];
        background = Load.image("游戏设置-背景.png");
        buttonOk[0] = Load.image("确定.png");
        buttonOk[1] = Load.image("确定-鼠标移上.png");
        buttonOk[2] = Load.image("确定-鼠标按下.png");
    }

    @Override
    public void onKeyDown(int keyCode) {
    }

    @Override
    public void onKeyUp(int keyCode) {
    }

    @Override
    public void onMouse(int x, int y, int struts) {
        buttonOkStruts = 0;
        int titleOffset = Data.TITLE_BOX_HEIGHT;

        if (Rect.isInternal(x, y, 120, 200 + titleOffset, 280, 40)) {
            if (struts == Scenes.MOUSE_UP) ConfigService.cycleDifficulty();
        } else if (Rect.isInternal(x, y, 80, 280 + titleOffset, 40, 40)) {
            if (struts == Scenes.MOUSE_UP) ConfigService.adjustBgmVolume(-10);
        } else if (Rect.isInternal(x, y, 400, 280 + titleOffset, 40, 40)) {
            if (struts == Scenes.MOUSE_UP) ConfigService.adjustBgmVolume(10);
        } else if (Rect.isInternal(x, y, 80, 360 + titleOffset, 40, 40)) {
            if (struts == Scenes.MOUSE_UP) ConfigService.adjustSfxVolume(-10);
        } else if (Rect.isInternal(x, y, 400, 360 + titleOffset, 40, 40)) {
            if (struts == Scenes.MOUSE_UP) ConfigService.adjustSfxVolume(10);
        } else if (Rect.isInternal(x, y, 188, 600 + titleOffset, 132, 42)) {
            buttonOkStruts = struts == Scenes.MOUSE_MOVED ? 1 : struts == Scenes.MOUSE_DOWN ? 2 : 0;
            if (struts == Scenes.MOUSE_UP) Data.canvas.switchScenes("Home");
        }
    }

    @Override
    public void draw(Graphics g) {
        Data.background.show(g);
        if (background != null) g.drawImage(background, 48, 60, null);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));
        g.drawString("游戏设置", 200, 130);

        g.setFont(g.getFont().deriveFont(Font.PLAIN, 16f));
        g.drawString("难度: " + ConfigService.difficultyLabel() + "  (点击切换)", 120, 220);
        g.drawString("背景音乐: " + Data.bgmVolume + "%  [-]  [+]", 120, 300);
        g.drawString("音效音量: " + Data.sfxVolume + "%  [-]  [+]", 120, 380);
        g.drawString("操作: WASD移动  P暂停  Space炸弹", 80, 460);

        g.drawImage(buttonOk[buttonOkStruts], 188, 600, null);
    }
}
