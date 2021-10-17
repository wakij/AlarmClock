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
    private TimePicker timePicker = null;
    private EditText editContents;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        setbtn = findViewById(R.id.setbtn);
        backbtn = findViewById(R.id.backbtn);
        editContents=findViewById(R.id.editContents);




        //MainActivity(登録されたアラーム一覧が乗っているactivity)からintentを取得
        Intent intent = getIntent();

       //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBContract.DBEntry._ID, 0);

        //intentからidが取得できていればtiemPickerの表記を変更(編集mode)
        if(id>0){

            String time = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TIME);
            String memo = intent.getStringExtra(DBContract.DBEntry.MEMO);
            String[] hour_minutes = time.split(":");
            timePicker.setHour(Integer.parseInt(String.valueOf(hour_minutes[0])));
            timePicker.setMinute(Integer.parseInt((String.valueOf((hour_minutes[1])))));
            editContents.setText(memo);


        }






    }
    // 「設定」ボタン　タップ時に呼び出されるメソッド
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnReg_onClick(View view) {
        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);
        // 入力欄に入力されたタイトルとコンテンツを取得
        String time = Integer.valueOf(timePicker.getHour()).toString()+":"+Integer.valueOf(timePicker.getMinute()).toString();
        String memo = editContents.getText().toString();
        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_TIME, time);
            cv.put(DBContract.DBEntry.SWITCH_CONDITION, "true");
            cv.put(DBContract.DBEntry.MEMO,memo);


            //新規登録mode
            if(id == 0) {
                // データ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME, null, cv);
//                登録したデータのidを取得
                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION,DBContract.DBEntry.MEMO};
                try {
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{time}
                            , null, null, null, null);
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(0);
                    }
                }catch (Exception e){
                    Log.e("title",e.toString());
                }

            } else {
                // データ更新
                db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmHelper.setAlarm(am, getApplicationContext(), timePicker.getHour(), timePicker.getMinute(), id);
        finish();
    }

    // 「戻る」ボタン　タップ時に呼び出されるメソッド
    public void onBack(View view) {
        // TextActivityを終了
        finish();
    }





}


//メモ
//もう少し楽にデーターベースの検索を行いたい
//取得するのはidまたは時間なので簡単な関数を作れるはず
//あとコードが汚すぎる
//あとデータベースをシングルトン化して楽に呼び出せるようにしたい
