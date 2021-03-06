package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.TimeZone;

//このクラスはアラームの設定に関する処理をまとめたものです
public class AlarmHelper {

    //編集と新規登録
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setAlarm(AlarmManager am, Context context, int hour, int minute, int id, String memo)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        // Calendarを使って現在の時間をミリ秒で取得
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.getTimeInMillis();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        // 現在時刻を取得
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        // 日付の調整
        int diff = calendar.compareTo(nowCalendar);
        if(diff <= 0){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }

        Intent intent = new Intent(context,
                AlarmBroadcastReceiver.class);

        intent.putExtra("memo",memo);
        intent.putExtra("id",id);
        intent.putExtra("hour",hour);
        intent.putExtra("minutes",minute);

        PendingIntent pending = PendingIntent.getBroadcast(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(am != null){
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
            Toast.makeText(context,
                    "Set Alarm ", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setAlarm(AlarmManager am, Context context, String time, int id, String isSwitchOn, String memo)
    {
        if (Boolean.parseBoolean(isSwitchOn))
        {
            String[] hour_minutes = time.split(":");
            int hour = Integer.parseInt(hour_minutes[0]);
            int minutes = Integer.parseInt(hour_minutes[1]);
            setAlarm(am, context, hour, minutes, id, memo);
        }
    }
}
