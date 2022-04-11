package com.example.alarmclock;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;


public class test extends AppCompatActivity {
    TextView info_text;
    ImageView left_image;
    ImageView center_image;
    ImageView right_image;
    int BLUE = Color.parseColor("#3E759F");
    int GREEN = Color.parseColor("#8CAA6A");
    int RED = Color.parseColor("#BD5252");
    int[] color_list = {BLUE,GREEN,RED};
    int[] right_image_pos = new int[2];
    int[] center_image_pos = new int[2];
    int[] left_image_pos = new int[2];
    int limitScrollDistance; //これ以上移動したら次に行く
    float lastDown;


    private GestureDetectorCompat mDetector;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.test2);


    }



}
