package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TextActivity extends AppCompatActivity {

    private int id = 0;

    private Button setbtn = null;

    private Button backbtn = null;
    public AlarmManager am;
    public PendingIntent pending;





    private TimePicker timePicker = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        // ビューオブジェクトを取得

        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        setbtn = findViewById(R.id.setbtn);
//
        backbtn = findViewById(R.id.backbtn);



            // インテントを取得
            Intent intent = getIntent();
//
//        //intentのデータを取得(データがない場合、第２引数の 0 が返る)
            id = intent.getIntExtra(DBContract.DBEntry._ID, 0);

            if(id!=0){
            String time = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TIME);

            String[] hour_minutes = time.split(":");
//
//
            // データ更新の場合
            if (id > 0) {
                timePicker.setHour(Integer.parseInt(String.valueOf(hour_minutes[0])));
                timePicker.setMinute(Integer.parseInt((String.valueOf((hour_minutes[1])))));
//
            }


        }
    }
        // 「設定」ボタン　タップ時に呼び出されるメソッド
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);



        // 入力欄に入力されたタイトルとコンテンツを取得
        String time    = Integer.valueOf(timePicker.getHour()).toString()+":"+Integer.valueOf(timePicker.getMinute()).toString();

        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_TIME, time);


            if(id == 0) {
                // データ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME, null, cv);
            } else {
                // データ更新
                db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }

        Calendar calendar = Calendar.getInstance();
        // Calendarを使って現在の時間をミリ秒で取得
        calendar.setTimeInMillis(System.currentTimeMillis());

        long time1 = calendar.getTimeInMillis();
        // 5秒後に設定
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());

        long time2 = calendar.getTimeInMillis();
        Log.e("test",String.valueOf((time2 - time1) / 1000));



        //明示的なBroadCast
        Intent intent = new Intent(getApplicationContext(),
                AlarmBroadcastReceiver.class);
        pending = PendingIntent.getBroadcast(
                getApplicationContext(), id, intent, 0);



        // アラームをセットする
        am = (AlarmManager) getSystemService(ALARM_SERVICE);






        if(am != null){
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
//            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    1000 * 60 * 1, pending);

            Toast.makeText(getApplicationContext(),
                    "Set Alarm ", Toast.LENGTH_SHORT).show();
        }


    finish();



    }

    // 「戻る」ボタン　タップ時に呼び出されるメソッド
    public void onBack(View view) {

        // TextActivityを終了
        finish();
    }



}




