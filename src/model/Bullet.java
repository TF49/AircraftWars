package model;

import data.Data;
import utils.Rect;

import java.awt.*;

public class Bullet {

    // 子弹的坐标, 大小
    public int x, y, width, height;
    // 子弹移动速度
    public int speed;
    // 子弹移动角度
    public double deg;
    // 子弹图片数组，他的大小为3，也只能为3
    public Image[] bullet;
    // 子弹当前索引
    public int buffetIndex = 0;
    // 为了方便，我把道具也化为一种另类的子弹， 击杀敌机有记录掉落子弹， 这个type规定了子弹的类型，有三种， 即我方飞机，敌方飞机，道具
    // 在每种状态下，struts表达的意义不同，详细意义见他的子类, 作为子弹时，struts表示伤害
    public int struts;

    // 子弹的图像,bullet[0] 为正常状态,bullet[1-2]为爆炸特效
    public Bullet(Image[] bullet, double deg, int speed, int x, int y) {
        // 子弹的移动不受外力控制， 在子弹生成的时候，子弹的轨迹已经确定，由于子弹的类型不多，只有两种
        this.bullet = bullet;
        this.deg = deg;
        this.speed = speed;
        this.x = x;
        this.y = y;
        width = bullet[0].getWidth(null);
        height = bullet[0].getHeight(null);
        // 子弹的伤害
        struts = 1;
    }

    public void move() {
        // move方法由子类自己实现，因为他们的移动方式都不同，其中分为BOSS的子弹，小怪的子弹，玩家的子弹， 这里我想了下，玩家的子弹比较单一，因此我还是实现一下move方法
        // 实际上他们的移动方式貌似是相同的，不过下面的步骤的确是必须的
        // 比如boss的子弹中的一种，转圈圈的子弹，只需要给一个不为90的倍数的deg，就可以实现转圈圈
        Point location = Rect.getPoint(x, y, deg, speed);
        this.x = location.x;
        this.y = location.y;
    }

    public void draw(Graphics g) {
        // 自动绘制结束
        if (buffetIndex / 5 + 2 >= bullet.length) return;
        // 正常状态下,buffetIndex == 0,当子弹和飞机碰撞时,buffetIndex被设置为1,开始播放2帧的碰撞动画,播放完成后,buffetIndex的值会增加到>=10
        g.drawImage(bullet[buffetIndex == 0 ? 0 : (buffetIndex += 1) / 5 + 1], x, y, null);
    }

    // 获取子弹的碰撞检测点,因为子弹太小，没必要矩形检测
    public Point getPoint() {
        // 子弹的碰撞点就是子弹的中心坐标
        return new Point(x + width / 2, y + height / 2);
    }

    // 判断是否应该移除这个对象,如果子弹击中敌人，或者子弹移动到屏幕外时，这个方法应该返回true
    public boolean isRemove() {
        // buffetIndex >= 10 表示子弹已经击中了敌人，因为它的击中动画已经播放完毕，(x > Data.WIDTH || y > Data.HEIGHT || x < -(width * 2) || y < -(height * 2))
        // 表示自动移动到了画布外，这两个条件符合如何一个，都应该移除子弹，避免不必要的CUP和内存开销
        return (buffetIndex / 5 + 1 >= bullet.length) || (x > Data.WIDTH || y > Data.HEIGHT || x < -(width * 2) || y < -(height * 2));
    }
}
