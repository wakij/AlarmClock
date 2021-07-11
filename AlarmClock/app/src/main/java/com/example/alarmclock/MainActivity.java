package com.example.alarmclock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startbtn = this.findViewById(R.id.startAlarm);
        Button setbtn = this.findViewById((R.id.setAlarm));
        TextView text = this.findViewById(R.id.text);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

//                時間をセットする
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 10);

//                明示的なブロードキャスト
                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (am != null){
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                    Toast.makeText(getApplicationContext(), "Set Alarm", Toast.LENGTH_LONG).show();
                }
            }
        });

        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Wakeup.class);
                startActivity(intent);
            }
        });
    }
}