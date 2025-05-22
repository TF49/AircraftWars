package widget;

import data.Data;
import utils.Load;
import widget.connector.Animation;

import java.awt.*;

/**
 * 一个可以滚动的动画背景
 */

public class ScrollBackground implements Animation {

    // 背景图片
    Image[] background = null;
    // 当前 X轴 位置
    public int x = 0, speed = 1;

    public ScrollBackground(Image image) {
        // 因为游戏只有两个关卡，所以只准备了俩个背景图片
        background = new Image[]{Load.image("background-1.jpg"), Load.image("background-2.jpg")};
    }

    public void show(Graphics g) {
        // 绘制第一张图片
        g.drawImage(background[0], 0, x, null);
        // 再第一张图片上方再次绘制
        g.drawImage(background[0], 0, x - Data.HEIGHT, null);
        // 更新图片在 X轴 的位置
        x = x >= Data.HEIGHT ? 0 : x + speed;
        // 查看速度是不是为1,如果不是,说明现在正在等待切换背景,此时加速
        speed = speed == 1 ? 1 : speed % 70 + 1;
        // speed 的加速效果只持续一回合
        if(x == 0) speed = 1;
    }

    public boolean isEnd() {
        return false;
    }

    public void setAnimation(Object animation) {
        // 切换背景
        Image img = background[0];
        background[0] = background[1];
        background[1] = img;
    }
}
