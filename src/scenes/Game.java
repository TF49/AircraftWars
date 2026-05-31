package scenes;

import data.Data;
import engine.*;
import model.Aircraft;
import model.Bullet;
import scenes.connector.Scenes;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game implements Scenes {

    final GameSession session = new GameSession();
    final List<Aircraft> enemy = new ArrayList<>();
    final List<Bullet> bulletPlayer = new ArrayList<>();
    final List<Bullet> bulletEnemy = new ArrayList<>();
    final List<Bullet> bulletBuff = new ArrayList<>();
    final Random random = new Random();

    public Game() {
        session.respawnPlayer();
    }

    @Override
    public void onKeyDown(int keyCode) {
        if (session.respawning) return;
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) session.up = true;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) session.down = true;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) session.left = true;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) session.right = true;
        if (keyCode == KeyEvent.VK_P) PlayerSkills.togglePause(session);
        if (keyCode == KeyEvent.VK_SPACE) PlayerSkills.useBomb(session, enemy, bulletEnemy);
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) session.up = false;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) session.down = false;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) session.left = false;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) session.right = false;
    }

    @Override
    public void onMouse(int x, int y, int struts) {
    }

    @Override
    public void draw(Graphics g) {
        if (session.fps == 0) session.resetMovement();
        session.fps++;
        session.updateTips();

        if (!session.paused && !session.respawning) {
            move();
            attack();
            generate();
            CollisionSystem.update(session, enemy, bulletPlayer, bulletEnemy, bulletBuff, this::onPlayerDeath);
            PlayerSkills.tickCooldown(session);
        }

        if (session.invincibleFrames > 0) session.invincibleFrames--;
        if (session.respawning) tickRespawn();

        Data.background.show(g);
        GameHUD.drawPlayer(g, session.player, session);
        if (session.boss != null) session.boss.draw(g);
        for (Aircraft a : enemy) a.draw(g);
        for (Bullet b : bulletPlayer) b.draw(g);
        for (Bullet b : bulletEnemy) b.draw(g);
        for (Bullet d : bulletBuff) d.draw(g);
        GameHUD.draw(g, session);
    }

    void move() {
        Aircraft player = session.player;
        if (player != null) {
            Data.x = player.x;
            Data.y = player.y;
            if (session.up) player.move(0, -player.speed);
            if (player.y < 0) player.y = 0;
            if (session.down) player.move(0, player.speed);
            if (player.y > Data.HEIGHT - player.height) player.y = Data.HEIGHT - player.height;
            if (session.left) player.move(-player.speed, 0);
            if (player.x < 0) player.x = 0;
            if (session.right) player.move(player.speed, 0);
            if (player.x > Data.WIDTH - player.width) player.x = Data.WIDTH - player.width;
        }
        if (session.boss != null) session.boss.move();
        for (Bullet bullet : bulletPlayer) bullet.move();
        for (Bullet bullet : bulletEnemy) bullet.move();
        for (Bullet bullet : bulletBuff) bullet.move();
        for (Aircraft air : enemy) air.move();
    }

    void attack() {
        Aircraft player = session.player;
        if (player != null && player.hp > 0) {
            if (!player.isRemove() && session.fps % 3 == 0) {
                bulletPlayer.addAll(Arrays.asList(player.attack()));
            }
        }
        if (session.fps % 5 == 0) {
            for (Aircraft em : enemy) bulletEnemy.addAll(Arrays.asList(em.attack()));
        }
        if (session.boss != null) bulletEnemy.addAll(Arrays.asList(session.boss.attack()));
    }

    void generate() {
        if (session.boss != null) return;
        int interval = LevelConfig.getWaveInterval(session.checkpoint);
        if (session.fps % interval != 0 && session.fps >= 1) return;
        EnemySpawner.spawn(enemy, session.fps, session.checkpoint, random);
    }

    void onPlayerDeath() {
        if (session.respawning) return;
        session.deathAnimFrames = Data.FPS * 3;
        session.respawning = true;
        session.player = null;
        bulletEnemy.clear();
    }

    void tickRespawn() {
        session.deathAnimFrames--;
        if (session.deathAnimFrames > 0) return;
        if (session.lives > 1) {
            session.lives--;
            session.respawnPlayer();
        } else {
            GameOver.lastScore = session.fraction;
            GameOver.lastCheckpoint = session.checkpoint;
            GameOver.lastMaxCombo = session.maxCombo;
            GameSession.clearCurrent();
            Data.canvas.switchScenes("GameOver");
        }
    }
}
