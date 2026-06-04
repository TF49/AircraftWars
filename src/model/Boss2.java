package model;

import data.Data;
import engine.GameSession;
import utils.Rect;

import java.awt.*;
import java.util.Random;

public class Boss2 extends Aircraft {

    double lastTime = 0;
    int freezeFrames = 0;
    int checkpoint;
    private int originalSpeed;

    public Boss2(int checkpoint) {
        super(Data.bossAircraftImages[1], Data.bossDeathImages[1], 2, 90,
                Data.WIDTH / 2 - Data.bossAircraftImages[1][0].getWidth(null) / 2, -200);
        this.checkpoint = checkpoint;
        maxHp = hp = 300 + checkpoint * 50;
        speed = 2;
        originalSpeed = 2;
        lastTime = -1;
    }

    public Boss2() {
        this(1);
    }

    @Override
    public void move() {
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }
        // 检查是否需要减速（通过GameSession获取玩家状态）
        if (GameSession.current != null && GameSession.current.player != null
                && GameSession.current.player.isSlowingEnemies()) {
            speed = originalSpeed / 2;
        } else {
            speed = originalSpeed;
        }
        Random random = new Random();
        boolean left = x < 0, right = x > Data.WIDTH - width, up = y < 0, down = y > Data.HEIGHT / 2 - height;
        if (left)
            deg = random.nextInt(20) - 9.5;
        if (right)
            deg = random.nextInt(20) - 9.5 + 180;
        if (up)
            deg = random.nextInt(40) - 19.5 + 90;
        if (down)
            deg = random.nextInt(40) - 19.5 + 270;
        super.move();
    }

    @Override
    public Bullet[] attack() {
        if (lifeTime - lastTime <= 3 || hp <= 10) {
            return new Bullet[] {};
        }
        lastTime = lifeTime;
        Random random = new Random();
        int rn = random.nextInt(100);
        if (rn < 20)
            return new Bullet[] {};

        int bx = Data.WIDTH / 2;
        int by = x + height;
        int mx = x + width / 2;
        int my = y + height / 2;

        if (rn % 3 == 0) {
            Bullet[] bs = new Bullet[60];
            for (int i = 0; i < 60; i++) {
                Point p = Rect.getPoint(mx, my, 20 * i, i / 10 + 3);
                bs[i] = new BossBullet1(20 * i, p.x, p.y, 0.5, i);
            }
            freezeFrames = Data.FPS;
            return bs;
        } else if (rn % 3 == 1) {
            Bullet[] bs = new Bullet[60];
            for (int i = 0; i < 10; i++) {
                bs[i * 6] = new BossBullet1(80, bx - 300, by + 30 - height * 2, 0, i * 10);
                bs[i * 6 + 1] = new BossBullet1(80, bx - 200, by - height * 2, 0, i * 10);
                bs[i * 6 + 2] = new BossBullet1(80, bx - 100, by + 30 - height * 2, 0, i * 10);
                bs[i * 6 + 3] = new BossBullet1(100, bx + 100, by - height * 2, 0, i * 10);
                bs[i * 6 + 4] = new BossBullet1(100, bx + 200, by + 30 - height * 2, 0, i * 10);
                bs[i * 6 + 5] = new BossBullet1(100, bx + 300, by - height * 2, 0, i * 10);
                bs[i * 6].speed = bs[i * 6 + 1].speed = bs[i * 6
                        + 2].speed = bs[i * 6 + 3].speed = bs[i * 6 + 4].speed = bs[i * 6 + 5].speed = 10;
            }
            return bs;
        } else {
            Bullet[] bs = new Bullet[360];
            for (int i = 0; i < 360; i++) {
                bs[i] = new BossBullet1(i, mx, my, 0, (i % 10) * 20);
            }
            freezeFrames = Data.FPS * 2;
            return bs;
        }
    }
}
