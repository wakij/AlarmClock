package com.example.alarmclock;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class spinner_timepicker extends TimePicker {

    public spinner_timepicker(Context context) {
        super(context);
        Log.e("Flag","1");
        setIs24HourView(true);
    }

    //This constructor is conduceted when excuting
    public spinner_timepicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("Flag","2");
        setIs24HourView(true);
    }

    public spinner_timepicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("Flag","3");
        setIs24HourView(true);
    }


}
