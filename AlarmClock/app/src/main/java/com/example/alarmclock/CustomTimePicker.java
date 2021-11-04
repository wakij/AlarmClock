package com.example.alarmclock;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class CustomTimePicker extends RelativeLayout {
    private Context mContext;

    public CustomTimePicker(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_time_picker, this, true);
    }

    public CustomTimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_time_picker, this, true);
    }

    public CustomTimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //change selec background
    public void setBackgroundColor(Color color)
    {
        ImageView timePickerbg = findViewById(R.id.select_bg);
    }


}
