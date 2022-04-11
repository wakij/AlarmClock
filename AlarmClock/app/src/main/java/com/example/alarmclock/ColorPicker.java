package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ColorPicker extends LinearLayout {

    ImageView left_image;
    ImageView center_image;
    ImageView right_image;
    int BLUE = Color.parseColor("#3E759F");
    int LIGHT_BLUE = Color.parseColor("#803E759F");
    int GREEN = Color.parseColor("#8CAA6A");
    int LIGHT_GREEN = Color.parseColor("#808CAA6A");
    int RED = Color.parseColor("#BD5252");
    int LIGHT_RED = Color.parseColor("#80BD5252");

    float lastDown;
    VelocityTracker mvelocitytracker;
    int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    int mMinimumFlingVelocity;
    int mMaximumFlingVelocity;
    int previousScrollerX;
    float mCurrentScrollOffset = 100;
    float mInitialScrollOffset;
    int mSelectorWidth = 200; //間隔
    int mScrollLimitWidth = mSelectorWidth / 2;
    int content_width = 600;
    int content_height = 120;
    Scroller mFlingScroller;
    Paint red_paint;
    Paint blue_paint;
    Paint green_paint;
    Paint[] paint_list;
    Paint light_red_paint;
    Paint light_blue_paint;
    Paint light_green_paint;
    Paint[] light_paint_list;
    final int SNAP_SCROLL_DURATION = 300;

    int left = 100;
    int top = 0;
    int right = left + 100;
    int bottom = top + 100;

    int center = 1; //中心のカラーの配列番号 0:red 1:blue 2:green


    public ColorPicker(Context context) {
        super(context);
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.test,this,true);


        left_image = findViewById(R.id.left_image);

        center_image = findViewById(R.id.center_image);

        right_image = findViewById(R.id.right_image);

        left_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScrollerX = 0;
                shift(-1);
                mFlingScroller.startScroll(0,0,mSelectorWidth,0,SNAP_SCROLL_DURATION);
            }
        });
        left_image.setAlpha(0f);
        right_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScrollerX = 0;
                shift(1);
                mFlingScroller.startScroll(0,0,-mSelectorWidth,0,SNAP_SCROLL_DURATION);
            }
        });
        right_image.setAlpha(0f);
        center_image.setAlpha(0f);

//        定数の初期化
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;

        mFlingScroller = new Scroller(getContext(), null, true);

        paint_list = new Paint[3];

        red_paint = new Paint();
        red_paint.setStyle(Paint.Style.FILL);
        red_paint.setColor(RED);
        paint_list[0] = red_paint;

        blue_paint = new Paint();
        blue_paint.setStyle(Paint.Style.FILL);
        blue_paint.setColor(BLUE);
        paint_list[1] = blue_paint;

        green_paint = new Paint();
        green_paint.setStyle(Paint.Style.FILL);
        green_paint.setColor(GREEN);
        paint_list[2] = green_paint;

        light_paint_list = new Paint[3];

        light_red_paint = new Paint();
        light_red_paint.setStyle(Paint.Style.FILL);
        light_red_paint.setColor(LIGHT_RED);
        light_paint_list[0] = light_red_paint;

        light_blue_paint = new Paint();
        light_blue_paint.setStyle(Paint.Style.FILL);
        light_blue_paint.setColor(LIGHT_BLUE);
        light_paint_list[1] = light_blue_paint;

        light_green_paint = new Paint();
        light_green_paint.setStyle(Paint.Style.FILL);
        light_green_paint.setColor(LIGHT_GREEN);
        light_paint_list[2] = light_green_paint;

    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mvelocitytracker == null)
        {
            mvelocitytracker = VelocityTracker.obtain();
        }
        mvelocitytracker.addMovement(event);
        int action = event.getActionMasked();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                mInitialScrollOffset = event.getX();
                lastDown = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentMoveX = event.getX();
                int deltaMoveX = (int) (currentMoveX - lastDown);
//                scrollBy(deltaMoveX,0);
//                invalidate();
                lastDown = currentMoveX;
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("Action","hhhhh");
                float offset = event.getX() - mInitialScrollOffset;
                VelocityTracker velocityTracker = mvelocitytracker;
                velocityTracker.computeCurrentVelocity(1000,mMaximumFlingVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity();
//                一定速度以上で指を動かして話した場合はフリングだとみなす
                if (Math.abs(initialVelocity) > mMinimumFlingVelocity)
                {
                    if (initialVelocity > 0)
                    {
                        previousScrollerX = 0;
                        shift(-1);
                        mFlingScroller.startScroll(0,0,mSelectorWidth,0,SNAP_SCROLL_DURATION);
                    }
                    else
                    {
                        previousScrollerX = 0;
                        shift(1);
                        mFlingScroller.startScroll(0,0,-mSelectorWidth,0,SNAP_SCROLL_DURATION);
                    }
                }
                mvelocitytracker.recycle();
                mvelocitytracker = null;
                return true;
            default:
                super.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll()
    {
        Scroller scroller = mFlingScroller;
        scroller.computeScrollOffset();
        int currentScrollerX = scroller.getCurrX();
        int offset = currentScrollerX - scroller.getStartX();
        if (previousScrollerX == 0)
        {
            previousScrollerX = scroller.getStartX();
        }
        scrollBy(currentScrollerX - previousScrollerX,0);
        previousScrollerX = currentScrollerX;
        invalidate();
    }


    void fling(int velocityX)
    {
        previousScrollerX = 0;
        if (velocityX > 0)
        {
            mFlingScroller.fling(0,0,velocityX,0,0,mSelectorWidth,0,0);
        }else
        {
            mFlingScroller.fling(Integer.MAX_VALUE,0,velocityX,0,0,Integer.MAX_VALUE,0,0);
        }
        invalidate();
    }

    Paint getCenterColor()
    {
        return paint_list[center];
    }

//    Paint getLeftColor()
//    {
//        int left = (center - 1) < 0 ? 2:center - 1;
//        return paint_list[left];
//    }
//
//    Paint getRightColor()
//    {
//        int right = (center + 1) > 2 ? 0:center + 1;
//        return paint_list[right];
//    }

    void shift(int direction)
    {
        center += direction;
        if (center < 0)
        {
            center += 3;
        }else if (center > 2)
        {
            center -= 3;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int width = content_width;
        int height = content_height;
        setMeasuredDimension(width,height);
    }

    @Override
    public void scrollBy(int x, int y)
    {
        mCurrentScrollOffset += x;
        if (mCurrentScrollOffset < 0)
        {
            mCurrentScrollOffset += content_width;
        }else if (mCurrentScrollOffset > content_width)
        {
            mCurrentScrollOffset -= content_width;
        }
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        left_image.layout(left,top,left+100,top + 100);
        center_image.layout(left + 200,top,left+100 + 200,top + 100);
        right_image.layout(left + 400,top,left+100 + 400,top + 100);

        float x = mCurrentScrollOffset;

        float y = 60;
        for (int i = 0;i < paint_list.length;i++)
        {
            if (i == center)
            {
                canvas.drawCircle(x,y,60,paint_list[i]);
            }else
            {
                canvas.drawCircle(x, y,50, light_paint_list[i]);
            }

            x += mSelectorWidth;
            if (x < 0)
            {
                x += content_width;
            }else if (x > content_width)
            {
                x -= content_width;
            }
        }
    }
}
