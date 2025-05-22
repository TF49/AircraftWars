package data;

import main.Canvas;
import model.Bullet;
import utils.Load;
import widget.ScrollBackground;
import widget.connector.Animation;

import javax.sound.sampled.Clip;
import java.applet.AudioClip;
import java.awt.*;

public class Data {
    // 窗口的宽度、高度,游戏的刷新速度
    public static final int WIDTH = 512, HEIGHT = 768, FPS = 50;
    // 显示游戏画面的面板
    public static Canvas canvas;
    // 这个是标题栏的高度,即最大化最小化所在的标题栏的高度
    public static int TITLE_BOX_HEIGHT;
    // 游戏背景 Animation 的泛型实际上没有使用到
    public static Animation background;
    // 我方飞机子弹 飞机
    public static Image[] playerBulletImage, playerAircraftImage, playerDeathImage;
    // 敌方飞机 敌方飞机子弹
    public static Image[][] enemyBulletImages, enemyAircraftImages, enemyDeathImages;
    // BOSS飞机 BOSS飞机子弹
    public static Image[][] bossBulletImages, bossAircraftImages, bossDeathImages;
    // 提示图片
    public static Image tips;
    // BOSS血条
    public static Image hpBox;
    // BUFF
    public static Image[][] buff;
    // 玩家的生命数 玩家的移动速度
    public static final int LIFE = 3, SPEED = 8;
    // 玩家的位置
    public static int x, y;

    // 必要的初始化
    public static void init() {
        Data.background = new ScrollBackground(null);
        // 加载我方飞机图片
        playerAircraftImage = new Image[]{
                Load.image("我方飞机1.png"), Load.image("我方飞机2.png"), Load.image("我方飞机3.png")
        };
        // 加载我方飞机子弹图片
        playerBulletImage = new Image[]{
                Load.image("子弹1.png"), Load.image("子弹1-击中效果1.png"), Load.image("子弹1-击中效果2.png")
        };
        // 加载我方飞机死亡图片
        playerDeathImage = new Image[]{
                Load.image("我方飞机爆炸1.png"), Load.image("我方飞机爆炸2.png"),
                Load.image("我方飞机爆炸3.png"), Load.image("我方飞机爆炸4.png"), Load.image("我方飞机爆炸5.png")
        };
        // 加载敌方飞机图片
        enemyAircraftImages = new Image[5][];
        for (int i = 0; i < 5; i++) {
            enemyAircraftImages[i] = new Image[]{
                    Load.image("敌机" + (i + 1) + ".png")
            };
        }
        // 加载敌方飞机子弹, 他们的子弹是一样的, 我这里只初始化一次,而且数组是1*1的数组，后面使用时一定要注意此处，可能会造成数组越界
        enemyBulletImages = new Image[1][];
        enemyBulletImages[0] = new Image[]{Load.image("子弹2.png")};
        // 加载敌方飞机死亡图片
        enemyDeathImages = new Image[5][];
        for (int i = 0; i < 5; i++) {
            enemyDeathImages[i] = new Image[]{
                    Load.image("敌机" + (i + 1) + "爆炸1.png"), Load.image("敌机" + (i + 1) + "爆炸2.png"),
                    Load.image("敌机" + (i + 1) + "爆炸3.png"), Load.image("敌机" + (i + 1) + "爆炸4.png"), Load.image("敌机" + (i + 1) + "爆炸5.png")
            };
        }
        // 加载BOSS图片
        bossAircraftImages = new Image[2][];
        for (int i = 0; i < 2; i++) {
            bossAircraftImages[i] = new Image[]{
                    Load.image("BOSS" + (i + 1) + "-1.png"), Load.image("BOSS" + (i + 1) + "-2.png")
            };
        }
        // 加载BOSS子弹， 这个BOSS的子弹和小怪的子弹一样，也只有一个，而且没有动画
        bossBulletImages = new Image[1][];
        bossBulletImages[0] = new Image[]{Load.image("子弹3.png")};
        // 加载BOSS死亡图片
        bossDeathImages = new Image[2][];
        for (int i = 0; i < 2; i++) {
            bossDeathImages[i] = new Image[]{
                    Load.image("BOSS" + (i + 1) + "爆炸1.png"), Load.image("BOSS" + (i + 1) + "爆炸2.png"),
                    Load.image("BOSS" + (i + 1) + "爆炸3.png"), Load.image("BOSS" + (i + 1) + "爆炸4.png"), Load.image("BOSS" + (i + 1) + "爆炸5.png"),
            };
        }
        buff = new Image[][]{
                {Load.image("道具-双倍子弹.png")},
                {Load.image("道具-三倍子弹.png")}
        };
        hpBox = Load.image("BOSS血条.png");
        tips = Load.image("BOSS出现提示.png");
        /* 图片全部加载完毕 */
    }
}
