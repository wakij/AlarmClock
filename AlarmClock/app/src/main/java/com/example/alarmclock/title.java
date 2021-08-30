package com.example.alarmclock;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

//タイトル画面のスクリプト
public class title extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);

        getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());

        String DB_FULL_PATH = "data/data/" + getPackageName() + "/myDatabase1.db";
        ImageButton title =  findViewById(R.id.imageButton2);

        title.setOnClickListener(v -> {
            try {
                SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH,null,SQLiteDatabase.OPEN_READONLY);
                checkDB.close();
                Intent intent= new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            } catch (Exception e){
                Intent intent = new Intent(getApplication(),Explaination.class);
                startActivity(intent);
            }

        });
    }
}
