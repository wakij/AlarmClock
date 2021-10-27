package com.example.alarmclock;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimationArc extends Animation {

    private Arc arc;

    // 中心座標
    private float centerX;
    private float centerY;

    // アニメーション角度
    private float oldAngle;
    private float endAngle;
    private float initAngle;

    AnimationArc(Arc arc, float endAngle, float initAngle) {
        this.oldAngle = arc.getAngle();
        this.endAngle = endAngle;
        this.arc = arc;
        this.initAngle = initAngle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((endAngle - oldAngle) * interpolatedTime);
        arc.setAngle(angle);
        arc.requestLayout();
        arc.setInitAngle(initAngle);
    }

    public void setEndAngle(float endAngle)
    {
        this.endAngle = endAngle;
    }

    public void setInitAngle(float initAngle)
    {
        this.initAngle = initAngle;
    }
}
