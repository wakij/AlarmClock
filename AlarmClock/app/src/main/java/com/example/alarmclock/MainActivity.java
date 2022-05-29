package com.example.alarmclock;


import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper = null;
    private Dialog dialog;
    private int nowpos;
    private int count;
    private int positivecount=0;
    private Handler handler;
    private ConstraintLayout main_background;
    private ImageView alarmlist_tab;
    private ImageView setting_tab;



    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        int needfootstep = sharedPreferences.getInt("needfootstep", 0);

        main_background = findViewById(R.id.main_background);

//        Debug用
//        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
//        editor.commit();


        if (needfootstep == 0) {
            //Create the Dialog here
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_layout);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false); //Optional
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
            dialog.show();

            Button Okay = dialog.findViewById(R.id.btn_okay);
            Button Cancel = dialog.findViewById(R.id.btn_cancel);

            Okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowpos++;
                    changeDialog(nowpos);
                    positivecount++;
                }
            });

            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowpos++;
                    changeDialog(nowpos);
                }
            });
        }
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                updateAlarmListScene();
            }
        };
        AlarmBroadcastReceiver.registerHandler(handler);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
        fragmentTransaction.commit();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        ImageButton fab_btn = findViewById(R.id.fab_reg);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AlarmSetScene.class);
                startActivity(intent);
            }
        });

        alarmlist_tab = findViewById(R.id.alarmlist_tab);
        alarmlist_tab.setColorFilter(getResources().getColor(R.color.selectedcolor));
        alarmlist_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmlist_tab.setColorFilter(getResources().getColor(R.color.selectedcolor));
                setting_tab.clearColorFilter();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
                fragmentTransaction.commit();

                main_background.setBackgroundColor(getResources().getColor(R.color.alarmlist_background));
            }
        });

        setting_tab = findViewById(R.id.setting_tab);
        setting_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_tab.setColorFilter(getResources().getColor(R.color.selectedcolor));
                alarmlist_tab.clearColorFilter();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.settingsContainer, new Personal_Info());
                fragmentTransaction.commit();

                main_background.setBackgroundColor(getResources().getColor(R.color.personal_info_background));
            }
        });
    }

    public void updateAlarmListScene()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("MainActivity","破壊されました");
    }


    @Override
    public void onPause()
    {
        super.onPause();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Log.e("MainActivity","再開されました");
    }


    private void changeDialog(int pos)
    {
        TextView question_text = dialog.findViewById(R.id.textView2);
        switch (pos)
        {
            case 1:
                question_text.setText("朝起きるのはお得意ですか？");
                break;
            case 2:
                question_text.setText("夜更かししがちですか？");
                break;
            case 3:
                question_text.setText("自分に厳しくしたいですか？");
                break;
            case 4:
                writeQResult();
                dialog.dismiss();
        }
    }

//    アンケートの結果をデータベースに書き込む
    private void writeQResult()
    {
        switch (positivecount){
            case 0:
                count=50;
                break;
            case 1:
                count=80;
                break;
            case 2:
                count=90;
                break;
            case 3:
                count=100;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("needfootstep",count);
        editor.apply();
    }
}