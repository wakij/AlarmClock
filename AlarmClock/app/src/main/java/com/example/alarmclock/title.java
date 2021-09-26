package com.example.alarmclock;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//タイトル画面のスクリプト
public class title extends AppCompatActivity {





    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);

        getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());


        ImageButton b = findViewById(R.id.imageButton2);




                b.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);

                });




    }
}