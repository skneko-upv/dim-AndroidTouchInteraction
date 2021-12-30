package moe.skneko.upv.dim.touch.views.squares;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import moe.skneko.upv.dim.touch.models.Square;

public class SquaresView extends View {
    final List<Square> squares = new ArrayList<>();

    final GestureDetector gestureDetector;
    final ScaleGestureDetector scaleGestureDetector;
    final SquaresGestureListener gestureListener;
    final SquaresGestureListener.ScaleGestureListener scaleGestureListener;
    final Paint paint = new Paint();

    public SquaresView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        gestureListener = new SquaresGestureListener(this);
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setOnDoubleTapListener(gestureListener);

        scaleGestureListener = gestureListener.new ScaleGestureListener();
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Square square : squares) {
            paint.setColor(square.color);
            canvas.drawRect(square.left(), square.top(), square.right(), square.bottom(), paint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (!gestureListener.hasSelectedSquare()) {
                    gestureListener.setSelectedSquare(getTouchedSquare(event.getX(), event.getY()));
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                gestureListener.setSelectedSquare(null);

                break;
            }
        }

        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);

        this.invalidate();
        return true;
    }

    Square getTouchedSquare(float x, float y) {
        for (int i = squares.size() - 1; i >= 0; i--) {
            Square square = squares.get(i);
            if (x >= square.left() && x <= square.right() && y >= square.top() && y <= square.bottom()) {
                return square;
            }
        }

        return null;
    }
}
