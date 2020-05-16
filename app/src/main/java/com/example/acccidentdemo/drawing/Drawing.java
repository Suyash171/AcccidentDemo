package com.example.acccidentdemo.drawing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.acccidentdemo.R;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Drawing extends ImageView {

    private static final int kColor = Color.BLACK;
    private static final float kTouchTolerance = 5;
    private static final float kLineThickness = 5;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private float mX;
    private float mY;
    private float mTolerance = kTouchTolerance;
    private float mThickness = kLineThickness;
    private int mColor = kColor;
    private EditText editText;
    private Context mContext;
    private String key = "";
    // public static final int DRAW_CIRCLE = 0;
    //public static final int DRAW_CUSTOM = 1;
    private int shape = 1;
    private float startX = 0F;
    private float startY = 0F;

    // Enumeration for Drawer
    public static class Drawer {
        public static final int PEN = 0;
        public static final int LINE = 1;
        public static final int RECTANGLE = 2;
        public static final int CIRCLE = 3;
        public static final int ELLIPSE = 4;
        public static final int ARROW = 5;
    }


    public Drawing(Context context) {
        super(context);
        //mContext = context;
        initView();
    }

    public Drawing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawingView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {

            int attr = a.getIndex(i);
            if (attr == R.styleable.DrawingView_drawingColor) {
                setDrawingColor(a.getColor(attr, kColor));
            } else if (attr == R.styleable.DrawingView_drawingThickness) {
                setDrawingThickness(a.getFloat(attr, kLineThickness));
            } else if (attr == R.styleable.DrawingView_drawingTolerance) {
                setDrawingTolerance(a.getFloat(attr, kTouchTolerance));
            }
        }

        a.recycle();
    }

    private Path createPath(MotionEvent event) {
        Path path = new Path();
        // Save for ACTION_MOVE
        this.startX = event.getX();
        this.startY = event.getY();

        path.moveTo(this.startX, this.startY);

        return path;
    }

    public int getDrawingColor() {
        return mColor;
    }

    public void setDrawingColor(int color) {
        mColor = color;
        mPaint.setColor(color);
    }

    public float getThickness() {
        return mThickness;
    }

    public void setDrawingThickness(float thickness) {
        mThickness = thickness;
        mPaint.setStrokeWidth(thickness);
    }

    public float getTolerance() {
        return mTolerance;
    }

    public void setDrawingTolerance(float tolerance) {
        mTolerance = tolerance;
    }


    /**
     * Reset the drawing view by cleaning the canvas that contains the drawing
     */
    public void resetDrawing() {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /**
     * Let you retrieve the drawing that has been drawn inside the canvas
     *
     * @return The drawing as a Bitmap
     */
    public Bitmap getDrawing() {
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(getLeft(), getTop(), getRight(), getBottom());
        draw(c);

        return b;
    }


    private void initView() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mThickness);

       /* editText = new EditText(mContext);
        editText.setHint("Please enter text here..");
        editText.setWidth(180);
        editText.setBackgroundColor(Color.RED);*/
    }

    private void startTouch(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= mTolerance || dy >= mTolerance) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void endTouch() {
        if (!mPath.isEmpty()) {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(40);
        } else {
            mCanvas.drawPoint(mX, mY, mPaint);
        }

        mPath.reset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, (h > 0 ? h : ((View) getParent()).getHeight()), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAccordingToShape(shape, canvas);
    }


    /**
     * @param shape
     * @param canvas
     */
    private void drawAccordingToShape(int shape, Canvas canvas) {
        switch (shape) {
            case Drawer.CIRCLE:
                //mBitmapPaint.setColor(Color.YELLOW);
                // canvas.drawCircle(mX, mY, 35, mBitmapPaint);
                ///canvas.drawPath(mPath, mPaint);
                double distanceX = Math.abs((double) (this.startX - mX));
                double distanceY = Math.abs((double) (this.startX - mY));
                double radius = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

                canvas.drawCircle(this.startX, this.startY, (float) radius, mPaint);
                break;
            case Drawer.PEN:
                canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                canvas.drawPath(mPath, mPaint);
                break;
            case Drawer.RECTANGLE:

                float left = Math.min(this.startX, mX);
                float right = Math.max(this.startX, mX);
                float top = Math.min(this.startY, mY);
                float bottom = Math.max(this.startY, mY);

                int sideLength = 200;
                Rect rectangle = new Rect((int) left,(int) right,(int) top,(int)bottom);
                //path.addRect(left, top, right, bottom, Path.Direction.CCW);
                canvas.drawRect(rectangle, mPaint);

                break;
            default:
                break;
        }
    }

    private void drawArrow(Paint paint, Canvas canvas, float from_x, float from_y, float to_x, float to_y) {
        float angle, anglerad, radius, lineangle;

        //values to change for other appearance *CHANGE THESE FOR OTHER SIZE ARROWHEADS*
        radius = 10;
        angle = 15;

        //some angle calculations
        anglerad = (float) (PI * angle / 180.0f);
        lineangle = (float) (atan2(to_y - from_y, to_x - from_x));

        //tha line
        canvas.drawLine(from_x, from_y, to_x, to_y, paint);

        //tha triangle
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(to_x, to_y);
        path.lineTo((float) (to_x - radius * cos(lineangle - (anglerad / 2.0))),
                (float) (to_y - radius * sin(lineangle - (anglerad / 2.0))));
        path.lineTo((float) (to_x - radius * cos(lineangle + (anglerad / 2.0))),
                (float) (to_y - radius * sin(lineangle + (anglerad / 2.0))));
        path.close();

        canvas.drawPath(path, paint);
    }
/*
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyaction = event.getAction();

        if(keyaction == event.ACTION_DOWN)
        {
            int keyunicode = event.getUnicodeChar(event.getMetaState() );
            char character = (char) keyunicode;
            key += character;
            System.out.println("DEBUG MESSAGE KEY=" + character);
        }
        // you might add if (delete)
        // https://stackoverflow.com/questions/7438612/how-to-remove-the-last-character-from-a-string
        // method to delete last character
        invalidate();
        return super.dispatchKeyEvent(event);
    }
*/

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                createPath(e);
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                createPath(e);
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endTouch();
                invalidate();
                break;
        }

        return true;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }
}
