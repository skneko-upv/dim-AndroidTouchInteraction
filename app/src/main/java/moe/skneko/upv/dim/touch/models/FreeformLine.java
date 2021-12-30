package moe.skneko.upv.dim.touch.models;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class FreeformLine {

    public final List<Point> points = new ArrayList<>();
    public final int color;

    public FreeformLine(float startX, float startY, int color) {
        this.color = color;

        points.add(new Point(startX, startY));
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int i = 1; i < points.size(); i ++) {
            Point start = points.get(i - 1);
            Point end = points.get(i);
            paint.setColor(color);
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
    }
}
