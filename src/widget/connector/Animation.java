package widget.connector;

import java.awt.*;


public interface Animation<T> {
    // 显示绘制自己
    void show(Graphics g);
    // 动画是否结束
    boolean isEnd();
    // 设置动画
    void setAnimation(T animation);
}
