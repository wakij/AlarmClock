package com.example.alarmclock;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//タイトル画面のスクリプト
public class title extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);

        View decor = getWindow().getDecorView();
        // hide navigation bar, hide status bar
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        String DB_FULL_PATH = "data/data/" + getPackageName() + "/myDatabase1.db";
        Button titlebtn =  findViewById(R.id.titlebtn);

        titlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH,null,SQLiteDatabase.OPEN_READONLY);
                    checkDB.close();
                    Intent intent= new Intent(getApplication(),MainActivity.class);
                    startActivity(intent);
                } catch (Exception e){
                    Intent intent = new Intent(getApplication(),Explaination.class);
                    startActivity(intent);
                }

            }
        });
    }
}
