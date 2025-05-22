package model;

import data.Data;
import utils.Rect;

import java.awt.*;
import java.util.Random;

public class Buff extends Bullet{

    public static final int BUFF1 = 0, BUFF2 = 1;

    Point m;

    public Buff( int buffType, int x, int y) {
        super(Data.buff[buffType], new Random().nextInt(360), 2, x, y);

        struts = buffType;

        m = Rect.getPoint(x, y, deg, speed);
        m.x = x - m.x;
        m.y = y - m.y;
    }

    public void move() {
        x += m.x;
        y += m.y;

        if(x < 3 || x > Data.WIDTH - width - 3)
            m.x *= -1;
        if(y < 3 || y > Data.HEIGHT - height - 3)
            m.y *= -1;
    }

    public boolean isRemove() {
        return x < 0 || y < 0 || x > Data.WIDTH || y > Data.HEIGHT;
    }

    public void draw(Graphics g) {
        g.drawImage(bullet[0], x, y, null);
    }
}
