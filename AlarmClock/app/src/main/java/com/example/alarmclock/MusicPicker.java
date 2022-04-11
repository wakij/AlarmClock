package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
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

public class MusicPicker extends LinearLayout {
    ImageView left_audio;
    ImageView center_audio;
    ImageView right_audio;

//    int BLUE = Color.parseColor("#3E759F");
//    int LIGHT_BLUE = Color.parseColor("#803E759F");
//    int GREEN = Color.parseColor("#8CAA6A");
//    int LIGHT_GREEN = Color.parseColor("#808CAA6A");
//    int RED = Color.parseColor("#BD5252");
//    int LIGHT_RED = Color.parseColor("#80BD5252");

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
    int image_width = 100;
    int image_height = 100;

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

    int[] pos; //[0]:left_audio [1]:center_audio [2]:right_audio 0:左 1:中心 2:右
    final int LEFT_AUDIO = 0;
    final int CENTER_AUDIO = 1;
    final int RIGHT_AUDIO = 2;

    ImageView[] audio_list;


    public MusicPicker(Context context) {
        super(context);
    }

    public MusicPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.music_picker,this,true);

        pos = new int[]{0,1,2};

        left_audio = findViewById(R.id.left_audio);

        center_audio = findViewById(R.id.center_audio);

        right_audio = findViewById(R.id.right_audio);

        audio_list = new ImageView[]{left_audio,center_audio,right_audio};


        left_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScrollerX = 0;
                int shift_num = getNeedShiftNum(getPos(LEFT_AUDIO));
                Log.e("shift_num",String.valueOf(shift_num));
                mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
                left_shift(shift_num);
            }
        });

        center_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScrollerX = 0;
                int shift_num = getNeedShiftNum(getPos(CENTER_AUDIO));
                mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
                left_shift(shift_num);
            }
        });

        right_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousScrollerX = 0;
                int shift_num = getNeedShiftNum(getPos(RIGHT_AUDIO));
                mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
                left_shift(shift_num);
            }
        });


//        定数の初期化
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;

        mFlingScroller = new Scroller(getContext(), null, true);

//        paint_list = new Paint[3];

//        red_paint = new Paint();
//        red_paint.setStyle(Paint.Style.FILL);
//        red_paint.setColor(RED);
//        paint_list[0] = red_paint;

//        blue_paint = new Paint();
//        blue_paint.setStyle(Paint.Style.FILL);
//        blue_paint.setColor(BLUE);
//        paint_list[1] = blue_paint;

//        green_paint = new Paint();
//        green_paint.setStyle(Paint.Style.FILL);
//        green_paint.setColor(GREEN);
//        paint_list[2] = green_paint;

//        light_paint_list = new Paint[3];
//
//        light_red_paint = new Paint();
//        light_red_paint.setStyle(Paint.Style.FILL);
//        light_red_paint.setColor(LIGHT_RED);
//        light_paint_list[0] = light_red_paint;
//
//        light_blue_paint = new Paint();
//        light_blue_paint.setStyle(Paint.Style.FILL);
//        light_blue_paint.setColor(LIGHT_BLUE);
//        light_paint_list[1] = light_blue_paint;
//
//        light_green_paint = new Paint();
//        light_green_paint.setStyle(Paint.Style.FILL);
//        light_green_paint.setColor(LIGHT_GREEN);
//        light_paint_list[2] = light_green_paint;
    }

    public MusicPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

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
                        mFlingScroller.startScroll(0,0,mSelectorWidth,0,SNAP_SCROLL_DURATION);
                        right_shift(1);
                    }
                    else
                    {
                        previousScrollerX = 0;
                        mFlingScroller.startScroll(0,0,-mSelectorWidth,0,SNAP_SCROLL_DURATION);
                        left_shift(1);
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


//    指定された回数左にシフトする。
    void left_shift(int count)
    {
        for (int p = 0;p<count;p++)
        {
            for (int i=0;i<pos.length;i++)
            {
                if (pos[i] - 1 >= 0)
                {
                    pos[i] = pos[i] -1;
                }else
                {
                    pos[i] = pos[i] - 1 + 3;
                }
            }
        }
    }

//    指定された回数右にシフトする
    void right_shift(int count)
    {
        for (int p = 0;p<count;p++)
        {
            for (int i=0;i<pos.length;i++)
            {
                if (pos[i] + 1 < 3)
                {
                    pos[i] = pos[i] + 1;
                }else
                {
                    pos[i] = pos[i] + 1 - 3;
                }
            }
        }
    }

    int getPos(int audio)
    {
        switch (audio)
        {
            case LEFT_AUDIO:
                return pos[0];
            case CENTER_AUDIO:
                return pos[1];
            case RIGHT_AUDIO:
                return pos[2];
        }
        return 100;
    }

//    ポジションに応じてshift関数の実行回数を決める
    int getNeedShiftNum(int pos)
    {
        switch (pos)
        {
            case 1:
                return 1;
            case 2:
                return 2;
        }
        return 0;
    }

    ImageView getLeftAudio()
    {
        for (int i=0;i<pos.length;i++)
        {
            if (pos[i] == 0)
            {
                return audio_list[i];
            }
        }
        return null;
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

        int x = (int) mCurrentScrollOffset;
        int y = 60;
        ImageView left_audio_image = getLeftAudio();

        for(ImageView audio_image:audio_list)
        {
            if (audio_image == left_audio_image)
            {
                audio_image.layout(x - 60,y - 60,x + 60,y + 60);
                audio_image.setAlpha(1.0f);
            }else
            {
                audio_image.layout(x - 50,y - 50,x + 50,y + 50);
                audio_image.setAlpha(0.5f);
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

//        left_audio.layout(x - 50,y - 50,x + 50,y + 50);
//        left_audio.layout(x - 50,y - 50,x + 50,y + 50);
//        x += mSelectorWidth;
//        if (x < 0)
//        {
//            x += content_width;
//        }else if (x > content_width)
//        {
//            x -= content_width;
//        }
//
//        center_audio.layout(x - 50,y - 50,x + 50,y + 50);
//        x += mSelectorWidth;
//        if (x < 0)
//        {
//            x += content_width;
//        }else if (x > content_width)
//        {
//            x -= content_width;
//        }
//        right_audio.layout(x - 50,y - 50,x + 50,y + 50);

    }
}
