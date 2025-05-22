package scenes;

import data.Data;
import model.*;
import scenes.connector.Scenes;
import utils.Load;
import utils.Rect;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game implements Scenes {
    // 玩家 BOSS
    Aircraft player, boss;
    // 敌人
    List<Aircraft> enemy;
    // 玩家子弹 敌人子弹 道具列表
    List<Bullet> bulletPlayer, bulletEnemy, bulletBuff;
    // 玩家生命 生命 < 0 时死亡
    // int life = Data.LIFE;
    // 当前关卡 当前分数
    int checkpoint = 1, fraction = 0;
    // 飞机的飞行方向, 此处需要注意，监听按键来控制飞机移动时，不应该单纯的按键按下一次就执行一次，因为键盘按下不松开，飞机应该一直向某个方向移动,直到按键被放开
    boolean left = false, right = false, down = false, up = false;
    // fps， 记录当前帧数
    int fps = 0;
    // 提示的 x 坐标
    int tipsX = -1000;

    public Game() {
        // 创造一个飞机， 并且放在场景下方, 玩家的角度属性不会使用
        player = new Aircraft(Data.playerAircraftImage, Data.playerDeathImage, Data.SPEED, 0, Data.WIDTH / 2 - 40, Data.HEIGHT - 100);
        // 设置飞机碰撞点，因为游戏采用的矩形碰撞检测，会出现很多误差，所以手动设置碰撞矩形，不采用图片的大小, 这个碰撞的坐标是相对于图片位置的相对坐标
        player.upperLeft = new Point(15, 15);
        player.lowerRight = new Point(75, 75);
        // 初始化列表
        bulletPlayer = new ArrayList<>();
        bulletEnemy = new ArrayList<>();
        bulletBuff = new ArrayList<>();
        enemy = new ArrayList<>();
    }

    public void onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
            up = true;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
            down = true;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
            left = true;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
            right = true;
    }

    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
            up = false;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
            down = false;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
            left = false;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
            right = false;
    }

    public void onMouse(int x, int y, int struts) {

    }

    public void draw(Graphics g) {
        if (fps == 0) {
            left = right = up = down = false;
        }
        // 绘制一次 fps + 1
        fps++;
        // 飞机移动
        move();
        // 子弹发射
        attack();
        // 敌方飞机生成
        generate();
        // 失效对象销毁
        remove();
        // 绘制背景
        Data.background.show(g);
        // 绘制我方飞机
        if(player != null)
            player.draw(g);
        // 绘制BOSS
        if (boss != null) boss.draw(g);
        // 绘制敌方飞机
        for (Aircraft a : enemy)
            a.draw(g);
        // 绘制我发子弹
        for (Bullet b : bulletPlayer)
            b.draw(g);
        // 绘制敌方子弹
        for (Bullet b : bulletEnemy)
            b.draw(g);
        // 绘制道具
        for (Bullet d : bulletBuff)
            d.draw(g);

        g.drawString("分数 : " + fraction, 200, 200);
        if (boss != null) {
            g.drawImage(Data.hpBox, 10, -10, null);
            g.setColor(Color.orange);
            g.fillRect(106, -2, (int) (300 * ((boss.hp * 1.0) / 300)), 15);
        }
        // 绘制boss出现的图片
        if (tipsX > -900 && tipsX < 900) {
            g.drawImage(Data.tips, tipsX, 200, null);
        }
    }

    // 管理场景所有的飞机、子弹、道具移动
    void move() {
        if (player != null) {
            // 这里这个坐标，是为了让敌人发射子弹时定位用的
            Data.x = player.x;
            Data.y = player.y;
            // 飞机向上移动, 这里还限制了飞机的范围，不让其飞出屏幕外
            if (up) player.move(0, -player.speed);
            if (player.y < 0) player.y = 0;
            // 飞机向下移动
            if (down) player.move(0, player.speed);
            if (player.y > Data.HEIGHT - player.height) player.y = Data.HEIGHT - player.height;
            // 飞机向左移动
            if (left) player.move(-player.speed, 0);
            if (player.x < 0) player.x = 0;
            // 飞机向右移动
            if (right) player.move(player.speed, 0);
            if (player.x > Data.WIDTH - player.width) player.x = Data.WIDTH - player.width;
        }
        // boss 移动
        if (boss != null) boss.move();
        // 我方子弹的移动
        for (Bullet bullet : bulletPlayer)
            bullet.move();
        // 敌方子弹的移动
        for (Bullet bullet : bulletEnemy)
            bullet.move();
        // 道具的移动
        for (Bullet bullet : bulletBuff)
            bullet.move();
        // 地方飞机的移动，包括BOSS
        for (Aircraft air : enemy)
            air.move();
        // BUFF 移动
        for (Bullet b : bulletBuff)
            b.move();
    }

    void remove() {
        Random random = new Random();
        // 子弹销毁
        for (int i = 0; i < bulletEnemy.size(); ) {
            if (bulletEnemy.get(i).isRemove()) {
                bulletEnemy.remove(i);
            } else i++;
        }
        for (int i = 0; i < bulletPlayer.size(); ) {
            if (bulletPlayer.get(i).isRemove()) {
                bulletPlayer.remove(i);
            } else i++;
        }
        for (int i = 0; i < bulletBuff.size(); ) {
            if (bulletBuff.get(i).isRemove()) {
                bulletBuff.remove(i);
            } else i++;
        }
        // 敌人销毁
        for (int i = 0; i < enemy.size(); ) {
            if (enemy.get(i).isRemove()) {
                // 生成道具
                if (random.nextInt(100) > 80) {
                    if (random.nextInt(100) > 60)
                        bulletBuff.add(new Buff(Buff.BUFF2, enemy.get(i).x, enemy.get(i).y));
                    else
                        bulletBuff.add(new Buff(Buff.BUFF1, enemy.get(i).x, enemy.get(i).y));
                }
                Load.playSound("死亡");
                // 击杀一个敌人增加 1 分，不整这么多花里胡哨的
                fraction += 10;
                if (fraction / 100 == checkpoint) {
                    boss = new Boss2();
                    checkpoint += 1;
                    Load.playSound("警告");
                    new Thread(() -> {
                        tipsX = -Data.WIDTH;
                        try {
                            for (int n = 0; n < Data.WIDTH / 2; n++) {
                                tipsX += 2;
                                Thread.sleep(4);
                            }
                            Thread.sleep(2000);
                            for (int n = 0; n < Data.WIDTH / 4; n++) {
                                tipsX += 4;
                                Thread.sleep(4);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tipsX = -1000;
                    }).start();
                }
                enemy.remove(i);
            } else i++;
        }
        // boss销毁
        if (boss != null) {
            // boss 死亡掉落4个buff
            if (boss.isRemove()) {
                bulletBuff.add(new Buff(Buff.BUFF2, boss.x + boss.width / 2, boss.y + boss.height / 2));
                bulletBuff.add(new Buff(Buff.BUFF1, boss.x + boss.width / 2, boss.y + boss.height / 2));
                bulletBuff.add(new Buff(Buff.BUFF2, boss.x + boss.width / 2, boss.y + boss.height / 2));
                bulletBuff.add(new Buff(Buff.BUFF1, boss.x + boss.width / 2, boss.y + boss.height / 2));
                boss = null;
            }
        }
        boolean isPlay = false;
        // 检测我方子弹碰撞
        for (int i = 0; i < bulletPlayer.size(); i++) {
            Point point = bulletPlayer.get(i).getPoint();
            if (bulletPlayer.get(i).buffetIndex > 1) continue;
            // 检测子弹是否击中boss
            if (boss != null && boss.hp > 0) {
                Point rect[] = boss.getCollisionRect();
                if (Rect.isInternal(point.x, point.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                    boss.hp -= bulletPlayer.get(i).struts;
                    bulletPlayer.get(i).buffetIndex = 1;
                    bulletPlayer.get(i).speed = 5;
                    if (boss.hp <= 0) {
                        boss.imgIndex = 10;
                    }
                    if (!isPlay) {
                        isPlay = true;
                        Load.playSound("击中");
                    }
                    continue;
                }
            }
            for (Aircraft a : enemy) {
                Point rect[] = a.getCollisionRect();
                if (a.hp < 0) continue;
                if (Rect.isInternal(point.x, point.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                    if (!isPlay) {
                        isPlay = true;
                        Load.playSound("击中");
                    }
                    a.hp -= bulletPlayer.get(i).struts;
                    bulletPlayer.get(i).buffetIndex = 1;
                    bulletPlayer.get(i).speed = 5;
                    if (a.hp < 0) {
                        a.kill();;
                        a.speed = 1;
                    }
                    break;
                }
            }
        }
        Point rect[], point;
        // 检测敌方子弹碰撞
        if(player != null) {
            rect = player.getCollisionRect();
            if (player != null && player.hp > 0) {
                for (Bullet b : bulletEnemy) {
                    point = b.getPoint();
                    if (Rect.isInternal(point.x, point.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                        player.hp -= b.struts;
                        player.kill();
                        death();
                    }
                }
            }
        }
        // 吃BUFF
        if (player != null && player.hp > 0) {
            rect = player.getCollisionRect();
            for (Bullet buff : bulletBuff) {
                Point p = buff.getPoint();
                if (Rect.isInternal(p.x, p.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                    buff.x = -100;
                    if (buff.struts == Buff.BUFF1)
                        player.setBuff(16, 0);
                    else
                        player.setBuff(0, 16);
                }
            }
        }
        // 飞机之间的碰撞
        if(player != null) {
            for (Aircraft air : enemy) {
                int x = air.x + air.width / 2, y = air.y + air.height / 2;

                if(Rect.isInternal(x, y, player.x, player.y, player.width, player.height)) {
                    player.kill();
                    air.kill();
                    Load.playSound("失败");
                    death();
                }
            }
        }
    }

    // 管理场景所有的子弹的发射
    void attack() {
        // 我方飞机3帧发射一次
        if (player != null && player.hp > 0) {
            if (!player.isRemove() && fps % 3 == 0)
                bulletPlayer.addAll(Arrays.asList(player.attack()));
        }
        // 敌方飞机发射子弹
        if (fps % 5 == 0)
            for (Aircraft em : enemy)
                bulletEnemy.addAll(Arrays.asList(em.attack()));
        // boss发射子弹
        if (boss != null) bulletEnemy.addAll(Arrays.asList(boss.attack()));
    }

    // 生成敌方飞机
    void generate() {
        // BOSS 存在时不生成小飞机
        if (boss != null) return;

        if (fps % 100 != 0 && fps >= 1) return;

        Random random = new Random();
        int rn = random.nextInt(100) + 1;

        int[] hp = {(fps / 2000) + 1 * checkpoint, (fps / 2000) + 2 * checkpoint, (fps / 2000) + 5 * checkpoint, (fps / 2000) + 8 * checkpoint, (fps / 2000) + 11 * checkpoint};

        switch (rn / 10) {
            case 1: {
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, random.nextInt(300) + 100, -150, hp[4]));
                return;
            }
            case 2: {
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 100, -150, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 380, -150, hp[3]));
                return;
            }
            case 3: {
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 10, -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 90, 240, -150, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 430 + 100, -150, hp[2]));
                return;
            }
            case 4: {
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 0, -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 130, -150, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 90, 300, -150, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 90, 450, -150, hp[1]));
                return;
            }
            case 5: {
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 110, 400, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 480, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 560, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 640, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 110, 720, random.nextInt(100) - 200, hp[0]));
                return;
            }
            case 6: {
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, -440, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -320, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -240, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -170, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, -100, random.nextInt(100) - 200, hp[1]));
                return;
            }
            case 7: {
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, 30, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 70, 100, random.nextInt(100) - 200, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 70, 200, random.nextInt(100) - 200, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 70, 300, random.nextInt(100) - 200, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, 430, random.nextInt(100) - 200, hp[1]));
                return;
            }
            case 8: {
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 30, -100, -150, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 90, 240, -150, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 120, 615 + 100, -150, hp[1]));
                return;
            }
            case 9: {
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, 100 + random.nextInt(100), -150, hp[4]));
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, 415 - random.nextInt(100), -150, hp[4]));
                return;
            }
            case 10: {
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 120 + random.nextInt(100), -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 395 - random.nextInt(100), -150, hp[2]));
            }
        }
    }
    void death() {
        new Thread(() ->{
            try {
                player = null;
                Thread.sleep(3000);
                Data.canvas.switchScenes("Home");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
