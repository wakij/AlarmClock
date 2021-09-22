package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.TimeZone;

public class TextActivity extends AppCompatActivity {

    private int id = 0;
    private Button setbtn = null;
    private Button backbtn = null;
    public static AlarmManager am;
    public PendingIntent pending;
    private TimePicker timePicker = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        setbtn = findViewById(R.id.setbtn);
        backbtn = findViewById(R.id.backbtn);



        //MainActivity(登録されたアラーム一覧が乗っているactivity)からintentを取得
        Intent intent = getIntent();

       //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBContract.DBEntry._ID, 0);

        //intentからidが取得できていればtiemPickerの表記を変更(編集mode)
        if(id>0){
            String time = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TIME);
            String[] hour_minutes = time.split(":");
            timePicker.setHour(Integer.parseInt(String.valueOf(hour_minutes[0])));
            timePicker.setMinute(Integer.parseInt((String.valueOf((hour_minutes[1])))));
        }
    }
    // 「設定」ボタン　タップ時に呼び出されるメソッド
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnReg_onClick(View view) {
        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);
        // 入力欄に入力されたタイトルとコンテンツを取得
        String time = Integer.valueOf(timePicker.getHour()).toString()+":"+Integer.valueOf(timePicker.getMinute()).toString();
        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_TIME, time);

            //新規登録mode
            if(id == 0) {
                // データ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME, null, cv);
//                登録したデータのidを取得
                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME};
                Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[] {time}
                        , null, null, null, null);
                if (cursor.moveToFirst())
                {
                    id = cursor.getInt(0);
                }

            } else {
                // データ更新
                db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }

//        alarmに時間を登録するためにCalenderを設定
//        下記のようにしないとjavaの種類によっては9時間ずれる
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        // Calendarを使って現在の時間をミリ秒で取得
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.getTimeInMillis();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.e("time",String.valueOf(calendar.getTimeInMillis()/1000));

// 現在時刻を取得
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        // 比較(確証はないので実際に機能するかは分かりませんが（笑）
        int diff = calendar.compareTo(nowCalendar);

        // 日付を設定
        if(diff <= 0){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }



        //明示的なBroadCast
        Intent intent = new Intent(getApplicationContext(),
                AlarmBroadcastReceiver.class);

        intent.putExtra(DBContract.DBEntry._ID, id);

        pending = PendingIntent.getBroadcast(
                getApplicationContext(), id, intent, 0);

        Log.e("test",String.valueOf(id));

        // アラームをセットする
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(am != null){
//            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 1, pending);
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

    //Alarmをキャンセルする
//    public void cancelAlarm(int id)
//    {
//        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
//        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(),id, intent, 0);
//        pending.cancel();
//        am.cancel(pending);
//    }



}


//メモ
//もう少し楽にデーターベースの検索を行いたい
//取得するのはidまたは時間なので簡単な関数を作れるはず
//あとコードが汚すぎる
//あとデータベースをシングルトン化して楽に呼び出せるようにしたい
