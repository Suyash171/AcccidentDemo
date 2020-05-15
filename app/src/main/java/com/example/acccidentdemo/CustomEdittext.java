package com.example.acccidentdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CustomEdittext extends androidx.appcompat.widget.AppCompatEditText {

    Paint paint;

    public CustomEdittext(Context context) {
        super(context);
        init();
    }

    public CustomEdittext(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }
}
