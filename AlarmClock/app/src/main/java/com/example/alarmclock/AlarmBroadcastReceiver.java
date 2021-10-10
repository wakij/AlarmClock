package com.example.alarmclock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            SampDatabaseHelper helper = new SampDatabaseHelper(context);
            String[] cols = {DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL, DBContract.DBEntry.EXPERIENCE};
            try(SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2, cols, null,
                        null, null, null, null, null);
                //moveToFirstで、カーソルを検索結果セットの先頭行に移動
                //検索結果が0件の場合、falseが返る
                if (cursor.moveToFirst()){
                    int needStep = Integer.parseInt(cursor.getString(0));
                    int soundLevel = Integer.parseInt(cursor.getString(1));

                    Intent serviveIntent = new Intent(context, SoundService.class);
                    serviveIntent.putExtra("soundLevel", soundLevel);
                    context.startService(serviveIntent);

                    Intent serviceIntent2 =new Intent(context,FootStep.class);
                    serviceIntent2.putExtra("needStep", needStep);
                    context.startService((serviceIntent2));

                    Intent startActivityIntent = new Intent(context, stopAlarm.class);
                    startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startActivityIntent);
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
            }catch (Exception e)
            {
                Log.e("aaaaa",e.toString());
                Intent serviveIntent = new Intent(context, SoundService.class);
                context.startService(serviveIntent);

                Intent serviceIntent2 =new Intent(context,FootStep.class);
                context.startService((serviceIntent2));

                Intent startActivityIntent = new Intent(context, stopAlarm.class);
                startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startActivityIntent);
            }

        }



//        Intent startActivityIntent = new Intent(context, AlarmStop.class);
//        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startActivityIntent);
    }
}

