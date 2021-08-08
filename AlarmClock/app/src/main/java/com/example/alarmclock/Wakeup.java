package com.example.alarmclock;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Wakeup extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setbtn;
    private Button backbtn;
//    private EditText editAlarmName;
    int hour;
    int minutes;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wakeup);

//        hide statusbars
        getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());

        timePicker = findViewById(R.id.time_picker);
        setbtn = findViewById(R.id.setbtn);
//        editAlarmName = findViewById(R.id.alarm_name);
        backbtn = findViewById(R.id.backbtn);



        timePicker.setIs24HourView(true); //24時間表記する

        setbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hour = timePicker.getHour();
                minutes = timePicker.getMinute();
//                String alarmName = editAlarmName.getText().toString();

//                if (alarmName.equals("")){
//                    alarmName = "無題";
//                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
