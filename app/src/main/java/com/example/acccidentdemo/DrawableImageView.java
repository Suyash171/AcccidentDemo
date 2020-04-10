package com.example.acccidentdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class DrawableImageView extends ImageView implements View.OnTouchListener {

    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;

    Canvas canvas;
    Paint paint;
    Matrix matrix;

    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<Path> paths = new ArrayList<Path>();


    public DrawableImageView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public DrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public DrawableImageView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    public void setNewImage(Bitmap alteredBitmap, Bitmap bmp) {
        canvas = new Canvas(alteredBitmap);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, paint);

        setImageBitmap(alteredBitmap);
    }

    public void clear() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = getPointerCoords(event)[0];//event.getX();
                downy = getPointerCoords(event)[1];//event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                upx = getPointerCoords(event)[0];//event.getX();
                upy = getPointerCoords(event)[1];//event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                upx = getPointerCoords(event)[0];//event.getX();
                upy = getPointerCoords(event)[1];//event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    final float[] getPointerCoords(MotionEvent e) {
        final int index = e.getActionIndex();
        final float[] coords = new float[]{e.getX(index), e.getY(index)};
        Matrix matrix = new Matrix();
        getImageMatrix().invert(matrix);
        matrix.postTranslate(getScrollX(), getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    public ArrayList<Path> getUndonePaths() {
        return undonePaths;
    }

    public void setUndonePaths(ArrayList<Path> undonePaths) {
        this.undonePaths = undonePaths;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

}
