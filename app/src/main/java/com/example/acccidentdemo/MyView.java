package com.example.acccidentdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MyView extends ImageView {
    private final static String TAG = "Main.MyView";

    private static final float CX = 0;
    private static final float CY = 0;
    private static final float RADIUS = 20;
    private static final float BIGRADIUS = 50;
    private static final int NORMAL_COLOR = 0xffffffff;
    private static final int PRESSED_COLOR = 0xffff0000;

    private Paint mPaint;
    private Path mSmallCircle;
    private Path mCircle;
    private Matrix mMatrix;
    private float mAngle;

    private int mSmallCircleColor;

    public MyView(Context context) {
        super(context);
        mPaint = new Paint();

        mSmallCircle = new Path();
        mSmallCircle.addCircle(BIGRADIUS + RADIUS + CX, CY, RADIUS, Path.Direction.CW);
        mSmallCircleColor = NORMAL_COLOR;

        mCircle = new Path();
        mCircle.addCircle(0, 0, BIGRADIUS, Path.Direction.CW);

        mMatrix = new Matrix();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            mSmallCircleColor = NORMAL_COLOR;
            invalidate();
            return false;
        }
        float w2 = getWidth() / 2f;
        float h2 = getHeight() / 2f;
        float r = 0;
        if (action == MotionEvent.ACTION_DOWN) {
            float[] pts = {
                    BIGRADIUS + RADIUS + CX, CY
            };
            mMatrix.mapPoints(pts);
            r = (float) Math.hypot(event.getX() - pts[0], event.getY() - pts[1]);
        }
        if (r < RADIUS) {
            mSmallCircleColor = PRESSED_COLOR;
            mAngle = (float) (180 * Math.atan2(event.getY() - h2, event.getX() - w2) / Math.PI);
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w2 = getWidth() / 2f;
        float h2 = getHeight() / 2f;
        mMatrix.reset();
        mMatrix.postRotate(mAngle);
        mMatrix.postTranslate(w2, h2);

        canvas.concat(mMatrix);
        mPaint.setColor(0x88ffffff);
        canvas.drawPath(mCircle, mPaint);
        mPaint.setColor(mSmallCircleColor);
        canvas.drawPath(mSmallCircle, mPaint);
    }
}