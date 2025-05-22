package model;

import data.Data;

import java.awt.*;
import java.util.Random;

public class Enemy extends Aircraft{
    public Enemy(Image[] aircraft, Image[] death, int speed, double deg, int x, int y, int hp) {
        super(aircraft, death, speed, deg, x, y);
        this.hp = hp;
        lifeTime = 4;
    }

    public Bullet[] attack() {
        if(lifeTime > 5 && hp > 20) {
            lifeTime = 0;

            int rn = new Random().nextInt(100);

            if(rn < 80) return new Bullet[]{};

            if(rn % 5 > 3)
                return new BossBullet2[] {
                        new BossBullet2(90, this.x - width / 2, y + height + 5, 0, 0)
                };
        }
        return new Bullet[]{};
    }
}
