package com.example.alarmclock;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.TimeZone;

public class AlarmSetScene extends AppCompatActivity {

    private int id = 0;
    private Button setbtn = null;
    private Button backbtn = null;
    private spinner_timepicker timePicker = null;
    private EditText editContents;
    private ConstraintLayout mConstraintLayout;
    private InputMethodManager inputMethodManager;
    private String memo;
//    private TextView rest_time_text;
    private TextView comments;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set_scene);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        comments = findViewById(R.id.comments);
        comments.setText("その調子!!!");


//        rest_time_text = findViewById(R.id.rest_time);

        timePicker = findViewById(R.id.spinner_time_picker);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
//                // Calendarを使って現在の時間をミリ秒で取得
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.getTimeInMillis();
//                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//                // 現在時刻を取得
//                Calendar nowCalendar = Calendar.getInstance();
//                nowCalendar.setTimeInMillis(System.currentTimeMillis());
//
//                int diff = calendar.compareTo(nowCalendar);
//                if(diff <= 0){
//                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
//                }
//
//                int rest_time = (int)((calendar.getTimeInMillis() - nowCalendar.getTimeInMillis()) / 1000); //秒
//                int rest_hour = rest_time / 3600; //時間
//                int rest_minutes = (rest_time - 3600 * rest_hour) / 60;
//                if (rest_hour == 0)
//                {
//                    rest_time_text.setText(rest_minutes + "分後にアラームが鳴ります");
//
//                }else
//                {
//                    rest_time_text.setText(rest_hour + "時間" + rest_minutes + "分後にアラームが鳴ります");
//                }
//
//            }
//        });
//        timePicker.setIs24HourView(true);
        setbtn = findViewById(R.id.setbtn);
        backbtn = findViewById(R.id.backbtn);
        editContents=findViewById(R.id.editContents);
        mConstraintLayout = findViewById(R.id.constrain_layout);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);





        //MainActivity(登録されたアラーム一覧が乗っているactivity)からintentを取得
        Intent intent = getIntent();

       //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBDef.DBEntry._ID, 0);

        //intentからidが取得できていればtiemPickerの表記を変更(編集mode)
        if(id>0){

            String time = intent.getStringExtra(DBDef.DBEntry.COLUMN_NAME_TIME);
            memo = intent.getStringExtra(DBDef.DBEntry.MEMO);
            String[] hour_minutes = time.split(":");
            timePicker.setHour(Integer.parseInt(String.valueOf(hour_minutes[0])));
            timePicker.setMinute(Integer.parseInt((String.valueOf((hour_minutes[1])))));
            editContents.setText(memo);
        }

//        String comment = "やらないやついねぇよなぁ？";
//        TextView commentsText = findViewById(R.id.comments);
//        SpannableString spanStr = new SpannableString(comment);
//        spanStr.setSpan(new UnderlineSpan(), 0, comment.length(), 0);
//        commentsText.setText(spanStr);
    }
    // 「設定」ボタン　タップ時に呼び出されるメソッド
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnReg_onClick(View view) {
        // ヘルパーを準備
        DatabaseHelper helper = new DatabaseHelper(this);
        // 入力欄に入力されたタイトルとコンテンツを取得
        String time = Integer.valueOf(timePicker.getHour()).toString()+":"+Integer.valueOf(timePicker.getMinute()).toString();
        String memo = editContents.getText().toString();
        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBDef.DBEntry.COLUMN_NAME_TIME, time);
            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "true");
            cv.put(DBDef.DBEntry.MEMO,memo);


            //新規登録mode
            if(id == 0) {
                // データ新規登録
                db.insert(DBDef.DBEntry.TABLE_NAME, null, cv);
//                登録したデータのidを取得
                String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION, DBDef.DBEntry.MEMO};
                try {
                    Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, DBDef.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{time}
                            , null, null, null, null);
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(0);
                    }
                }catch (Exception e){
                    Log.e("title",e.toString());
                }

            } else {
                // データ更新
                db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmHelper.setAlarm(am, getApplicationContext(), timePicker.getHour(), timePicker.getMinute(), id, memo);
        finish();
    }

    // 「戻る」ボタン　タップ時に呼び出されるメソッド
    public void onBack(View view) {
        // TextActivityを終了
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mConstraintLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        mConstraintLayout.requestFocus();
        return false;
    }
}



//メモ
//もう少し楽にデーターベースの検索を行いたい
//取得するのはidまたは時間なので簡単な関数を作れるはず
//あとコードが汚すぎる
//あとデータベースをシングルトン化して楽に呼び出せるようにしたい
