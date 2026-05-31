package engine;

import data.Data;
import model.Aircraft;
import model.Bullet;
import utils.Load;

import java.awt.*;
import java.util.List;

public class GameHUD {

    public static void draw(Graphics g, GameSession session) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 16f));
        g.drawString("分数: " + session.fraction, 10, 25);
        g.drawString("关卡: " + session.checkpoint, 10, 45);
        g.drawString("生命: " + session.lives, 10, 65);
        if (session.combo > 1) {
            g.setColor(Color.YELLOW);
            g.drawString("Combo x" + session.combo, 10, 85);
        }
        g.setColor(Color.CYAN);
        g.drawString("炸弹: " + session.bombs, Data.WIDTH - 90, 25);

        Aircraft boss = session.boss;
        if (boss != null && boss.maxHp > 0) {
            g.drawImage(Data.hpBox, 10, -10, null);
            g.setColor(Color.orange);
            int barWidth = (int) (300 * ((boss.hp * 1.0) / boss.maxHp));
            g.fillRect(106, -2, Math.max(0, barWidth), 15);
        }

        if (session.tipsActive) {
            g.drawImage(Data.tips, session.tipsX, 200, null);
        }

        if (session.paused) {
            g.setColor(new Color(0, 0, 0, 120));
            g.fillRect(0, 0, Data.WIDTH, Data.HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD, 32f));
            g.drawString("PAUSED", Data.WIDTH / 2 - 70, Data.HEIGHT / 2);
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 14f));
            g.drawString("按 P 继续", Data.WIDTH / 2 - 40, Data.HEIGHT / 2 + 30);
        }

        if (session.respawning) {
            g.setColor(new Color(255, 255, 255, 180));
            g.setFont(g.getFont().deriveFont(Font.BOLD, 20f));
            g.drawString("复活中...", Data.WIDTH / 2 - 50, Data.HEIGHT / 2);
        }
    }

    public static void drawPlayer(Graphics g, Aircraft player, GameSession session) {
        if (player == null) return;
        if (session.invincibleFrames > 0 && session.fps % 6 < 3) return;
        player.draw(g);
    }
}
