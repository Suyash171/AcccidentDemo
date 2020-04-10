package com.example.acccidentdemo.recoderdb;
/**
 * THIS CLASS WILL HELP TO CREATE PROGRESS BAR 4 LEVEL.YOU CAN USE 2 COLOR FOR DISPLAYING THE SOME 2 STATE.
 * PROGRESS BAR.eg progress bar use to display sound level exceeding the specific range.
 * <p>
 * <h>USE mileStone PROPERTY OR setMileStone METHOD  TO SET THE BENCHMARK FOR COLOR CHANGE</h>
 * USE isSingleColor PROPERTY or setSingleColor METHOD TO ENABLE OR DISABLE DUAL COLOR FOR PROGRESS BAR
 * USE nFirstColor PROPERTY or setnFirstColor METHOD TO SET COLOR TO PROGRESS BAR REACHING BEFORE IT REACHING TO MILESTONE
 * USE nSecondColor PROPERTY ot setnSecondColor METHOD TO SET COLOR TO PROGRESS BAR AFTER IT CROSSING THE MILESTONE
 *
 * @author Ankit Polekar.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.example.acccidentdemo.R;

public class LevelProgressBar extends ProgressBar {
    Paint paint;
    private int progress;
    private int nSecondColor;
    private int nFirstColor;
    private float barThickness;
    private int unfilledSectionColor;
    private int mileStone;
    private int nSlot;
    private Paint whitePaint;
    private Paint grayPaint;
    private boolean isSingleColor;
    private int nBackGroundColor;
    private int nHorizontalSpace;

    public LevelProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public LevelProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LevelProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setSingleColor(boolean singleColor) {
        isSingleColor = singleColor;
    }

    public void setnSecondColor(int nSecondColor) {
        this.nSecondColor = nSecondColor;
    }

    public void setnFirstColor(int nFirstColor) {
        this.nFirstColor = nFirstColor;
    }

    public void setBarThickness(float barThickness) {
        this.barThickness = barThickness;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LevelProgressBar, defStyle, 0);

        barThickness = a.getFloat(R.styleable.LevelProgressBar_barThickness, 60);
        unfilledSectionColor = Color.parseColor("#34c5bb");
        nSecondColor = a.getColor(R.styleable.LevelProgressBar_secondColor, Color.RED);
        nFirstColor = a.getColor(R.styleable.LevelProgressBar_firstColor, Color.GREEN);
        isSingleColor = a.getBoolean(R.styleable.LevelProgressBar_singleColor, false);
        mileStone = a.getInt(R.styleable.LevelProgressBar_mileStone, getMax());
        //nHorizontalSpace = a.getInt(R.styleable.LevelProgressBar_horizontalSpace, 2);
        nHorizontalSpace = 0;


        paint = new Paint();
        whitePaint = new Paint();
        grayPaint = new Paint();


        ColorDrawable colorDrawable = (ColorDrawable) this.getBackground();
        nBackGroundColor = colorDrawable != null ? colorDrawable.getColor() : Color.WHITE;

        grayPaint.setColor(Color.GRAY);
        whitePaint.setStrokeWidth(barThickness);
        grayPaint.setStrokeWidth(barThickness);

        setProgressDrawable(null);

        a.recycle();
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.TRANSPARENT);
//        whitePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        whitePaint.setAntiAlias(true);

        int halfHeight = getHeight() / 2;
        int progressEndX = (int) (getWidth() * getProgress() / 100f);
//        int progressStop = (int) ((getWidth() * getMax()) / 100f);
        int progressStop = (int) ((getWidth() * getMax()) / 100f);

        nSlot = getMax() / 4;

        int firstStop = (int) ((getWidth() * nSlot) / 100f);
//        int secondStop = (int) ((getWidth() * (nSlot * 2)) / 100f);
        int secondStop = firstStop * 2;
//        int thirdStop = (int) ((getWidth() * (nSlot * 3)) / 100f);
        int thirdStop = firstStop * 3;
//        int lastStop = (int) ((getWidth() * (nSlot * 4)) / 100f);
        int lastStop = firstStop * 4;

        progress = getProgress();
        // draw the filled portion of the bar
        paint.setStrokeWidth(barThickness);

        int color = nFirstColor;
        if (!isSingleColor)
            color = (progress >= mileStone) ? nSecondColor : nFirstColor;

        paint.setColor(color);

        //BACK GROUND OF PROGRESS BAR
        canvas.drawLine(nHorizontalSpace, halfHeight, firstStop, halfHeight, grayPaint);

        //HERE DRAW FIRST SECTION OF PROGRESS BAR
        if (progress > nSlot)
            canvas.drawLine(nHorizontalSpace, halfHeight, firstStop, halfHeight, paint);
        else {
            canvas.drawLine(nHorizontalSpace, halfHeight, progressEndX, halfHeight, paint);
        }


        //DRAW SECOND SECTION OF PROGRESS BAR
        canvas.drawLine(firstStop + nHorizontalSpace, halfHeight, secondStop, halfHeight, grayPaint);
        if (progress < (nSlot * 2) && progress > nSlot)
            canvas.drawLine(firstStop + nHorizontalSpace, halfHeight, progressEndX, halfHeight, paint);
        else if (progress > nSlot) {
            canvas.drawLine(firstStop + nHorizontalSpace, halfHeight, secondStop, halfHeight, paint);
        }


        //DRAW THIRD SECTION OF PROGRESS BAR
        canvas.drawLine(secondStop + nHorizontalSpace, halfHeight, thirdStop, halfHeight, grayPaint);
        if (progress < (nSlot * 3) && (progress > (nSlot * 2)))
            canvas.drawLine(secondStop + nHorizontalSpace, halfHeight, progressEndX, halfHeight, paint);
        else if (progress > nSlot * 3)
            canvas.drawLine(secondStop + nHorizontalSpace, halfHeight, thirdStop, halfHeight, paint);


        //DRAW FOURTH SECTION OF PROGRESS BAR
        canvas.drawLine(thirdStop + nHorizontalSpace, halfHeight, lastStop, halfHeight, grayPaint);
        if (progress > (nSlot * 3))
            canvas.drawLine(thirdStop + nHorizontalSpace, halfHeight, progressStop, halfHeight, paint);
    }

    public void setMileStone(int mileStone) {
        this.mileStone = mileStone;
    }

    public void setnHorizontalSpace(int nHorizontalSpace) {
        this.nHorizontalSpace = nHorizontalSpace;
    }
}
