package model;

import data.Data;
import utils.Rect;

import java.awt.*;

public class Aircraft {
    // 碰撞检测时的检测范围
    public Point upperLeft, lowerRight;
    // 飞机的坐标, 大小
    public int x, y, width, height;
    // 飞机每次移动的距离和角度
    public int speed;
    double deg;
    // 飞机的血量，玩家操控的飞机血量为1，碰到就死弱鸡. 飞机的伤害，这个伤害属性在飞机发生碰撞时对敌方造成伤害, chp默认为2， 且后续不打算修改，毕竟撞机不光荣
    public int hp = 10, chp = 20;
    // 飞机的buff， 两中buff， 2个子弹发射和三个子弹发射，当同时拥有两种buff时，这个buff将叠加，同时释放5个子弹, 这个属性表示的是buff存在剩余时间，时间<0
    // 时，相应 buff 消失, 因为帧率是固定的，且每帧都会绘制自身，因此，tBuff1时间的减少不采用计时器，直接在draw里面减少即可
    // 他们的单位是秒
    double tBuff1 = -0.01, tBuff2 = -0.01;
    // 飞机的图像,死亡图像
    Image[] aircraft, death;
    // 图像索引, imgIndex < aircraft.length时，正常绘制，当大于aircraft.length时，绘制death,当大于aircraft.length+绘制death.length时，表示飞机死亡，因为飞机已经绘制完成了死亡动画
    public int imgIndex = 0;
    // 飞机切换的间隔
    public final int fps = 5;
    // 飞机存活的时间
    double lifeTime = 0;

    // 飞机的图像,死亡图像, 玩家的移动完全有场景控制，所以增加一个构造方法
    public Aircraft(Image[] aircraft, Image[] death, int speed, double deg, int x, int y) {
        this.aircraft = aircraft;
        this.death = death;
        this.speed = speed;
        this.deg = deg;
        this.x = x;
        this.y = y;
        width = aircraft[0].getWidth(null) + 6;
        height = aircraft[0].getHeight(null);

        upperLeft = new Point(5, 5);
        lowerRight = new Point(width - 5, height - 5);
    }

    public void move() {
        Point p = Rect.getPoint(x, y, deg, speed);
        x = p.x;
        y = p.y;
    }

