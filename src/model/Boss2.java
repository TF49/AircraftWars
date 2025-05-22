package model;

import data.Data;
import utils.Rect;

import java.awt.*;
import java.util.Random;

/**
 * 这个类是用来描述第二个BOSS的
 */

public class Boss2 extends Aircraft {

    double lastTime = 0;

    // 构造方法
    public Boss2() {
        super(Data.bossAircraftImages[1], Data.bossDeathImages[1], 2, 90, Data.WIDTH / 2 - Data.bossAircraftImages[1][0].getWidth(null) / 2, -200);

        hp = 300;

        speed = 2;

        lastTime = -1;
    }

    // BOSS 的移动
    public void move() {
        Random random = new Random();
        // BOSS会在屏幕上方反复横跳，这里我将采用固定的一定方式
        // 获取飞机在上下左右四个位置的情况，如果为true,表示它不应该继续下去
        boolean left = x < 0, right = x > Data.WIDTH - width, up = y < 0, down = y > Data.HEIGHT / 2 - height;
        // 根据飞机所在位置重新设定飞机应该移动的方向

        // 左边超出边界,则向右边走, 范围在 -9.5° ~ 9.5°
        if (left) deg = random.nextInt(20) - 9.5;
        // 右边超出边界,则向左边走, 范围在 171.5° ~ 189.5
        if (right) deg = random.nextInt(20) - 9.5 + 180;
        // 上边超出边界,则向下边走, 范围在 71.5° ~ 109.5°
        if (up) deg = random.nextInt(40) - 19.5 + 90;
        // 下边超出边界,则向上边走, 范围在 -281.5° ~ 259.5°
        if (down) deg = random.nextInt(40) - 19.5 + 270;

        super.move();
    }

    // BOSS 发射子弹
    public Bullet[] attack() {

        // 最低四秒发射一次子弹
        if (lifeTime - lastTime > 3 && hp > 10) {
            lastTime = lifeTime;
            int rn = new Random().nextInt(100);
            // 发射子弹时，有一定的几率不发射
            if (rn < 20) return new Bullet[]{};

            int bx = Data.WIDTH / 2;
            int by = x + height;
            int mx = x + width / 2, my = y + height / 2;

            if (rn % 3 == 0) {

                // 发射一圈螺旋子弹
                Bullet[] bs = new Bullet[60];
                for (int i = 0; i < 60; i++) {
                    Point p = Rect.getPoint(mx, my, 20 * i, i / 10 + 3);
                    bs[i] = new BossBullet1(20 * i, p.x, p.y, 0.5, i);
                }
                new Thread(() -> {
                    try {
                        speed = 0;
                        Thread.sleep(1000);
                        speed = 2;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                return bs;
            } else if (rn % 3 == 1) {
                // 发射散射子弹
                Bullet[] bs = new Bullet[60];

                for (int i = 0; i < 10; i++) {
                    bs[i * 6 + 0] = new BossBullet1(80, bx - 300, by + 30 - height * 2, 0, i * 10);
                    bs[i * 6 + 1] = new BossBullet1(80, bx - 200, by - height * 2, 0, i * 10);
                    bs[i * 6 + 2] = new BossBullet1(80, bx - 100, by + 30 - height * 2, 0, i * 10);

                    bs[i * 6 + 3] = new BossBullet1(100, bx + 100, by - height * 2, 0, i * 10);
                    bs[i * 6 + 4] = new BossBullet1(100, bx + 200, by + 30 - height * 2, 0, i * 10);
                    bs[i * 6 + 5] = new BossBullet1(100, bx + 300, by - height * 2, 0, i * 10);
                    bs[i * 6 + 0].speed = bs[i * 6 + 1].speed = bs[i * 6 + 2].speed = bs[i * 6 + 3].speed = bs[i * 6 + 4].speed = bs[i * 6 + 5].speed = 10;

                }
                return bs;
            } else if (rn % 3 == 2) {
                Bullet[] bs = new Bullet[360];
                for (int i = 0; i < 360; i++) {
                    bs[i] = new BossBullet1(i, x + width / 2, y + height / 2, 0, (i % 10) * 20);
                }
                new Thread(() -> {
                    try {
                        speed = 0;
                        Thread.sleep(2000);
                        speed = 2;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                return bs;
            }
        }

        return new Bullet[]{};
    }
}
