package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.media.audiofx.LoudnessEnhancer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Arc extends View {

    private final Paint paint;
//    private final Paint paint2;
    private final Paint outercirclePaint;
    private final Paint innercirclePaint;
    private final Paint arcTip;
    private final Paint arcEnd;
    private RectF rect;
    private RectF rect2;
    private float initAngle = 0;
    private float constInitAngle = 0;
    private float constEndAngle;

    // Animation 開始地点をセット
//    private  float  AngleTarget = 0;
    // 初期 Angle
    private float angle = 0;
    private float sweepAngle = 50f;
    private float sweepAngle2=70f;

    public Arc(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Arcの幅
        paint = new Paint();
//        paint2= new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        paint.setStrokeCap(Paint.Cap.ROUND); //先端を丸める



        // Arcの色
        paint.setColor(Color.argb(255,37,90,199));

        // Arcの範囲
        rect = new RectF();
        rect2 = new RectF();

        int strokeWidth2 = 10;
        outercirclePaint = new Paint();
        outercirclePaint.setColor(Color.argb(255,25,24,46));
        outercirclePaint.setStrokeWidth(strokeWidth2);
        outercirclePaint.setAntiAlias(true);

        int strokeWidth3 = 10;
        innercirclePaint = new Paint();
        innercirclePaint.setColor(Color.argb(255, 28, 27, 56));
        innercirclePaint.setStrokeWidth(strokeWidth3);
        innercirclePaint.setAntiAlias(true);

        arcTip = new Paint();
        arcTip.setColor(Color.parseColor("#193498"));

        arcEnd = new Paint();
        arcEnd.setColor(Color.parseColor("#1597E5"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        // 背景を描画
        canvas.drawColor(Color.argb(255, 28, 27, 56));

        // Canvas 中心点
        float x = getWidth()/2;
        float y = getHeight()/2;

//        上から始まるように90度回転
        canvas.rotate(-90,x,y);
//        グラデーション用のshaderを制作(詳しくはdoc参照、グラデーションに用いる色や割合を決めることが可能※現在は色しかしていしないので均等に変化するようになっています)
        SweepGradient circleGradient = new SweepGradient(x, y, Color.parseColor("#193498"),Color.parseColor("#1597E5"));
        paint.setShader(circleGradient);

        // 円弧の領域設定
        rect.set(x - 400, y - 400, x + 400, y + 400);
        rect2.set(x - 400, y - 400, x + 400, y + 400);

        canvas.drawCircle(x, y, 450, outercirclePaint); //外円
        canvas.drawCircle(x, y, 350, innercirclePaint); //内円
        canvas.drawArc(rect2, constInitAngle, constEndAngle, false, paint); //初期に描画する円弧
        canvas.drawArc(rect, initAngle, angle, false, paint); //新たに描画する円弧
        angle = (float)Math.toRadians(angle + constEndAngle);
        //progressbarの後端部分
        canvas.drawCircle(x + 400, y, 50, arcTip);
        //progressbarの先端部分
//        canvas.drawCircle(x + (float)Math.cos(angle) * 400, y + (float)Math.sin(angle) * 400, 50, arcTip);


        canvas.restore();
    }

    // AnimationAへ現在のangleを返す
    public float getAngle() {
        return angle;
    }

    // AnimationAから新しいangleが設定される
    public void setAngle(float angle) {
        this.angle = angle;

    }

    public void setInitAngle(float angle)
    {
        this.initAngle = angle;
    }

    public void setconstEndAngle(float angle)
    {
        this.constEndAngle = angle;
    }
}

