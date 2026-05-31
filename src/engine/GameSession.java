package engine;

import data.Data;
import model.Aircraft;

import java.awt.Point;

public class GameSession {

    public static GameSession current;

    public Aircraft player;
    public Aircraft boss;
    public int lives = Data.LIFE;
    public int fraction = 0;
    public int checkpoint = 1;
    public int fps = 0;
    public int combo = 0;
    public int comboTimer = 0;
    public int maxCombo = 0;
    public boolean paused = false;
    public int bombs = 3;
    public int bombCooldown = 0;
    public int invincibleFrames = 0;
    public boolean respawning = false;
    public int deathAnimFrames = 0;

    public boolean up, down, left, right;

    public int tipsX = -1000;
    public int tipsPhase = 0;
    public int tipsHoldFrames = 0;
    public boolean tipsActive = false;

    public GameSession() {
        current = this;
    }

    public void resetMovement() {
        up = down = left = right = false;
    }

    public void startBossTips() {
        tipsPhase = 1;
        tipsX = -Data.WIDTH;
        tipsHoldFrames = Data.FPS * 2;
        tipsActive = true;
    }

    public void updateTips() {
        if (tipsPhase == 0) return;
        if (tipsPhase == 1) {
            tipsX += 2;
            if (tipsX >= 0) {
                tipsX = 0;
                tipsPhase = 2;
            }
        } else if (tipsPhase == 2) {
            if (tipsHoldFrames-- <= 0) tipsPhase = 3;
        } else if (tipsPhase == 3) {
            tipsX += 4;
            if (tipsX > Data.WIDTH) {
                tipsPhase = 0;
                tipsActive = false;
                tipsX = -1000;
            }
        }
    }

    public void respawnPlayer() {
        player = new Aircraft(Data.playerAircraftImage, Data.playerDeathImage, Data.SPEED, 0,
                Data.WIDTH / 2 - 40, Data.HEIGHT - 100);
        player.upperLeft = new Point(15, 15);
        player.lowerRight = new Point(75, 75);
        player.hp = 10;
        invincibleFrames = Data.FPS * 2;
        respawning = false;
        deathAnimFrames = 0;
    }

    public static void clearCurrent() {
        current = null;
    }
}
