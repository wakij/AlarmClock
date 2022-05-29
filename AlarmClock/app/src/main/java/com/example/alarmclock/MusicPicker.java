package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MusicPicker extends LinearLayout {

    float lastDown;
    VelocityTracker mvelocitytracker;
    int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    int mMinimumFlingVelocity;
    int mMaximumFlingVelocity;
    int previousScrollerX;

    int content_width;
    int content_height;
    int mSelectorWidth; //間隔
    float mCurrentScrollOffset;
    float mInitialScrollOffset;


    Scroller mFlingScroller;
    final int SNAP_SCROLL_DURATION = 300;

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

        LinearLayout music_layout = findViewById(R.id.music_container);

        music_layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                music_layout.getViewTreeObserver().removeOnPreDrawListener(this);
                content_width = music_layout.getWidth();
                content_height = content_width / 4;
                if (content_height > music_layout.getHeight())
                {
                    content_height = music_layout.getHeight();
                }
                ViewGroup.LayoutParams new_music_layout =  music_layout.getLayoutParams();
                new_music_layout.height = content_height;
                music_layout.setLayoutParams(new_music_layout);
                mSelectorWidth = content_width / 3; //間隔
                mCurrentScrollOffset = content_width / 6;
                int childCount = music_layout.getChildCount();
                for(int i=0;i < childCount;i++)
                {
                    ViewGroup.LayoutParams child_layout = music_layout.getChildAt(i).getLayoutParams();
                    child_layout.height = content_height;
                    child_layout.width = content_height;
                    music_layout.getChildAt(i).setLayoutParams(child_layout);
                }

                return false;
            }
        });
        ImageView left_audio = new ImageView(getContext());
        ImageView center_audio = new ImageView(getContext());
        ImageView right_audio = new ImageView(getContext());

        left_audio.setImageResource(R.drawable.red_note);
        center_audio.setImageResource(R.drawable.blue_note);
        right_audio.setImageResource(R.drawable.yellow_note);

        music_layout.addView(left_audio);
        music_layout.addView(center_audio);
        music_layout.addView(right_audio);


        audio_list = new ImageView[]{left_audio,center_audio,right_audio};


        left_audio.setOnClickListener(v -> {
            previousScrollerX = 0;
            int shift_num = getNeedShiftNum(getPos(LEFT_AUDIO));
            mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
            left_shift(shift_num);
        });

        center_audio.setOnClickListener(v -> {
            previousScrollerX = 0;
            int shift_num = getNeedShiftNum(getPos(CENTER_AUDIO));
            mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
            left_shift(shift_num);
        });

        right_audio.setOnClickListener(v -> {
            previousScrollerX = 0;
            int shift_num = getNeedShiftNum(getPos(RIGHT_AUDIO));
            mFlingScroller.startScroll(0,0,-mSelectorWidth * shift_num,0,SNAP_SCROLL_DURATION);
            left_shift(shift_num);
        });


//        定数の初期化
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;
        mFlingScroller = new Scroller(getContext(), null, true);
        invalidate();
    }

    public MusicPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                lastDown = currentMoveX;
                return true;
            case MotionEvent.ACTION_UP:
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
        if (previousScrollerX == 0)
        {
            previousScrollerX = scroller.getStartX();
        }
        scrollBy(currentScrollerX - previousScrollerX,0);
        previousScrollerX = currentScrollerX;
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

//    初期状態で一番左にあったものを取得
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
        int y = content_height / 2;
        ImageView left_audio_image = getLeftAudio();

        for(ImageView audio_image:audio_list)
        {
            if (audio_image == left_audio_image)
            {
                audio_image.layout(x - content_height/2,y - content_height/2,x + content_height/2,y + content_height/2);
                audio_image.setAlpha(1.0f);
            }else
            {
                audio_image.layout(x - (content_height/2 - 10),y - (content_height/2 - 10),x + (content_height/2 - 10),y + (content_height/2 - 10));
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
    }
}
