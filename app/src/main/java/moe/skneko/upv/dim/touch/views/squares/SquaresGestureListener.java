package moe.skneko.upv.dim.touch.views.squares;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.Random;

import moe.skneko.upv.dim.touch.models.Square;

public class SquaresGestureListener extends GestureDetector.SimpleOnGestureListener {
    final SquaresView view;
    final Random rng = new Random();

    Square selectedSquare;

    public SquaresGestureListener(SquaresView view) {
        this.view = view;
    }

    public void setSelectedSquare(Square square) {
        selectedSquare = square;
    }

    public boolean hasSelectedSquare() {
        return selectedSquare != null;
    }

    @Override
    public boolean onScroll(MotionEvent origin, MotionEvent current, float distanceX, float distanceY) {
        if (selectedSquare != null) {
            selectedSquare.topRight.x -= distanceX;
            selectedSquare.topRight.y -= distanceY;
        }

        return super.onScroll(origin, current, distanceX, distanceY);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (selectedSquare == null) {
            int color = Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255));
            Square newSquare = new Square(color);
            newSquare.setCenter(e.getX(), e.getY());
            view.squares.add(newSquare);
        }

        return super.onDoubleTap(e);
    }

    class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        static final float SCALE_FACTOR_SMOOTHING = 0.15f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (selectedSquare == null) {
                selectedSquare = view.getTouchedSquare(detector.getFocusX(), detector.getFocusY());
            }
            if (selectedSquare != null) {
                float smoothScaleFactor = (detector.getScaleFactor() - 1) * SCALE_FACTOR_SMOOTHING + 1;
                selectedSquare.size *= smoothScaleFactor;
                selectedSquare.size = Math.min(800, Math.max(selectedSquare.size, 50));
                selectedSquare.setCenter(detector.getFocusX(), detector.getFocusY());
            }

            return super.onScale(detector);
        }
    }
}
