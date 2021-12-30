package moe.skneko.upv.dim.touch.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import moe.skneko.upv.dim.touch.models.FreeformLine;
import moe.skneko.upv.dim.touch.models.Point;

public class MultiTouchView extends View {
    final Random rng = new Random();
    final Paint paint = new Paint();
    final Map<Integer, FreeformLine> lines = new HashMap<>();

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (FreeformLine line : lines.values()) {
            line.draw(canvas, paint);
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
                int color = Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255));
                lines.put(pointerId, new FreeformLine(event.getX(pointerIndex), event.getY(pointerIndex), color));

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (Map.Entry<Integer, FreeformLine> entry : lines.entrySet()) {
                    int pointerIndex = event.findPointerIndex(entry.getKey());
                    FreeformLine line = entry.getValue();

                    float x = event.getX(pointerIndex);
                    float y = event.getY(pointerIndex);
                    line.points.add(new Point(x, y));
                }
                this.invalidate();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                lines.remove(pointerId);
                this.invalidate();

                break;
            }
        }

        return true;
    }

}
