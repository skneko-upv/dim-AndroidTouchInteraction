package moe.skneko.upv.dim.touch.models;

public class Square {

    public final int color;
    public Point topRight;
    public float size;

    public Square(int color) {
        this.color = color;
        size = 100;
    }

    public void setCenter(float centerX, float centerY) {
        topRight = new Point(centerX - size / 2, centerY - size / 2);
    }

    public float left() {
        return topRight.x;
    }

    public float right() {
        return topRight.x + size;
    }

    public float top() {
        return topRight.y;
    }

    public float bottom() {
        return topRight.y + size;
    }
}
