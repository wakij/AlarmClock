package com.example.alarmclock;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

//役割
//ボタンの提供とデータの管理
public class Main extends FragmentActivity {
    private ViewPager2 pager;
    private SceneAdapter scene_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scene);
        pager = findViewById(R.id.pager);
        scene_adapter = new SceneAdapter(this);
        pager.setAdapter(scene_adapter);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state)
            {}
            @Override
            public void onPageScrolled(int position, float positionOffset, int PxpositionOffsetPixels)
            {}
            @Override
            public void onPageSelected(int position){
            }

        });

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        ImageView setAlarmBtn = findViewById(R.id.setAlarmButton);
        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0,false);
            }
        });

        ImageView alarmInfoBtn = findViewById(R.id.statusButton);
        alarmInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1,false);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpButton);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2,false);
            }
        });

    }
}
