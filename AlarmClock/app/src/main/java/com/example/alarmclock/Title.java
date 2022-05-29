package com.example.alarmclock;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


//タイトル画面のスクリプト
public class Title extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);
//        2秒後にMainActivity.classに遷移
        new Handler(getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        }, 2000);

//        ナビゲーションバーを消す
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}