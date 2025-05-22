package scenes;

import data.Data;
import scenes.connector.Scenes;
import utils.Load;
import utils.Rect;

import java.awt.*;

public class About implements Scenes {
    // 背景图片
    Image background;
    // 确定按钮
    Image[] buttonOk;
    // 按钮状态
    int buttonOkStruts = 0;

    public About() {
        buttonOk = new Image[3];

        background = Load.image("关于作者-背景.png");
        buttonOk[0] = Load.image("确定.png");
        buttonOk[1] = Load.image("确定-鼠标移上.png");
        buttonOk[2] = Load.image("确定-鼠标按下.png");
    }

    public void onKeyDown(int keyCode) {

    }

    public void onKeyUp(int keyCode) {

    }

    public void onMouse(int x, int y, int struts) {
        buttonOkStruts = 0;

        if(Rect.isInternal(x, y, 188, 600 + Data.TITLE_BOX_HEIGHT, 132, 42)) {
            buttonOkStruts = struts == Scenes.MOUSE_MOVED ? 1 : struts == Scenes.MOUSE_DOWN ? 2 : 0;
            if(struts == Scenes.MOUSE_UP) Data.canvas.switchScenes("Home");
        }
    }

    public void draw(Graphics g) {
        // 绘制背景
        Data.background.show(g);
        // 绘制关于作者
        g.drawImage(background, 48, 60, null);
        // 绘制按钮
        g.drawImage(buttonOk[buttonOkStruts], 188, 600, null);
    }
}
