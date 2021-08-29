package com.example.alarmclock;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
import com.example.alarmclock.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

//タイトル画面のスクリプト
public class title extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);

        MainActivity main = new MainActivity();

//        getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());

        ImageButton title =  findViewById(R.id.imageButton2);

        title.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(),Explaination.class);
            startActivity(intent);

        });
    }
}
