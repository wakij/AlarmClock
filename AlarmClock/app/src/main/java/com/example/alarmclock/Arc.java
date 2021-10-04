package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.audiofx.LoudnessEnhancer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Arc extends View {

    private final Paint paint;
    private final Paint circlePaint;
    private final Paint circlePaint2;
    private RectF rect;
    private RectF rect2;

    // Animation 開始地点をセット
//    private  float  AngleTarget = 0;
    // 初期 Angle
    private float angle = 0;
    private float sweepAngle = 30f;

    public Arc(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Arcの幅
        int strokeWidth = 70;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        // Arcの色
        paint.setColor(Color.argb(255, 123, 147, 113));
        // Arcの範囲
        rect = new RectF();
        rect2 = new RectF();

        int strokeWidth2 = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.argb(255, 153, 193, 234));
        circlePaint.setStrokeWidth(strokeWidth2);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);

        int strokeWidth3 = 10;
        circlePaint2 = new Paint();
        circlePaint2.setColor(Color.argb(255, 238, 197, 13));
        circlePaint2.setStrokeWidth(strokeWidth2);
        circlePaint2.setAntiAlias(true);
        circlePaint2.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 背景、半透明
        canvas.drawColor(Color.argb(64, 255, 255, 255));

        // Canvas 中心点
        float x = getWidth()/2;
        float y = getHeight()/2;
        // 半径
        float radius = getWidth() * 0.45f;

        // 円弧の領域設定
        rect.set(x - radius, y - radius, x + radius, y + radius);
        rect2.set(x - radius - 120, y - radius - 120, x + radius + 120, y + radius + 120);


        // 円弧の描画
//        canvas.drawArc(rect, AngleTarget, angle, false, paint);
//        canvas.drawArc(rect, 0, 90, false, paint);
        canvas.drawArc(rect, angle, sweepAngle, false, paint);
        canvas.drawArc(rect2, -angle, sweepAngle, false, paint);

        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(x, y, radius + 50 + 50, circlePaint2);
        canvas.drawCircle(x, y, radius - 50 + 10, circlePaint);
    }

    // AnimationAへ現在のangleを返す
    public float getAngle() {
        return angle;
    }

    // AnimationAから新しいangleが設定される
    public void setAngle(float angle) {
        this.angle = angle;

    }
}

