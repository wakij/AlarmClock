package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

    Paint paint;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 背景、半透明
        canvas.drawColor(Color.argb(127, 0, 127, 63));

        // 円
        paint.setColor(Color.argb(255, 68, 255, 255));
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(450, 450, 100, paint);

        
    }
}
