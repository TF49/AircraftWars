package model;

import data.Data;

import java.awt.*;

/**
 * 这种子弹遇到墙壁会反弹
 */

public class BossBullet2 extends Bullet {
    // 这个gDeg是每次改变的角度
    double gDeg;
    // 自动宽度
    int width;
    // 表示需要延迟 delay 帧之后才会显示
    int delay;

    public BossBullet2(double deg, int x, int y, double gDeg, int delay) {
        super(Data.enemyBulletImages[0], deg, 6, x, y);
        struts = 1;
        this.gDeg = gDeg;
        this.delay = delay;

        width = Data.enemyBulletImages[0][0].getWidth(null);
        buffetIndex = 0;
    }

    public void move() {
        // 延迟移动
        if (delay > 0 || buffetIndex > 0) return;

        // 重写此方法，是为了实现特殊子弹，这个子弹的路径会随着移动二有规律的改变
        deg += gDeg;

        if(delay > - (Data.FPS * 5)) {
            boolean top = y < 0, left = x < 0, right = x > Data.WIDTH - width;

            if (top) {
                deg -= 90;
                super.move();
            }
            else if (left) {
                deg -= 90;
                super.move();
            }
            else if (right) {
                deg -= 180;
                super.move();
            }
        }

        super.move();
    }

    public void draw(Graphics g) {
        // 延迟显示
        if ((delay -= 1) == 0 || buffetIndex > 0) return;

        // 绘制自己
        g.drawImage(bullet[buffetIndex], x, y, null);
    }

    public boolean isRemove() {
        return buffetIndex != 0 || x < -width || y < -height || x > Data.WIDTH + width || y > Data.HEIGHT + height || delay < -(Data.FPS * 10);
    }
}
