package com.example.alarmclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelShow extends AppCompatActivity {


    private TextView viewTitle;
    private TextView viewContents;
    private Arc arc;
    private int endAngle = 0;
    private int animationPeriod = 2000;
    private ProgressBar bar;




    private SampDatabaseHelper helper = null;
    private int val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelshow);

        // 88%に角度を合わせる
        endAngle = 100*360/100;

        arc = findViewById(R.id.arc);
//
//        Button buttonStart = findViewById(R.id.button_start);
//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                AnimationArc animation = new AnimationArc(arc, endAngle);
                // アニメーションの起動期間を設定
                animation.setDuration(animationPeriod);
                animation.setRepeatCount(Animation.INFINITE);
                arc.startAnimation(animation);

//            }
//        });

//        Button buttonReset = findViewById(R.id.button_reset);
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationArc animation = new AnimationArc(arc, 0);
//                animation.setDuration(0);
//                arc.startAnimation(animation);
//
//            }
//        });

        bar = (ProgressBar)findViewById(R.id.progressBar1);
        bar.setMax(100);
        bar.setProgress(80);
    }







   public void backbtn(View view){

////


        finish();

   }

}







