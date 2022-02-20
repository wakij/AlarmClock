package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;



//アプリが再起動された時に再設定を行うためのクラス
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static Handler handler;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context,"Received", Toast.LENGTH_LONG).show();

//        //アプリが再起動されたら
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                DatabaseHelper helper = new DatabaseHelper(context);

                // データベースを検索する項目を定義
                String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION, DBDef.DBEntry.MEMO};

                // 読み込みモードでデータベースをオープン
                try (SQLiteDatabase db = helper.getReadableDatabase()) {

                    // データベースを検索
                    Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, null,
                            null, null, null, null, null);

                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (cursor.moveToFirst()) {
                        int id = cursor.getInt(0);
                        String time = cursor.getString(1);
                        String isSwitchOn = cursor.getString(2);
                        String memo = cursor.getString(3);
                        AlarmHelper.setAlarm(am, context, time, id, isSwitchOn, memo);
                    }
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        String time = cursor.getString(1);
                        String isSwitchOn = cursor.getString(2);
                        String memo = cursor.getString(3);
                        AlarmHelper.setAlarm(am, context, time, id, isSwitchOn, memo);
                    }
                    cursor.close();
                }
            }
        }
        //設定していた時間になってアラーム信号を受信した時 Note: actionを設定しておかないと予期せぬ事態になる可能性がある
        else
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Info",Context.MODE_PRIVATE);
            int needfootstep = sharedPreferences.getInt("needfootstep",0);
            int hour = intent.getIntExtra("hour",0);
            int minutes = intent.getIntExtra("minutes",0);
            Intent soundServiceIntent = new Intent(context, SoundService.class);
            soundServiceIntent.setAction("MusicPlay");
            context.startService(soundServiceIntent);
            Intent footstepServiceIntent =new Intent(context, FootStepService.class);
            footstepServiceIntent.putExtra("hour",hour);
            footstepServiceIntent.putExtra("minutes",minutes);
            footstepServiceIntent.putExtra("needfootstep",needfootstep);
            context.startService((footstepServiceIntent));

                String memo = intent.getStringExtra("memo");
                int id = intent.getIntExtra("id",0);
                DatabaseHelper helper = new DatabaseHelper(context);
                //アラームがなったらスイッチをoffにする処理
                try(SQLiteDatabase db = helper.getWritableDatabase())
                {
                    ContentValues cv = new ContentValues();
                    cv.put(DBDef.DBEntry.SWITCH_CONDITION, "false");
                    db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                }
                if (handler != null)
                {
                    Message msg = new Message();
                    handler.sendMessage(msg);
                }
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("default","おはようございます",NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(memo);
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); //ロック画面に表示
                    if (notificationManager != null)
                    {
                        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationManager.createNotificationChannel(channel);
                        Notification notification = new Notification.Builder(context, "default")
                                .setContentTitle("おはようございます")
                                // android標準アイコンから
                                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                                .setContentText(memo)
                                .setAutoCancel(true)
                                .setContentIntent(notifyPendingIntent)
                                .setWhen(System.currentTimeMillis())
                                .build();

                        notificationManager.notify(R.string.app_name, notification);
                    }
                }
        }
    }

//    handlerを設定
    public static void registerHandler(Handler updateHandler)
    {
        handler = updateHandler;
    }
}

