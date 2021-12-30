package moe.skneko.upv.dim.touch.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import moe.skneko.upv.dim.touch.models.Point;

public class TangibleTouchView extends View {
    public static final float STICKY_DISTANCE_EPSILON = 1000.0f;
    public static final int TRIANGLE_VERTEX_COUNT = 3;

    final Paint paint = new Paint();
    final Map<Integer, Point> pointers = new HashMap<>();
    final List<Integer> trianglePointers = new ArrayList<>();
    int lastMinIdx = -1;

    public TangibleTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPointerHints(canvas);

        if (trianglePointers.size() == 3) {
            drawTriangle(canvas);
        } else {
            lastMinIdx = -1;
        }
    }

    private void drawTriangle(Canvas canvas) {
        Point[] vertices = new Point[TRIANGLE_VERTEX_COUNT];
        for (int i = 0 ; i < vertices.length ; i++) {
            vertices[i] = pointers.get(trianglePointers.get(i));
        }

        paint.setColor(Color.GREEN);
        float[] distances = new float[3];
        for (int i = 0 ; i < vertices.length ; i++) {
            canvas.drawLine(
                    vertices[i].x, vertices[i].y,
                    vertices[(i + 1) % vertices.length].x, vertices[(i + 1) % vertices.length].y,
                    paint);
            distances[i] = getDistance(vertices[i], vertices[(i + 1) % vertices.length]);
        }

        int minIdx = findMinDistanceIdx(distances);
        if (lastMinIdx >= 0 && Math.abs(distances[minIdx] - distances[lastMinIdx]) > STICKY_DISTANCE_EPSILON) {
            minIdx = lastMinIdx;
        } else {
            lastMinIdx = minIdx;
        }
        Point midA = vertices[minIdx];
        Point midB = vertices[(minIdx + 1) % vertices.length];
        Point mid = new Point((midA.x + midB.x) / 2, (midA.y + midB.y) / 2);
        Point oppositeVertex = vertices[(minIdx + 2) % vertices.length];

        double alpha = Math.atan2(oppositeVertex.y - mid.y, oppositeVertex.x - mid.x);
        paint.setColor(Color.RED);
        canvas.drawLine(mid.x, mid.y,
                (float) (canvas.getHeight() * Math.cos(alpha) + oppositeVertex.x),
                (float) (canvas.getHeight() * Math.sin(alpha) + oppositeVertex.y),
                paint);
    }

    private int findMinDistanceIdx(float[] distances) {
        int minIdx = 0;
        float minValue = Float.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < minValue) {
                minValue = distances[i];
                minIdx = i;
            }
        }
        return minIdx;
    }

    private void drawPointerHints(Canvas canvas) {
        for (Point pointerHint : pointers.values()) {
            paint.setColor(Color.CYAN);
            canvas.drawCircle(pointerHint.x, pointerHint.y, 50, paint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                Point point = new Point(event.getX(pointerIndex), event.getY(pointerIndex));

                pointers.put(pointerId, point);
                if (trianglePointers.size() < 3) {
                    trianglePointers.add(pointerId);
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (Map.Entry<Integer, Point> entry : pointers.entrySet()) {
                    int pointerIndex = event.findPointerIndex(entry.getKey());
                    Point pointerHint = entry.getValue();

                    pointerHint.x = event.getX(pointerIndex);
                    pointerHint.y = event.getY(pointerIndex);
                }

                this.invalidate();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                pointers.remove(pointerId);
                trianglePointers.remove((Integer)pointerId);
                this.invalidate();

                break;
            }
        }

        return true;
    }

    private float getDistance(Point a, Point b) {
        return getDistance(a.x, a.y, b.x, b.y);
    }

    private float getDistance(float startx, float starty, float endx, float endy) {
        float dx = Math.abs(startx - endx);
        float dy = Math.abs(starty - endy);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
