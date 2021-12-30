package moe.skneko.upv.dim.touch.models;

public class Line {
    public final float startX;
    public final float startY;
    public float endX;
    public float endY;

    public final int color;

    public Line(float startX, float startY, int color) {
        this.startX = startX;
        this.startY = startY;
        this.color = color;
        endX = startX;
        endY = startY;
    }
}
