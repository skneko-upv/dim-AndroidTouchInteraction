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
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import moe.skneko.upv.dim.touch.models.Line;

public class PersistentTouchView extends View {
    final Random rng = new Random();
    final Paint paint = new Paint();
    final List<Line> lines = new ArrayList<>();

    public PersistentTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Line line : lines) {
            paint.setColor(line.color);
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int color = Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255));
                lines.add(new Line(event.getX(), event.getY(), color));

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Line current = lines.get(lines.size() - 1);
                current.endX = event.getX();
                current.endY = event.getY();
                this.invalidate();

                break;
            }
        }

        return true;
    }
}
