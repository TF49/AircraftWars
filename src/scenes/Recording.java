package scenes;

import data.Data;
import scenes.connector.Scenes;
import service.ConfigService;
import service.ScoreService;
import utils.Load;
import utils.Rect;

import java.awt.*;
import java.util.List;

public class Recording implements Scenes {

    Image background;
    Image[] buttonOk;
    int buttonOkStruts = 0;

    public Recording() {
        buttonOk = new Image[3];
        background = Load.image("历史排行-背景.png");
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
        if (Rect.isInternal(x, y, 188, 600 + Data.TITLE_BOX_HEIGHT, 132, 42)) {
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
        g.drawString("历史排行 TOP 10", 150, 120);

        List<ScoreService.ScoreEntry> top = ScoreService.getTopN(10);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 14f));
        int y = 160;
        if (top.isEmpty()) {
            g.drawString("暂无记录", 180, y);
        } else {
            for (int i = 0; i < top.size(); i++) {
                ScoreService.ScoreEntry entry = top.get(i);
                g.drawString(String.format("%2d. %-8s %6d  %s", i + 1, entry.name, entry.score, entry.date), 70, y);
                y += 28;
            }
        }

        g.drawImage(buttonOk[buttonOkStruts], 188, 600, null);
    }
}
