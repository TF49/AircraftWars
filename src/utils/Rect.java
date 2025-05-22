package utils;

import java.awt.*;

public class Rect {

    /* 判断点 (x,y) 是否在矩形 (rx, ry, rx+width, ry+height) 内部 */
    public static boolean isInternal(int x, int y, int rx, int ry, int width, int height) {
        int cx = x - rx, cy = y - ry;
        return cx > 0 && cy > 0 && cx < width && cy < height;
    }

    /* 求 点(x,y) 在 deg 角度的方向 延伸 distance 像素,到达的点的位置 */
    public static Point getPoint(int x, int y, double deg, int distance) {
        //角度转弧度
        double radian = (deg * Math.PI) / 180;
        //计算新坐标
        int px = (int)(x + distance * Math.cos(radian) + 0.5);
        int py = (int)(y + distance * Math.sin(radian) + 0.5);
        // 返回获得的坐标
        return new Point(px, py);
    }

    /* 判断两矩形是否相交, 参数是两矩形的左上角顶点和高宽(这里的矩形不会旋转) */
    public static boolean isIntersect(int x, int y, int w, int h, int cx, int cy, int cw, int ch) {
        return (Math.abs(x - cx) < (w + cw) / 2) || (Math.abs(y - cy) < (h + ch) / 2);
    }
}
