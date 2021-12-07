package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION, DBContract.DBEntry.MEMO};

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
        //設定していた時間になってアラーム信号を受信した時
        else
        {
            SampDatabaseHelper helper = new SampDatabaseHelper(context);
            String[] cols = {DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL_FORMER, DBContract.DBEntry.COLUMN_SOUND_LEVEL_LATTER};
            try(SQLiteDatabase db = helper.getReadableDatabase())
            {
                Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2, cols, null,
                        null, null, null, null, null);
                //moveToFirstで、カーソルを検索結果セットの先頭行に移動
                //検索結果が0件の場合、falseが返る
//                アラーム停止までに必要な歩数など必要な情報が記録されていたら
                if (cursor.moveToFirst()){
                    int needStep = Integer.parseInt(cursor.getString(0));
                    int soundLevel = Integer.parseInt(cursor.getString(1));

                    Intent serviveIntent = new Intent(context, SoundService.class);
                    serviveIntent.putExtra("soundLevel", soundLevel);
                    context.startService(serviveIntent);

                    Intent serviceIntent2 =new Intent(context,FootStep.class);
                    serviceIntent2.putExtra("needStep", needStep);
                    context.startService((serviceIntent2));

                }
                else
                {
                    Intent serviveIntent = new Intent(context, SoundService.class);
                    context.startService(serviveIntent);

                    Intent serviceIntent2 =new Intent(context,FootStep.class);
                    context.startService((serviceIntent2));

                }
                String memo = intent.getStringExtra("memo");
                int id = intent.getIntExtra("id",0);
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
            }catch (Exception e)
            {
                Log.e("aaaaa",e.toString());
                Intent serviveIntent = new Intent(context, SoundService.class);
                context.startService(serviveIntent);

                Intent serviceIntent2 =new Intent(context,FootStep.class);
                context.startService((serviceIntent2));


            }
        }
    }
}

