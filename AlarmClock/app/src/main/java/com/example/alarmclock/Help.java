package com.example.alarmclock;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Help extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsContainer, new help_rows());
        fragmentTransaction.commit();

        ImageView bar_back_btn = findViewById(R.id.bar_back_button);
        bar_back_btn.setColorFilter(Color.parseColor("#FFFFFF"));
        bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView bar_title = findViewById(R.id.bar_title);
                bar_title.setText("ヘルプ");
                ImageView bar_back_btn = findViewById(R.id.bar_back_button);
                bar_back_btn.setVisibility(View.INVISIBLE);
                fragmentManager.popBackStack();
            }
        });
    }
}
