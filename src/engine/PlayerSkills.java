package engine;

import data.Data;
import model.Aircraft;
import model.Bullet;
import utils.Load;

import java.awt.*;
import java.util.List;

public class PlayerSkills {

    public static void togglePause(GameSession session) {
        session.paused = !session.paused;
    }

    public static void useBomb(GameSession session, List<Aircraft> enemy, List<Bullet> bulletEnemy) {
        if (session.bombs <= 0 || session.bombCooldown > 0 || session.respawning) return;
        session.bombs--;
        session.bombCooldown = Data.FPS;
        bulletEnemy.clear();
        for (Aircraft air : enemy) {
            if (air.hp > 0) {
                air.hp -= 50;
                if (air.hp <= 0) {
                    air.kill();
                    air.speed = 1;
                }
            }
        }
        Aircraft boss = session.boss;
        if (boss != null && boss.hp > 0) {
            boss.hp -= 80;
            if (boss.hp <= 0) boss.imgIndex = 10;
        }
        Load.playSound("击中");
    }

    public static void tickCooldown(GameSession session) {
        if (session.bombCooldown > 0) session.bombCooldown--;
    }
}
