package com.example.alarmclock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context,"Received", Toast.LENGTH_LONG).show();



//        //アプリが再起動されたら
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Log.e("apple", "apple");
                SampDatabaseHelper helper = new SampDatabaseHelper(context);

                // データベースを検索する項目を定義
                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};

                // 読み込みモードでデータベースをオープン
                try (SQLiteDatabase db = helper.getReadableDatabase()) {

                    // データベースを検索
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
                            null, null, null, null, null);

                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (cursor.moveToFirst()) {
                        int id = cursor.getInt(0);
                        String time = cursor.getString(1);
                        String isSwitchOn = cursor.getString(2);
                        AlarmHelper.setAlarm(am, context, time, id, isSwitchOn);
                    }
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        String time = cursor.getString(1);
                        String isSwitchOn = cursor.getString(2);
                        AlarmHelper.setAlarm(am, context, time, id, isSwitchOn);
                    }
                    cursor.close();
                }
            }
        }
        else
        {
            Intent serviveIntent = new Intent(context, SoundService.class);
            context.startService(serviveIntent);

            Intent serviceIntent2 =new Intent(context,FootStep.class);
            context.startService((serviceIntent2));

            Intent startActivityIntent = new Intent(context, stopAlarm.class);
            startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startActivityIntent);
        }



//        Intent startActivityIntent = new Intent(context, AlarmStop.class);
//        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startActivityIntent);
    }
}

