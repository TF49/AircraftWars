package engine;

import data.Data;
import model.Aircraft;
import model.Buff;
import model.Bullet;
import utils.Load;
import utils.Rect;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class CollisionSystem {

    public interface DeathHandler {
        void onPlayerDeath();
    }

    public static void update(GameSession session, List<Aircraft> enemy, List<Bullet> bulletPlayer,
                              List<Bullet> bulletEnemy, List<Bullet> bulletBuff, DeathHandler deathHandler) {
        Random random = new Random();
        purgeBullets(bulletEnemy, bulletPlayer, bulletBuff);
        handleEnemyDeaths(session, enemy, bulletBuff, random);
        handleBossDeath(session, bulletBuff);
        handlePlayerBullets(session, enemy, bulletPlayer);
        handleEnemyBullets(session, bulletEnemy, deathHandler);
        handleBuffPickup(session, bulletBuff);
        handlePlaneCollision(session, enemy, deathHandler);
        tickCombo(session);
    }

    static void purgeBullets(List<Bullet> bulletEnemy, List<Bullet> bulletPlayer, List<Bullet> bulletBuff) {
        for (int i = 0; i < bulletEnemy.size(); ) {
            if (bulletEnemy.get(i).isRemove()) bulletEnemy.remove(i);
            else i++;
        }
        for (int i = 0; i < bulletPlayer.size(); ) {
            if (bulletPlayer.get(i).isRemove()) bulletPlayer.remove(i);
            else i++;
        }
        for (int i = 0; i < bulletBuff.size(); ) {
            if (bulletBuff.get(i).isRemove()) bulletBuff.remove(i);
            else i++;
        }
    }

    static void handleEnemyDeaths(GameSession session, List<Aircraft> enemy, List<Bullet> bulletBuff, Random random) {
        Aircraft boss = session.boss;
        for (int i = 0; i < enemy.size(); ) {
            if (enemy.get(i).isRemove()) {
                if (random.nextInt(100) > 80) {
                    int roll = random.nextInt(100);
                    if (roll < 5) {
                        bulletBuff.add(new Buff(Buff.BUFF3, enemy.get(i).x, enemy.get(i).y));
                    } else if (roll > 60) {
                        bulletBuff.add(new Buff(Buff.BUFF2, enemy.get(i).x, enemy.get(i).y));
                    } else {
                        bulletBuff.add(new Buff(Buff.BUFF1, enemy.get(i).x, enemy.get(i).y));
                    }
                }
                Load.playSound("死亡");
                addScore(session, 10);
                if (session.fraction / 100 == session.checkpoint && boss == null) {
                    session.boss = BossFactory.create(session.checkpoint);
                    session.checkpoint += 1;
                    Load.playSound("警告");
                    session.startBossTips();
                }
                enemy.remove(i);
            } else i++;
        }
    }

    static void handleBossDeath(GameSession session, List<Bullet> bulletBuff) {
        Aircraft boss = session.boss;
        if (boss != null && boss.isRemove()) {
            int cx = boss.x + boss.width / 2;
            int cy = boss.y + boss.height / 2;
            bulletBuff.add(new Buff(Buff.BUFF2, cx, cy));
            bulletBuff.add(new Buff(Buff.BUFF1, cx, cy));
            bulletBuff.add(new Buff(Buff.BUFF3, cx, cy));
            bulletBuff.add(new Buff(Buff.BUFF1, cx, cy));
            session.boss = null;
        }
    }

    static void handlePlayerBullets(GameSession session, List<Aircraft> enemy, List<Bullet> bulletPlayer) {
        Aircraft boss = session.boss;
        boolean isPlay = false;
        for (int i = 0; i < bulletPlayer.size(); i++) {
            Point point = bulletPlayer.get(i).getPoint();
            if (bulletPlayer.get(i).buffetIndex > 1) continue;
            if (boss != null && boss.hp > 0) {
                Point[] rect = boss.getCollisionRect();
                if (Rect.isInternal(point.x, point.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                    boss.hp -= bulletPlayer.get(i).struts;
                    bulletPlayer.get(i).buffetIndex = 1;
                    bulletPlayer.get(i).speed = 5;
                    if (boss.hp <= 0) boss.imgIndex = 10;
                    if (!isPlay) {
                        isPlay = true;
                        Load.playSound("击中");
                    }
                    continue;
                }
            }
            for (Aircraft a : enemy) {
                Point[] rect = a.getCollisionRect();
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
                        a.kill();
                        a.speed = 1;
                    }
                    break;
                }
            }
        }
    }

    static void handleEnemyBullets(GameSession session, List<Bullet> bulletEnemy, DeathHandler deathHandler) {
        Aircraft player = session.player;
        if (player == null || player.hp <= 0 || session.invincibleFrames > 0) return;
        Point[] rect = player.getCollisionRect();
        for (Bullet b : bulletEnemy) {
            Point point = b.getPoint();
            if (Rect.isInternal(point.x, point.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                if (consumeShield(player)) continue;
                player.hp -= b.struts;
                player.kill();
                deathHandler.onPlayerDeath();
                return;
            }
        }
    }

    static void handleBuffPickup(GameSession session, List<Bullet> bulletBuff) {
        Aircraft player = session.player;
        if (player == null || player.hp <= 0) return;
        Point[] rect = player.getCollisionRect();
        for (Bullet buff : bulletBuff) {
            Point p = buff.getPoint();
            if (Rect.isInternal(p.x, p.y, rect[0].x, rect[0].y, rect[1].x, rect[1].y)) {
                buff.x = -100;
                if (buff.struts == Buff.BUFF1) player.setBuff(16, 0);
                else if (buff.struts == Buff.BUFF2) player.setBuff(0, 16);
                else if (buff.struts == Buff.BUFF3) player.grantShield();
            }
        }
    }

    static void handlePlaneCollision(GameSession session, List<Aircraft> enemy, DeathHandler deathHandler) {
        Aircraft player = session.player;
        if (player == null || session.invincibleFrames > 0) return;
        for (Aircraft air : enemy) {
            int cx = air.x + air.width / 2;
            int cy = air.y + air.height / 2;
            if (Rect.isInternal(cx, cy, player.x, player.y, player.width, player.height)) {
                if (consumeShield(player)) return;
                player.kill();
                air.kill();
                Load.playSound("失败");
                deathHandler.onPlayerDeath();
                return;
            }
        }
    }

    static boolean consumeShield(Aircraft player) {
        if (player.shieldCharges > 0) {
            player.shieldCharges--;
            return true;
        }
        return false;
    }

    static void addScore(GameSession session, int base) {
        session.comboTimer = Data.FPS * 3;
        session.combo = Math.min(3, session.combo + 1);
        if (session.combo > session.maxCombo) session.maxCombo = session.combo;
        session.fraction += base * session.combo;
    }

    static void tickCombo(GameSession session) {
        if (session.comboTimer > 0) {
            session.comboTimer--;
            if (session.comboTimer == 0) session.combo = 0;
        }
    }
}