    // 这个方法用来实现飞机生成后重叠的问题， 目测不会使用这个方法， 因为飞机重叠就重叠吧
    public void move(double deg, int speed) {
        Point p = Rect.getPoint(x, y, deg, speed);
        x = p.x;
        y = p.y;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /* 飞机的子弹我放在场景中统一处理，这样会比较方便，如果有必要，可以放在飞机的类中处理，本游戏明显没必要这样做 */
    // 此处发射飞机的子弹, 发射自动是有如下情况，没有buff时的正常子弹，有一个buff时的增强子弹，两个buff同时存在的叠加超级强化子弹， 本游戏正常子弹一种，增强子弹2种，强化叠加强化子弹1种
    // 超级强化就是牛
    // 敌人飞机会重写这个方法，敌机太强大没法大，而且我方飞机还是一只脆皮机
    public Bullet[] attack() {
        // 获取飞机的正中心
        int zx = x + (width / 2 - Data.playerBulletImage[0].getWidth(null) / 2) - 4, zy = y - height / 2;
        // 现在确定应该发射何种类型子弹
        // 将子弹生成后，以数组的方式直接返回，场景里面会将发射的子弹添加到相应的队列中
        if (tBuff1 > 0 && tBuff2 > 0) {
            // 超强化子弹等待发射
            return new Bullet[]{
                    new Bullet(Data.playerBulletImage, 286, 18, zx - 60, zy + 40),
                    new Bullet(Data.playerBulletImage, 278, 18, zx - 30, zy + 10),

                    new Bullet(Data.playerBulletImage, 262, 18, zx + 30, zy + 10),
                    new Bullet(Data.playerBulletImage, 254, 18, zx + 60, zy + 40),

                    new Bullet(Data.playerBulletImage, 270, 18, zx, zy)
            };
        } else if (tBuff1 > 0) {
            // 发射 tBuff1 状态下的子弹
            return new Bullet[]{
                    new Bullet(Data.playerBulletImage, -90, 17, zx - 20, zy),
                    new Bullet(Data.playerBulletImage, -90, 17, zx + 20, zy)
            };
        } else if (tBuff2 > 0) {
            // 发射 tBuff2 状态下的子弹
            return new Bullet[]{
                    new Bullet(Data.playerBulletImage, -100, 16, zx-10, zy),
                    new Bullet(Data.playerBulletImage, -90, 16, zx, zy),
                    new Bullet(Data.playerBulletImage, -80, 16, zx+10, zy)
            };
        } else {
            // 普通刮痧子弹等待发射
            return new Bullet[]{
                    new Bullet(Data.playerBulletImage, -90, 13, zx, zy)
            };
        }
    }

    // 吃buff， 也就是增加buff的时间， 没有buff的时候，他的时间是-0.01, 所有增加buff时，直接setBuff(0, 16)
    // 当没用buff时，上面的语句相当于设置增加一个buff，类型为buff2， 时间为16秒,这其中有0.01秒的损失，忽略不计
    // 如果buff存在，则给他添加16秒的时间
    public void setBuff(double buff1, double buff2) {
        // buff最大的时间为30秒
        if ((tBuff1 += buff1) > 30) {
            tBuff1 = 30;
        }
        if ((tBuff2 += buff2) > 30) {
            tBuff2 = 30;
        }
    }

    // 获取这个飞机的碰撞区, 返回的是一个包含矩形左上角坐标和高宽的Point数组
    public Point[] getCollisionRect() {
        return new Point[]{
                new Point(x + upperLeft.x, y + upperLeft.y),
                new Point(lowerRight.x - upperLeft.x, lowerRight.y - upperLeft.y)
        };
    }

    // 执行此方法后，接下来会播放死亡动画，播放完毕后，调用isRemove()会返回true
    public void kill() {
        hp = -10;
        imgIndex = aircraft.length * fps + 1;
    }

    // 判断是否应该移除这个对象,如果飞机被击杀，或者飞机飞出屏幕外时，这个方法应该返回true, 敌方飞机的生成实在屏幕上方，所以不判断屏幕上方的飞机,况且敌方飞机不会向上飞,至少不会垂直向上
    // 这里更改为， 移动到双倍的屏幕外才会移除， 即在 (-WIDTH, -HEIGHT, WIDTH*2, HEIGHT*2) 的范围外时，会被判定离开屏幕
    public boolean isRemove() {
        // 此处有三种情况会返回 true
        return ((hp <= 0) && (imgIndex >= (aircraft.length + death.length) * fps)) || (x > Data.WIDTH * 2 || x < -width + -Data.WIDTH || y > Data.HEIGHT * 2 || y < -height + -Data.HEIGHT);
    }

    public void draw(Graphics g) {
        // 这个fps表示每隔多少帧更新一下图片，因为如果没一帧都更新，在帧率为50的情况下，每秒切换50次，会很鬼畜
        if (imgIndex >= (aircraft.length + death.length) * fps) return;
        // 更新 imgIndex
        imgIndex++;
        // 更新 buff时间, 每次减去 Data.FPS / 1000 秒，在FPS为50的默认情况下，每次减少0.02秒, double 类型尽量不使用 ==
        // 计算每帧所用的时间 与 一秒 之间的时间比
        double time = (1000.0 / Data.FPS) / 1000;
        // 很棒，飞机又存活了 0.02秒
        lifeTime += time;
        // Buff的时间单位是秒, 上面的1000是微秒
        if(tBuff1 > -0.01) tBuff1 -= time;
        if(tBuff2 > -0.01) tBuff2 -= time;
        // 开始绘制自身
        if (imgIndex < aircraft.length * fps)
            g.drawImage(aircraft[imgIndex / fps], x, y, null);
        else if (imgIndex < (aircraft.length + death.length) * fps)
            g.drawImage(death[(imgIndex - aircraft.length * fps) / fps], x, y, null);
        /* 如果想让其播放死亡动画，则直接让imgIndex = aircraft.length * fps + 1, 此时这个方法播放在调用 death.length *fps 次后, draw将不会被执行 */
        if (imgIndex == aircraft.length * fps - 1) {
            imgIndex = 0;
        }
    }
}
