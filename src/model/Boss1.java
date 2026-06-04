package model;

import data.Data;
import engine.GameSession;
import utils.Rect;

import java.awt.*;
import java.util.Random;

public class Boss1 extends Aircraft {

    double lastTime = 0;
    int freezeFrames = 0;
    private int originalSpeed;

    public Boss1(int checkpoint) {
        super(Data.bossAircraftImages[0], Data.bossDeathImages[0], 2, 90,
                Data.WIDTH / 2 - Data.bossAircraftImages[0][0].getWidth(null) / 2, -200);
        maxHp = hp = 200 + checkpoint * 30;
        speed = 3;
        originalSpeed = 3;
        lastTime = -1;
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
        boolean left = x < 0, right = x > Data.WIDTH - width, up = y < 0, down = y > Data.HEIGHT / 3 - height;
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
        if (lifeTime - lastTime <= 2.5 || hp <= 10) {
            return new Bullet[] {};
        }
        lastTime = lifeTime;
        Random random = new Random();
        int mx = x + width / 2;
        int my = y + height / 2;

        if (random.nextInt(100) < 50) {
            Bullet[] bs = new Bullet[5];
            for (int i = 0; i < 5; i++) {
                bs[i] = new BossBullet1(-60 + i * 30, mx, my + 20, 0, i * 3);
            }
            return bs;
        }

        freezeFrames = Data.FPS;
        Bullet[] bs = new Bullet[12];
        double baseDeg = Math.toDegrees(Math.atan2(Data.y - my, Data.x - mx));
        for (int i = 0; i < 12; i++) {
            Point p = Rect.getPoint(mx, my, baseDeg + i * 30 - 165, 20);
            bs[i] = new BossBullet1(baseDeg + i * 30 - 165, p.x, p.y, 2, i * 5);
        }
        return bs;
    }
}
