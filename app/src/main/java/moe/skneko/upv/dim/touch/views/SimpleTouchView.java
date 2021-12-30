package moe.skneko.upv.dim.touch.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import androidx.annotation.Nullable;

public class SimpleTouchView extends View {
    final Random rng = new Random();
    final Paint paint = new Paint();
    float prevX, prevY, newX, newY = -1;
    int color = Color.BLACK;

    public SimpleTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color);
        canvas.drawLine(prevX, prevY, newX, newY, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                color = Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255));

                prevX = event.getX();
                prevY = event.getY();
                newX = event.getX();
                newY = event.getY();

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                newX = event.getX();
                newY = event.getY();
                this.invalidate();

                break;
            }
            case MotionEvent.ACTION_UP: {
                prevX = -1;
                prevY = -1;
                newX = -1;
                newY = -1;
                this.invalidate();

                break;
            }
        }

        return true;
    }
}
