package com.example.alarmclock;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Wakeup extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setbtn;
    private EditText editAlarmName;
    int hour;
    int minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wakeup);

        timePicker = findViewById(R.id.time_picker);
        setbtn = findViewById(R.id.setbtn);
        editAlarmName = findViewById(R.id.alarm_name);

        setbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                hour = timePicker.getHour();
                minutes = timePicker.getMinute();
                String alarmName = editAlarmName.getText().toString();
                if (alarmName.equals("")){
                    alarmName = "無題";
                }
            }
        });
    }
}
